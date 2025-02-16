package bruhcollective.itaysonlab.cobalt.core.commons

import bruhcollective.itaysonlab.ksteam.network.exception.CMJobDroppedException
import okio.IOException

/**
 * Defines initial loading screen load result for Cobalt screens.
 */
sealed interface CobaltScreenResult {
    /**
     * Content is loading. Show the progress bar.
     */
    data object Loading: CobaltScreenResult

    /**
     * Content loaded successfully. Show it.
     */
    data object Loaded: CobaltScreenResult

    /**
     * An error occurred. Show a generic network exception screen.
     */
    data class Error (
        val exception: Throwable,
    ): CobaltScreenResult {
        val isNetworkException: Boolean
            = exception is IOException || exception is CMJobDroppedException
    }
}