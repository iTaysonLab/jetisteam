package bruhcollective.itaysonlab.cobalt.news

import bruhcollective.itaysonlab.cobalt.news.models.NewsfeedType
import com.arkivanov.decompose.ComponentContext

internal class DefaultPickNewsfeedComponent (
    override val selectedFeed: NewsfeedType,
    private val onPick: (NewsfeedType) -> Unit,
    private val onDismiss: () -> Unit,
    componentContext: ComponentContext
): PickNewsfeedComponent, ComponentContext by componentContext {
    override fun selectFeed(type: NewsfeedType) {
        onPick(type)
        dismiss()
    }

    override fun dismiss() {
        onDismiss()
    }
}