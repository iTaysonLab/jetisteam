package bruhcollective.itaysonlab.cobalt.news.discover

import bruhcollective.itaysonlab.cobalt.core.decompose.componentCoroutineScope
import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.handlers.News
import bruhcollective.itaysonlab.ksteam.handlers.news
import bruhcollective.itaysonlab.ksteam.models.news.usernews.ActivityFeedEntry
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
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
    private val scope = componentCoroutineScope()
    private val loadMutex = Mutex()

    override val state = MutableValue(DiscoverComponent.DiscoverState.Idle)
    override val items = MutableValue(emptyList<ActivityFeedEntry>())
    override val canLoadMore = MutableValue(false)

    override fun dispatchInitialLoad() {
        if (loadMutex.isLocked) return

        scope.launch {
            loadMutex.withLock {
                state.value = DiscoverComponent.DiscoverState.Loading

                items.value = withContext(Dispatchers.IO) {
                    steamClient.ksteam.news.getUserNews(showEvents = News.UserNewsFilterScenario.FriendActivity).sortedByDescending(ActivityFeedEntry::date)
                }

                state.value = DiscoverComponent.DiscoverState.Loaded
            }
        }
    }

    override fun dispatchPagingLoad() {
        if (loadMutex.isLocked) return

    }
}