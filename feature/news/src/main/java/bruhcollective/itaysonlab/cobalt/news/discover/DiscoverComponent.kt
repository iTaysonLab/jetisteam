package bruhcollective.itaysonlab.cobalt.news.discover

import bruhcollective.itaysonlab.ksteam.models.news.usernews.ActivityFeedEntry
import com.arkivanov.decompose.value.Value

interface DiscoverComponent {
    val state: Value<DiscoverState>
    val items: Value<List<ActivityFeedEntry>>
    val canLoadMore: Value<Boolean>

    fun dispatchInitialLoad()
    fun dispatchPagingLoad()

    enum class DiscoverState {
        Idle, Loading, Loaded, Error
    }
}