package bruhcollective.itaysonlab.cobalt.root_flows

import bruhcollective.itaysonlab.cobalt.core.decompose.HandlesScrollToTopChild
import bruhcollective.itaysonlab.cobalt.core.decompose.HandlesScrollToTopComponent
import bruhcollective.itaysonlab.cobalt.library.DefaultLibraryComponent
import bruhcollective.itaysonlab.cobalt.published_files.DefaultPublishedFullscreenPhotoViewerComponent
import bruhcollective.itaysonlab.cobalt.published_files.PublishedFullscreenPhotoViewerComponent
import bruhcollective.itaysonlab.ksteam.models.publishedfiles.PublishedFile
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.items
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popToFirst
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

class DefaultRootLibraryFlowComponent (
    componentContext: ComponentContext
): RootLibraryFlowComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    override val scrollToTopFlag = MutableValue<Boolean>(false)
    override val stack: Value<ChildStack<*, RootLibraryFlowComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.LibraryRoot,
        childFactory = ::createChild,
        serializer = Config.serializer()
    )

    override fun scrollToTop() {
        if (stack.items.size > 1) {
            navigation.popToFirst()
        } else {
            (stack.active.instance as? HandlesScrollToTopChild)?.scrollToTop()
        }
    }

    override fun resetScrollToTop() {

    }

    private fun createChild(
        config: Config,
        componentContext: ComponentContext
    ): RootLibraryFlowComponent.Child {
        return when (config) {
            Config.LibraryRoot -> RootLibraryFlowComponent.Child.LibraryRoot(
                component = DefaultLibraryComponent(
                    componentContext = componentContext,
                    onScreenshotClicked = { file ->
                        navigation.pushNew(Config.PhotoViewer(file))
                    }
                )
            )

            is Config.PhotoViewer -> RootLibraryFlowComponent.Child.PhotoViewer(
                component = DefaultPublishedFullscreenPhotoViewerComponent(
                    componentContext = componentContext,
                    screenshot = config.file,
                    onBack = navigation::pop
                )
            )
        }
    }

    override fun onBackClicked() {
        navigation.pop()
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object LibraryRoot: Config

        @Serializable
        data class PhotoViewer (
            val file: PublishedFile.Screenshot
        ): Config
    }
}