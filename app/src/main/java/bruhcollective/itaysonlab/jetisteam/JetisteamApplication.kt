package bruhcollective.itaysonlab.jetisteam

import android.app.Application
import android.os.Build
import android.util.Log
import bruhcollective.itaysonlab.jetisteam.util.AnimatedPngDecoder
import bruhcollective.itaysonlab.ksteam.debug.Logging
import bruhcollective.itaysonlab.ksteam.debug.LoggingTransport
import bruhcollective.itaysonlab.ksteam.debug.LoggingVerbosity
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class JetisteamApplication : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()

        Logging.transport = object: LoggingTransport {
            override var verbosity: LoggingVerbosity = LoggingVerbosity.Verbose

            override fun printDebug(tag: String, message: String) {
                Log.d(tag, message)
            }

            override fun printError(tag: String, message: String) {
                Log.e(tag, message)
            }

            override fun printVerbose(tag: String, message: String) {
                Log.v(tag, message)
            }

            override fun printWarning(tag: String, message: String) {
                Log.w(tag, message)
            }
        }
    }

    override fun newImageLoader() = ImageLoader.Builder(applicationContext).components {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            add(ImageDecoderDecoder.Factory())
        } else {
            add(GifDecoder.Factory())
        }

        add(AnimatedPngDecoder.Factory())
    }.build()
}