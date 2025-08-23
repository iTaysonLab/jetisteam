package bruhcollective.itaysonlab.cobalt.news

import bruhcollective.itaysonlab.cobalt.news.models.NewsfeedType
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.MutableValue

internal class DefaultWrappedNewsfeedComponent (
    componentContext: ComponentContext,
    onUpcomingCardClicked: () -> Unit,
): WrappedNewsfeedComponent, ComponentContext by componentContext {
    override val currentType = MutableValue<NewsfeedType>(NewsfeedType.Everything)

    private val pickerNavigator = SlotNavigation<NewsfeedType>()
    private val feedNavigator = SlotNavigation<NewsfeedType>()

    override val pickerSlot = childSlot(
        key = "PickerSlot",
        source = pickerNavigator,
        serializer = NewsfeedType.serializer(),
        childFactory = ::createPickerComponent
    )

    override val feedSlot = childSlot(
        key = "FeedSlot",
        source = feedNavigator,
        serializer = NewsfeedType.serializer(),
        initialConfiguration = { currentType.value },
        childFactory = ::createFeedComponent
    )

    override fun openPicker() {
        pickerNavigator.activate(currentType.value)
    }

    private fun createPickerComponent(configuration: NewsfeedType, componentContext: ComponentContext): PickNewsfeedComponent {
        return DefaultPickNewsfeedComponent(
            selectedFeed = configuration,
            componentContext = componentContext,
            onPick = { newType ->
                currentType.value = newType
                feedNavigator.activate(newType)
            }, onDismiss = pickerNavigator::dismiss
        )
    }

    private fun createFeedComponent(configuration: NewsfeedType, componentContext: ComponentContext): NewsfeedComponent {
        return DefaultNewsfeedComponent(
            type = configuration,
            componentContext = componentContext,
            onItemClicked = { item ->

            }
        )
    }
}