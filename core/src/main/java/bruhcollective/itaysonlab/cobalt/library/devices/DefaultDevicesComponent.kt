package bruhcollective.itaysonlab.cobalt.library.devices

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue

class DefaultDevicesComponent (
    componentContext: ComponentContext
) : DevicesComponent, ComponentContext by componentContext {
    override val scrollToTopFlag = MutableValue<Boolean>(false)

    override fun scrollToTop() {
        scrollToTopFlag.value = true
    }

    override fun resetScrollToTop() {
        scrollToTopFlag.value = false
    }
}