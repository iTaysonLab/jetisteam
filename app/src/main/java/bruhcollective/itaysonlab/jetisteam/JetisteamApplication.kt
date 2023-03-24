package bruhcollective.itaysonlab.jetisteam

import android.app.Application
import android.os.Build
import bruhcollective.itaysonlab.jetisteam.util.AnimatedPngDecoder
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class JetisteamApplication : Application(), ImageLoaderFactory {

    override fun newImageLoader() = ImageLoader.Builder(applicationContext).components {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            add(ImageDecoderDecoder.Factory())
        } else {
            add(GifDecoder.Factory())
        }

        add(AnimatedPngDecoder.Factory())
    }.build()
}