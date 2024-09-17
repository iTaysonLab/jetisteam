package bruhcollective.itaysonlab.cobalt.library.screenshots

import bruhcollective.itaysonlab.cobalt.core.commons.CobaltScreenResult
import bruhcollective.itaysonlab.cobalt.core.decompose.HandlesScrollToTopComponent
import bruhcollective.itaysonlab.ksteam.models.AppId
import bruhcollective.itaysonlab.ksteam.models.publishedfiles.PublishedFile
import com.arkivanov.decompose.value.Value
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.Serializable

interface ScreenshotsComponent: HandlesScrollToTopComponent {
    val screenResult: Value<CobaltScreenResult>
    val isRefreshing: Value<Boolean>
    val isPagingAvailable: Value<Boolean>

    val availableApplications: Value<ImmutableList<PickedApplication>>
    val selectedApplication: Value<PickedApplication>
    val screenshots: Value<ImmutableList<PublishedFile.Screenshot>>

    fun refresh()
    fun onScreenshotClicked(item: PublishedFile.Screenshot)
    fun onApplicationFilterClicked()
    fun onApplicationPicked(app: PickedApplication)
    suspend fun onPagingRequested()

    @Serializable
    data class PickedApplication(
        val id: Int = 0,
        val name: String = "",
    )
}