package bruhcollective.itaysonlab.cobalt.published_files

interface PublishedFullscreenPhotoViewerComponent {
    val id: Long
    val previewUrl: String
    val url: String

    fun onBackPressed()
}