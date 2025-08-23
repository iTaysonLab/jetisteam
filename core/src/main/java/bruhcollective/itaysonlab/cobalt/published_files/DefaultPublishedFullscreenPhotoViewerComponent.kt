package bruhcollective.itaysonlab.cobalt.published_files

import android.content.Context
import bruhcollective.itaysonlab.cobalt.core.platform.GalleryUtils
import bruhcollective.itaysonlab.ksteam.models.publishedfiles.PublishedFile
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

internal class DefaultPublishedFullscreenPhotoViewerComponent (
    componentContext: ComponentContext,
    private val screenshot: PublishedFile.Screenshot
): PublishedFullscreenPhotoViewerComponent, KoinComponent, ComponentContext by componentContext {
    private val scope = coroutineScope()

    private val permissions = get<PermissionsController>()
    private val context = get<Context>()

    override val id: Long = screenshot.id
    override val previewUrl: String = screenshot.info.previewUrl
    override val url: String = screenshot.imageUrl
    override val saveToGalleryState = MutableValue(PublishedFullscreenPhotoViewerComponent.SaveToGalleryState.Awaiting)
    override val saveToGalleryProgress = MutableValue(0f)

    override fun formatDocumentShareUrl(): String {
        return "https://steamcommunity.com/sharedfiles/filedetails/?id=${screenshot.id}"
    }

    @OptIn(ExperimentalTime::class)
    override val infoSheetContent = PublishedFullscreenPhotoViewerComponent.InfoSheetContent(
        appId = screenshot.info.appId.value,
        appName = screenshot.info.appName,
        createdAt = Instant.fromEpochSeconds(screenshot.info.creationDate.toLong()),
        sizeBytes = screenshot.info.fileSize,
        resolutionX = screenshot.imageWidth,
        resolutionY = screenshot.imageHeight,
        views = screenshot.info.views,
        likes = screenshot.info.votesUp,
        favorites = screenshot.info.favorites,
        kvTags = screenshot.info.kvTags,
    )

    override fun dispatchSaveToGallery() {
        if (saveToGalleryState.value != PublishedFullscreenPhotoViewerComponent.SaveToGalleryState.Awaiting) return

        scope.launch {
            GalleryUtils.saveToGallery(
                permissionsController = permissions,
                ctx = context,
                url = screenshot.imageUrl,
                filename = "${screenshot.info.appName}_${screenshot.id}.jpg",
                video = false
            ).collect { state ->
                when (state) {
                    is GalleryUtils.SaveToGalleryState.Downloading -> {
                        saveToGalleryState.value = PublishedFullscreenPhotoViewerComponent.SaveToGalleryState.InProgress
                        saveToGalleryProgress.value = state.progress
                    }

                    is GalleryUtils.SaveToGalleryState.Error -> {
                        saveToGalleryProgress.value = 0f
                        saveToGalleryState.value = PublishedFullscreenPhotoViewerComponent.SaveToGalleryState.Error
                        delay(1500L)
                        saveToGalleryState.value = PublishedFullscreenPhotoViewerComponent.SaveToGalleryState.Awaiting
                    }

                    GalleryUtils.SaveToGalleryState.PermissionsRequired -> {
                        saveToGalleryProgress.value = 0f
                        saveToGalleryState.value = PublishedFullscreenPhotoViewerComponent.SaveToGalleryState.PermissionRequired
                        delay(1500L)
                        saveToGalleryState.value = PublishedFullscreenPhotoViewerComponent.SaveToGalleryState.Awaiting
                    }

                    GalleryUtils.SaveToGalleryState.Success -> {
                        saveToGalleryProgress.value = 0f
                        saveToGalleryState.value = PublishedFullscreenPhotoViewerComponent.SaveToGalleryState.Success
                        delay(1500L)
                        saveToGalleryState.value = PublishedFullscreenPhotoViewerComponent.SaveToGalleryState.Awaiting
                    }
                }
            }
        }
    }
}