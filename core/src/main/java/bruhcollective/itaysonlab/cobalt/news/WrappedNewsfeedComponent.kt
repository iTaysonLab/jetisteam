package bruhcollective.itaysonlab.cobalt.news

import bruhcollective.itaysonlab.cobalt.news.models.NewsfeedType
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value

/**
 * A wrapped newsfeed - basically a tab entry. It can switch between different feeds.
 */
interface WrappedNewsfeedComponent {
    val currentType: Value<NewsfeedType>

    val pickerSlot: Value<ChildSlot<NewsfeedType, PickNewsfeedComponent>>
    val feedSlot: Value<ChildSlot<NewsfeedType, NewsfeedComponent>>

    fun openPicker()
}