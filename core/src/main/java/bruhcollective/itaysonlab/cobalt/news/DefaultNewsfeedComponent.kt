package bruhcollective.itaysonlab.cobalt.news

import bruhcollective.itaysonlab.cobalt.core.commons.CobaltScreenResult
import bruhcollective.itaysonlab.cobalt.core.decompose.ViewModel
import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.cobalt.news.models.NewsfeedSource
import bruhcollective.itaysonlab.cobalt.news.models.NewsfeedType
import bruhcollective.itaysonlab.cobalt.news.paging.NewsfeedPagingItem
import bruhcollective.itaysonlab.ksteam.handlers.News
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.hours

class DefaultNewsfeedComponent(
    private val type: NewsfeedType,
    componentContext: ComponentContext
) : NewsfeedComponent, KoinComponent, ComponentContext by componentContext,
    CoroutineScope by componentContext.coroutineScope() {
    private companion object {
        private fun createSourceFromType(type: NewsfeedType): NewsfeedSource {
            return when (type) {
                NewsfeedType.Everything -> NewsfeedSource()
                NewsfeedType.Activity -> NewsfeedSource(includeNews = false)
                NewsfeedType.News -> NewsfeedSource(includeUserNews = false)
                NewsfeedType.Upcoming -> NewsfeedSource(context = NewsfeedSource.NewsfeedContext.Upcoming)
                NewsfeedType.Featured -> NewsfeedSource(includeUserNews = false, newsCollectionId = News.Collections.Featured)
                NewsfeedType.Press -> NewsfeedSource(includeUserNews = false, newsCollectionId = News.Collections.Press)
                NewsfeedType.Steam -> NewsfeedSource(includeUserNews = false, newsCollectionId = News.Collections.Steam)
            }
        }
    }

    private val source: NewsfeedSource = createSourceFromType(type)

    private val viewModel: NewsfeedViewModel =
        instanceKeeper.getOrCreate(key = type) { NewsfeedViewModel() }
    private val steamClient: SteamClient by inject()

    override val state: Value<CobaltScreenResult> get() = viewModel.state
    override val items: Value<ImmutableList<NewsfeedPagingItem>> get() = viewModel.items
    override val upcomingItems: Value<ImmutableList<NewsfeedPagingItem>> get() = viewModel.upcomingItems
    override val canLoadMore: Value<Boolean> get() = viewModel.canLoadMore
    override val isLoading: Value<Boolean> get() = viewModel.isLoading

    override fun dispatchLoad() {
        launch {
            runCatching {
                load()
            }.onSuccess {
                viewModel.submitState(CobaltScreenResult.Loaded)
            }.onFailure { e ->
                viewModel.submitState(CobaltScreenResult.Error(e))
            }
        }
    }

    override fun refresh() {
        viewModel.reset()
        dispatchLoad()
    }

    private suspend fun load() {
        viewModel.submitIsLoading(true)

        withContext(Dispatchers.IO) {
            val pagingKey = viewModel.pagingKey ?: Clock.System.now()
            val pagingKeyRange = pagingKey - 24.hours

            if (type != NewsfeedType.Upcoming) {
                if (type == NewsfeedType.Everything && upcomingItems.value.isEmpty() && items.value.isEmpty()) {
                    steamClient.ksteam.news.getUpcomingEvents(maxCount = 1)
                        .map(NewsfeedPagingItem::SteamNewsPost)
                        .sortedByDescending(NewsfeedPagingItem::date)
                        .take(1)
                        .let(viewModel::submitUpcomingItems)
                }

                val userNews =
                    if ((source.context is NewsfeedSource.NewsfeedContext.None || source.context is NewsfeedSource.NewsfeedContext.Application) && source.includeUserNews) {
                        steamClient.ksteam.userNews.getUserNews(
                            showEvents = source.userNewsContentTypes,
                            startTime = pagingKeyRange.epochSeconds.toInt(),
                            endTime = pagingKey.epochSeconds.toInt(),
                            count = null,
                            appId = (source.context as? NewsfeedSource.NewsfeedContext.Application)?.id?.value ?: 0
                        ).map(NewsfeedPagingItem::UserActivityUpdate)
                    } else {
                        emptyList()
                    }

                val news = if (source.includeNews) {
                    steamClient.ksteam.news.getEventsInCalendarRange(
                        range = pagingKeyRange.epochSeconds..pagingKey.epochSeconds,
                        eventTypes = source.newsContentTypes,
                        appTypes = source.newsAppTypes,
                        collectionId = source.newsCollectionId
                    ).map(NewsfeedPagingItem::SteamNewsPost)
                } else {
                    emptyList()
                }

                val sortedNews = (userNews + news).sortedByDescending(NewsfeedPagingItem::date)

                viewModel.submitItems(
                    list = sortedNews,
                    key = pagingKeyRange
                )
            } else {
                val upcomingNews = steamClient.ksteam.news.getUpcomingEvents(
                    maxCount = 250,
                    eventTypes = source.newsContentTypes,
                    appTypes = source.newsAppTypes,
                ).map(NewsfeedPagingItem::SteamNewsPost).sortedByDescending(NewsfeedPagingItem::date)

                viewModel.submitItems(
                    list = upcomingNews,
                    key = null
                )
            }
        }

        viewModel.submitIsLoading(false)
    }

    private class NewsfeedViewModel : ViewModel() {
        val state = MutableValue<CobaltScreenResult>(CobaltScreenResult.Loading)
        val upcomingItems = MutableValue<PersistentList<NewsfeedPagingItem>>(persistentListOf())
        val items = MutableValue<PersistentList<NewsfeedPagingItem>>(persistentListOf())

        val canLoadMore = MutableValue<Boolean>(false)
        val isLoading = MutableValue<Boolean>(false)

        var pagingKey: Instant? = null
            private set

        //

        fun reset() {
            pagingKey = null
            state.value = CobaltScreenResult.Loaded
            upcomingItems.value = persistentListOf()
            items.value = persistentListOf()
            canLoadMore.value = false
            isLoading.value = false
        }

        fun submitState(value: CobaltScreenResult) {
            state.value = value
        }

        fun submitIsLoading(value: Boolean) {
            isLoading.value = value
        }

        fun submitUpcomingItems(list: List<NewsfeedPagingItem>) {
            upcomingItems.value = list.toPersistentList()
        }

        fun submitItems(list: List<NewsfeedPagingItem>, key: Instant?) {
            items.update { it.addAll(list) }
            pagingKey = key
            canLoadMore.value = key != null
        }
    }
}