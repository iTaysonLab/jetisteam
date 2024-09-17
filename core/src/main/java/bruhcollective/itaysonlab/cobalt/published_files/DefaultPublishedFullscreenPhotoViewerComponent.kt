package bruhcollective.itaysonlab.cobalt.published_files

import bruhcollective.itaysonlab.ksteam.models.publishedfiles.PublishedFile
import com.arkivanov.decompose.ComponentContext

internal class DefaultPublishedFullscreenPhotoViewerComponent (
    componentContext: ComponentContext,
    screenshot: PublishedFile.Screenshot,
    private val onBack: () -> Unit,
): PublishedFullscreenPhotoViewerComponent, ComponentContext by componentContext {
    override val id: Long = screenshot.id
    override val previewUrl: String = screenshot.info.previewUrl
    override val url: String = screenshot.imageUrl

    override fun onBackPressed() {
        onBack()
    }
}