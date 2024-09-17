package bruhcollective.itaysonlab.cobalt.library

import bruhcollective.itaysonlab.cobalt.core.decompose.HandlesScrollToTopChild
import bruhcollective.itaysonlab.cobalt.core.decompose.HandlesScrollToTopComponent
import bruhcollective.itaysonlab.cobalt.library.devices.DefaultDevicesComponent
import bruhcollective.itaysonlab.cobalt.library.games.DefaultGamesComponent
import bruhcollective.itaysonlab.cobalt.library.screenshots.DefaultScreenshotsComponent
import bruhcollective.itaysonlab.ksteam.models.publishedfiles.PublishedFile
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.children.ChildNavState.Status
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.router.pages.Pages
import com.arkivanov.decompose.router.pages.childPages
import com.arkivanov.decompose.router.pages.PagesNavigation
import com.arkivanov.decompose.router.pages.select
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

class DefaultLibraryComponent(
    componentContext: ComponentContext,
    private val onScreenshotClicked: (PublishedFile.Screenshot) -> Unit
): LibraryComponent, ComponentContext by componentContext {
    private val navigation = PagesNavigation<Config>()

    override val scrollToTopFlag = MutableValue<Boolean>(false)

    override fun scrollToTop() {
        (pages.value.items[pages.value.selectedIndex].instance as? HandlesScrollToTopChild)?.scrollToTop()
    }

    override fun resetScrollToTop() {

    }

    override val pages: Value<ChildPages<*, LibraryComponent.Child>> = childPages(
        source = navigation,
        childFactory = ::createChild,
        initialPages = ::createInitialPages,
        pageStatus = { index, pages ->
            if (index == pages.selectedIndex) {
                Status.RESUMED
            } else {
                Status.CREATED
            }
        },
        serializer = Config.serializer()
    )

    private fun createInitialPages(): Pages<Config> {
        return Pages(listOf(Config.Devices, Config.Games, Config.Screenshots), selectedIndex = 1)
    }

    private fun createChild(
        config: Config,
        componentContext: ComponentContext
    ): LibraryComponent.Child {
        return when (config) {
            Config.Devices -> LibraryComponent.Child.Devices(
                component = DefaultDevicesComponent(componentContext = componentContext)
            )

            Config.Games -> LibraryComponent.Child.Games(
                component = DefaultGamesComponent(componentContext = componentContext)
            )

            Config.Screenshots -> LibraryComponent.Child.Screenshots(
                component = DefaultScreenshotsComponent(componentContext = componentContext, onScreenshotClicked = onScreenshotClicked)
            )
        }
    }

    override fun selectPage(index: Int) {
        navigation.select(index)
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Devices: Config

        @Serializable
        data object Games: Config

        @Serializable
        data object Screenshots: Config
    }
}