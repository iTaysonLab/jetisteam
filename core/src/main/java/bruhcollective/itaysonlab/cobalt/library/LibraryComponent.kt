package bruhcollective.itaysonlab.cobalt.library

import bruhcollective.itaysonlab.cobalt.core.decompose.HandlesScrollToTopChild
import bruhcollective.itaysonlab.cobalt.core.decompose.HandlesScrollToTopComponent
import bruhcollective.itaysonlab.cobalt.library.devices.DevicesComponent
import bruhcollective.itaysonlab.cobalt.library.games.GamesComponent
import bruhcollective.itaysonlab.cobalt.library.screenshots.ScreenshotsComponent
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.value.Value

interface LibraryComponent: HandlesScrollToTopComponent {
    val pages: Value<ChildPages<*, Child>>

    fun selectPage(index: Int)

    sealed interface Child {
        class Devices (
            val component: DevicesComponent
        ): Child, HandlesScrollToTopChild {
            override fun scrollToTop() = component.scrollToTop()
        }

        class Games (
            val component: GamesComponent
        ): Child, HandlesScrollToTopChild {
            override fun scrollToTop() = component.scrollToTop()
        }

        class Screenshots (
            val component: ScreenshotsComponent
        ): Child, HandlesScrollToTopChild {
            override fun scrollToTop() = component.scrollToTop()
        }
    }
}