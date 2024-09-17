package bruhcollective.itaysonlab.cobalt.library.screenshots

import bruhcollective.itaysonlab.cobalt.core.commons.CobaltScreenResult
import bruhcollective.itaysonlab.ksteam.ExtendedSteamClient
import bruhcollective.itaysonlab.ksteam.handlers.PublishedFiles.PersonalBrowseFilter
import bruhcollective.itaysonlab.ksteam.handlers.PublishedFiles.PersonalPrivacyFilter
import bruhcollective.itaysonlab.ksteam.handlers.PublishedFiles.PersonalSortOrder
import bruhcollective.itaysonlab.ksteam.models.AppId
import bruhcollective.itaysonlab.ksteam.models.enums.EPublishedFileInfoMatchingFileType
import bruhcollective.itaysonlab.ksteam.models.publishedfiles.PublishedFile
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnResume
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class DefaultScreenshotsComponent (
    componentContext: ComponentContext,
    private val onScreenshotClicked: (PublishedFile.Screenshot) -> Unit,
): ScreenshotsComponent, KoinComponent, ComponentContext by componentContext, CoroutineScope by componentContext.coroutineScope() {
    private var currentPage = 1

    private val steamClient: ExtendedSteamClient = get()

    override val screenResult = MutableValue<CobaltScreenResult>(CobaltScreenResult.Loading)
    override val selectedApplication = MutableValue<ScreenshotsComponent.PickedApplication>(ScreenshotsComponent.PickedApplication())
    override val screenshots = MutableValue<ImmutableList<PublishedFile.Screenshot>>(persistentListOf())

    override val availableApplications = MutableValue<ImmutableList<ScreenshotsComponent.PickedApplication>>(persistentListOf())
    override val isRefreshing = MutableValue<Boolean>(false)
    override val isPagingAvailable = MutableValue<Boolean>(false)

    val currentSortOrder = MutableValue(PersonalSortOrder.LastUpdated)
    val currentPrivacyFilter = MutableValue(PersonalPrivacyFilter.Everything)
    val currentBrowseFilter = MutableValue(PersonalBrowseFilter.Created)

    override val scrollToTopFlag = MutableValue<Boolean>(false)

    override fun scrollToTop() {
        scrollToTopFlag.value = true
    }

    override fun resetScrollToTop() {
        scrollToTopFlag.value = false
    }

    override fun refresh() {
        currentPage = 1
        isRefreshing.value = true

        launch {
            currentPage = 1
            screenshots.value = requestScreenshots().toImmutableList()
            isRefreshing.value = false
        }
    }

    init {
        doOnResume(isOneTime = true) {
            currentPage = 1
            screenResult.value = CobaltScreenResult.Loading
            screenshots.value = persistentListOf()

            launch {
                runCatching {
                    screenshots.value = requestScreenshots().toImmutableList()
                    screenResult.value = CobaltScreenResult.Loaded
                }.onFailure {
                    screenResult.value = CobaltScreenResult.UnknownError
                }
            }
        }
    }

    override suspend fun onPagingRequested() {
        steamClient.logger.logDebug("UI:Screenshots") { "[dispatchNewPageRequest]" }

        currentPage++
        screenshots.value = (screenshots.value + requestScreenshots()).toImmutableList()
    }

    override fun onApplicationPicked(app: ScreenshotsComponent.PickedApplication) {

    }

    private suspend fun requestScreenshots(
        appId: Int = selectedApplication.value.id,
        sortOrder: PersonalSortOrder = currentSortOrder.value,
        privacyFilter: PersonalPrivacyFilter = currentPrivacyFilter.value,
        browseFilter: PersonalBrowseFilter = currentBrowseFilter.value,
        page: Int = currentPage,
    ): List<PublishedFile.Screenshot> {
        steamClient.logger.logDebug("UI:Screenshots") { "[requestScreenshots] $appId $sortOrder $privacyFilter $browseFilter $page" }

        return steamClient.publishedFiles.getFiles(
            steamId = steamClient.currentSessionSteamId,
            fileType = EPublishedFileInfoMatchingFileType.Screenshots,
            count = 40,
            page = page,
            appId = AppId(appId),
            sortOrder = sortOrder,
            privacyFilter = privacyFilter,
            browseFilter = browseFilter,
            returnApps = page == 1
        ).also { container ->
            isPagingAvailable.value = container.files.size + screenshots.value.size < container.total

            container.apps.takeIf {
                it.isNotEmpty()
            }?.let {
                availableApplications.value = it.map {
                    app -> ScreenshotsComponent.PickedApplication(app.id.value, app.name)
                }.toImmutableList()
            }
        }.files.filterIsInstance<PublishedFile.Screenshot>()
    }

    override fun onScreenshotClicked(item: PublishedFile.Screenshot) {
        onScreenshotClicked.invoke(item)
    }

    override fun onApplicationFilterClicked() {

    }
}