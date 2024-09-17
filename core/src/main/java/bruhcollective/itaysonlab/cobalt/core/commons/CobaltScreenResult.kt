package bruhcollective.itaysonlab.cobalt.core.commons

/**
 * Defines initial loading screen load result for Cobalt screens.
 */
enum class CobaltScreenResult {
    /**
     * Content is loading. Show the progress bar.
     */
    Loading,

    /**
     * Content loaded successfully. Show it.
     */
    Loaded,

    /**
     * A network error occurred. Show a generic network exception screen.
     */
    NetworkError,

    /**
     * An unknown error occurred. Show a generic error screen with an option to retry.
     */
    UnknownError
}