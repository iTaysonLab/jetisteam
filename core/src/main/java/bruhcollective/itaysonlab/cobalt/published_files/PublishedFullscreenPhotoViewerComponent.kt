package bruhcollective.itaysonlab.cobalt.published_files

import com.arkivanov.decompose.value.Value
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

interface PublishedFullscreenPhotoViewerComponent {
    val id: Long
    val previewUrl: String
    val url: String

    val saveToGalleryState: Value<SaveToGalleryState>
    val saveToGalleryProgress: Value<Float>

    val infoSheetContent: InfoSheetContent
    fun formatDocumentShareUrl(): String

    @OptIn(ExperimentalTime::class)
    data class InfoSheetContent (
        // App
        val appId: Int,
        val appName: String,

        // Metadata
        val createdAt: Instant,
        val sizeBytes: Long,
        val resolutionX: Int,
        val resolutionY: Int,

        //
        val views: Int,
        val likes: Int,
        val favorites: Int,
        val kvTags: Map<String, String>
    )

    enum class SaveToGalleryState {
        Awaiting, InProgress, PermissionRequired, Error, Success
    }

    fun dispatchSaveToGallery()
}