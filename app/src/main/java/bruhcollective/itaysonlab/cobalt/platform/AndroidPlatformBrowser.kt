package bruhcollective.itaysonlab.cobalt.platform

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import bruhcollective.itaysonlab.cobalt.core.platform.PlatformBrowser

class AndroidPlatformBrowser (
    private val context: Context
): PlatformBrowser {
    override fun openLink(url: String) {
        try {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                    addCategory(Intent.CATEGORY_BROWSABLE)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
        } catch (e: ActivityNotFoundException) {
            // Toast?
        }
    }
}