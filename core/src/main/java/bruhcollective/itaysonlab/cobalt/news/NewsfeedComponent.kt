package bruhcollective.itaysonlab.cobalt.news

import bruhcollective.itaysonlab.cobalt.core.commons.CobaltScreenResult
import bruhcollective.itaysonlab.cobalt.news.discover.DiscoverComponent.DiscoverItem
import bruhcollective.itaysonlab.cobalt.news.discover.DiscoverComponent.DiscoverState
import bruhcollective.itaysonlab.cobalt.news.paging.NewsfeedPagingItem
import com.arkivanov.decompose.value.Value
import kotlinx.collections.immutable.ImmutableList

interface NewsfeedComponent {
    val state: Value<CobaltScreenResult>
    val items: Value<ImmutableList<NewsfeedPagingItem>>
    val canLoadMore: Value<Boolean>

    fun dispatchInitialLoad()
    fun dispatchPagingLoad()
}