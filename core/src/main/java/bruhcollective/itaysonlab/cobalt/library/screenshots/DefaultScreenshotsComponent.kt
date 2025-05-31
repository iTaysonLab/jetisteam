package bruhcollective.itaysonlab.cobalt.library.screenshots

import bruhcollective.itaysonlab.cobalt.core.commons.CobaltScreenResult
import bruhcollective.itaysonlab.cobalt.published_files.DefaultPublishedFullscreenPhotoViewerComponent
import bruhcollective.itaysonlab.ksteam.ExtendedSteamClient
import bruhcollective.itaysonlab.ksteam.handlers.PublishedFiles.PersonalBrowseFilter
import bruhcollective.itaysonlab.ksteam.handlers.PublishedFiles.PersonalPrivacyFilter
import bruhcollective.itaysonlab.ksteam.handlers.PublishedFiles.PersonalSortOrder
import bruhcollective.itaysonlab.ksteam.models.AppId
import bruhcollective.itaysonlab.ksteam.models.enums.EPublishedFileInfoMatchingFileType
import bruhcollective.itaysonlab.ksteam.models.publishedfiles.PublishedFile
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.coroutines.withLifecycle
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnResume
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class DefaultScreenshotsComponent (
    componentContext: ComponentContext
): ScreenshotsComponent, KoinComponent, ComponentContext by componentContext, CoroutineScope by componentContext.coroutineScope() {
    private var currentPage = 1

    private val steamClient: ExtendedSteamClient = get()

    override val screenResult = MutableValue<CobaltScreenResult>(CobaltScreenResult.Loading)
    override val selectedApplication = MutableValue<ScreenshotsComponent.PickedApplication>(ScreenshotsComponent.PickedApplication())
    override val screenshots = MutableValue<ImmutableList<PublishedFile.Screenshot>>(persistentListOf())

    override val isRefreshing = MutableValue<Boolean>(false)
    override val isPagingAvailable = MutableValue<Boolean>(false)

    private val currentFileQuery = MutableStateFlow(ScreenshotsComponent.FileQuery())

    private val alertNavigation = SlotNavigation<AlertConfig>()
    override val alertChild: Value<ChildSlot<*, ScreenshotsComponent.AlertChild>>
        = childSlot(source = alertNavigation, serializer = AlertConfig.serializer(), childFactory = ::createAlertChild)

    override fun refresh() {
        isRefreshing.value = true

        launch {
            resetLoad(currentFileQuery.value)
            isRefreshing.value = false
        }
    }

    init {
        doOnCreate {
            launch {
                currentFileQuery.collect { query ->
                    resetState()
                    resetLoad(query)
                }
            }
        }
    }

    private suspend fun resetLoad(query: ScreenshotsComponent.FileQuery) {
        println("currentFileQuery $query")

        runCatching {
            currentPage = 1
            screenshots.value = requestScreenshots(query).toImmutableList()
            screenResult.value = CobaltScreenResult.Loaded
        }.onFailure { e ->
            screenResult.value = CobaltScreenResult.Error(e)
        }
    }

    private fun resetState() {
        screenResult.value = CobaltScreenResult.Loading
        screenshots.value = persistentListOf()
    }

    override suspend fun onPagingRequested() {
        steamClient.logger.logDebug("UI:Screenshots") { "[dispatchNewPageRequest]" }

        currentPage++
        screenshots.value = (screenshots.value + requestScreenshots(currentFileQuery.value)).toImmutableList()
    }

    private suspend fun requestScreenshots(query: ScreenshotsComponent.FileQuery, page: Int = currentPage): List<PublishedFile.Screenshot> {
        steamClient.logger.logDebug("UI:Screenshots") { "[requestScreenshots] $query x $page" }

        return steamClient.publishedFiles.getFiles(
            steamId = steamClient.currentSessionSteamId,
            fileType = EPublishedFileInfoMatchingFileType.Screenshots,
            count = 40,
            page = page,
            appId = AppId(query.appId),
            sortOrder = query.sortOrder,
            privacyFilter = query.privacyFilter,
            browseFilter = query.browseFilter,
            returnApps = page == 1
        ).also { container ->
            isPagingAvailable.value = container.files.size + screenshots.value.size < container.total
        }.files.filterIsInstance<PublishedFile.Screenshot>()
    }

    override fun onScreenshotClicked(item: PublishedFile.Screenshot) {
        alertNavigation.activate(AlertConfig.ViewScreenshot(item))
    }

    override fun onApplicationFilterClicked() {
        alertNavigation.activate(AlertConfig.PickApplication(currentFileQuery.value.browseFilter))
    }

    override fun onFilterClicked() {
        alertNavigation.activate(AlertConfig.SetParameters(currentFileQuery.value))
    }

    override fun dismissAlert() {
        alertNavigation.dismiss()
    }

    private fun onApplicationSelected(app: ScreenshotsComponent.PickedApplication) {
        selectedApplication.value = app
        currentFileQuery.update { query -> query.copy(appId = app.id) }
    }

    private fun createAlertChild(config: AlertConfig, componentContext: ComponentContext): ScreenshotsComponent.AlertChild {
        return when (config) {
            is AlertConfig.PickApplication -> {
                ScreenshotsComponent.AlertChild.PickApplication(
                    component = DefaultSelectApplicationSheetComponent(
                        browseFilter = config.browseFilter,
                        componentContext = componentContext,
                        onApplicationSelected = { app ->
                            onApplicationSelected(app)
                            dismissAlert()
                        }
                    )
                )
            }

            is AlertConfig.SetParameters -> {
                ScreenshotsComponent.AlertChild.SetParameters(
                    component = DefaultScreenshotsFilterSheetComponent(
                        currentQuery = config.currentQuery,
                        onQueryChanged = { query ->
                            currentFileQuery.value = query
                            dismissAlert()
                        },
                        componentContext = componentContext,
                    )
                )
            }

            is AlertConfig.ViewScreenshot -> {
                ScreenshotsComponent.AlertChild.PhotoViewer(
                    component = DefaultPublishedFullscreenPhotoViewerComponent(
                        screenshot = config.payload,
                        componentContext = componentContext
                    )
                )
            }
        }
    }

    @Serializable
    private sealed interface AlertConfig {
        @Serializable
        @SerialName("pick_app")
        data class PickApplication(
            val browseFilter: PersonalBrowseFilter
        ): AlertConfig

        @Serializable
        @SerialName("set_params")
        data class SetParameters(
            val currentQuery: ScreenshotsComponent.FileQuery
        ): AlertConfig

        @Serializable
        @SerialName("view_screenshot")
        data class ViewScreenshot(
            val payload: PublishedFile.Screenshot
        ): AlertConfig
    }
}