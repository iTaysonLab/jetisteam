package bruhcollective.itaysonlab.cobalt.library.screenshots

import bruhcollective.itaysonlab.cobalt.core.commons.CobaltScreenResult
import bruhcollective.itaysonlab.cobalt.published_files.PublishedFullscreenPhotoViewerComponent
import bruhcollective.itaysonlab.ksteam.handlers.PublishedFiles.PersonalBrowseFilter
import bruhcollective.itaysonlab.ksteam.handlers.PublishedFiles.PersonalPrivacyFilter
import bruhcollective.itaysonlab.ksteam.handlers.PublishedFiles.PersonalSortOrder
import bruhcollective.itaysonlab.ksteam.models.publishedfiles.PublishedFile
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.Serializable

interface ScreenshotsComponent {
    val alertChild: Value<ChildSlot<*, AlertChild>>

    val screenResult: Value<CobaltScreenResult>
    val isRefreshing: Value<Boolean>
    val isPagingAvailable: Value<Boolean>

    val selectedApplication: Value<PickedApplication>
    val screenshots: Value<ImmutableList<PublishedFile.Screenshot>>

    fun refresh()
    fun onScreenshotClicked(item: PublishedFile.Screenshot)
    fun onApplicationFilterClicked()
    fun onFilterClicked()
    fun dismissAlert()
    suspend fun onPagingRequested()

    @Serializable
    data class PickedApplication(
        val id: Int = 0,
        val name: String = "",
    )

    @Serializable
    data class FileQuery (
        val appId: Int = 0,
        val sortOrder: PersonalSortOrder = PersonalSortOrder.NewestFirst,
        val privacyFilter: PersonalPrivacyFilter = PersonalPrivacyFilter.Everything,
        val browseFilter: PersonalBrowseFilter = PersonalBrowseFilter.Created,
    )

    sealed interface AlertChild {
        class PickApplication (
            val component: SelectApplicationSheetComponent
        ): AlertChild

        class SetParameters (
            val component: ScreenshotsFilterSheetComponent
        ): AlertChild

        class PhotoViewer (
            val component: PublishedFullscreenPhotoViewerComponent
        ): AlertChild
    }
}