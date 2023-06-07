package bruhcollective.itaysonlab.cobalt.core.ksteam

import android.util.Log
import bruhcollective.itaysonlab.ksteam.debug.KSteamLoggingVerbosity
import bruhcollective.itaysonlab.ksteam.debug.LoggingTransport

object KsteamAndroidLoggingTransport: LoggingTransport {
    override var verbosity: KSteamLoggingVerbosity = KSteamLoggingVerbosity.Verbose

    override fun printError(tag: String, message: String) {
        if (verbosity.atLeast(KSteamLoggingVerbosity.Error)) {
            Log.e(tag, message)
        }
    }

    override fun printWarning(tag: String, message: String) {
        if (verbosity.atLeast(KSteamLoggingVerbosity.Warning)) {
            Log.w(tag, message)
        }
    }

    override fun printDebug(tag: String, message: String) {
        if (verbosity.atLeast(KSteamLoggingVerbosity.Debug)) {
            Log.d(tag, message)
        }
    }

    override fun printVerbose(tag: String, message: String) {
        if (verbosity.atLeast(KSteamLoggingVerbosity.Verbose)) {
            Log.v(tag, message)
        }
    }
}