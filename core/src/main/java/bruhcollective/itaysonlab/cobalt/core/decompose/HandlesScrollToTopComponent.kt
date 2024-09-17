package bruhcollective.itaysonlab.cobalt.core.decompose

import com.arkivanov.decompose.value.Value

interface HandlesScrollToTopComponent {
    val scrollToTopFlag: Value<Boolean>

    fun scrollToTop()
    fun resetScrollToTop()
}