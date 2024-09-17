package bruhcollective.itaysonlab.cobalt.root_flows

import bruhcollective.itaysonlab.cobalt.core.decompose.HandlesScrollToTopChild
import bruhcollective.itaysonlab.cobalt.core.decompose.HandlesScrollToTopComponent
import bruhcollective.itaysonlab.cobalt.guard.GuardComponent.Child
import bruhcollective.itaysonlab.cobalt.library.LibraryComponent
import bruhcollective.itaysonlab.cobalt.published_files.PublishedFullscreenPhotoViewerComponent
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner

interface RootLibraryFlowComponent: BackHandlerOwner, HandlesScrollToTopComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class LibraryRoot (
            val component: LibraryComponent
        ): Child, HandlesScrollToTopChild {
            override fun scrollToTop() = component.scrollToTop()
        }

        class PhotoViewer (
            val component: PublishedFullscreenPhotoViewerComponent
        ): Child
    }

    fun onBackClicked()
}