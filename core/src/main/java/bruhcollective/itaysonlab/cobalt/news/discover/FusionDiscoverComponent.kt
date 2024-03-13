package bruhcollective.itaysonlab.cobalt.news.discover

import bruhcollective.itaysonlab.cobalt.core.decompose.ViewModel
import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.handlers.News
import bruhcollective.itaysonlab.ksteam.handlers.UserNews
import bruhcollective.itaysonlab.ksteam.handlers.news
import bruhcollective.itaysonlab.ksteam.handlers.userNews
import bruhcollective.itaysonlab.ksteam.models.news.NewsEvent
import bruhcollective.itaysonlab.ksteam.models.news.usernews.ActivityFeedEntry
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.instancekeeper.getOrCreate
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FusionDiscoverComponent (
    componentContext: ComponentContext
): DiscoverComponent, ComponentContext by componentContext, KoinComponent {
    private val steamClient: SteamClient by inject()

    private val viewModel: FusionViewModel = instanceKeeper.getOrCreate {
        FusionViewModel(ksNews = steamClient.ksteam.news, ksUserNews = steamClient.ksteam.userNews)
    }

    override val state get() = viewModel.state
    override val canLoadMore get() = viewModel.canLoadMore
    override val items get() = viewModel.items
    override fun dispatchInitialLoad() = viewModel.dispatchLoad()
    override fun dispatchPagingLoad() {}

    private class FusionViewModel(
        private val ksUserNews: UserNews,
        private val ksNews: News
    ): ViewModel() {
        private val loadMutex = Mutex()

        val state = MutableValue(DiscoverComponent.DiscoverState.Idle)
        val canLoadMore = MutableValue(false)
        val items: MutableValue<ImmutableList<DiscoverComponent.DiscoverItem>> = MutableValue(persistentListOf())

        fun dispatchLoad() {
            if (loadMutex.isLocked) return

            viewModelScope.launch {
                loadMutex.withLock {
                    state.value = DiscoverComponent.DiscoverState.Loading

                    items.value = withContext(Dispatchers.IO) {
                        val userNews = ksUserNews.getUserNews(
                            showEvents = UserNews.UserNewsFilterScenario.FriendActivity,
                            count = 25 // 25 is enough-ish, we can TODO implement paging
                        ).sortedByDescending(ActivityFeedEntry::date)

                        val lastUserNewsDate = userNews.lastOrNull()?.date?.toLong() ?: 0L
                        val currentClock = System.currentTimeMillis() / 1000

                        val upcomingEvents = ksNews.getUpcomingEvents(maxCount = 5).toImmutableList()
                        val steamNewsUpToUser = ksNews.getEventsInCalendarRange(range = lastUserNewsDate..currentClock)

                        val afData = userNews.map(::createActivityFeed)
                        val nfData = steamNewsUpToUser.map(::createNewsPost)

                        buildList {
                            if (upcomingEvents.isNotEmpty()) {
                                add(DiscoverComponent.DiscoverItem.UpcomingEvents(events = upcomingEvents))
                            }

                            addAll(
                                (afData + nfData).sortedByDescending(DiscoverComponent.DiscoverItem::date)
                            )
                        }.toImmutableList()
                    }

                    state.value = DiscoverComponent.DiscoverState.Loaded
                }
            }
        }

        private fun createNewsPost(entry: NewsEvent) = DiscoverComponent.DiscoverItem.NewsPost(entry)
        private fun createActivityFeed(entry: ActivityFeedEntry) = DiscoverComponent.DiscoverItem.ActivityFeed(entry)
    }
}