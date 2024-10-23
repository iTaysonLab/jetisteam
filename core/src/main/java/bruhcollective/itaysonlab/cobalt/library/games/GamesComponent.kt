package bruhcollective.itaysonlab.cobalt.library.games

import bruhcollective.itaysonlab.cobalt.core.commons.CobaltScreenResult
import bruhcollective.itaysonlab.cobalt.core.decompose.HandlesScrollToTopComponent
import bruhcollective.itaysonlab.ksteam.models.app.OwnedSteamApplication
import bruhcollective.itaysonlab.ksteam.models.library.LibraryCollection
import com.arkivanov.decompose.value.Value
import kotlinx.collections.immutable.ImmutableList

interface GamesComponent: HandlesScrollToTopComponent {
    /**
     * Page state.
     */
    val screenResult: Value<CobaltScreenResult>

    /**
     * Currently available CUSTOM collections.
     */
    val collections: Value<ImmutableList<LibraryCollection>>

    /**
     * Filtered list of games.
     */
    val games: Value<ImmutableList<OwnedSteamApplication>>

    /**
     * Selected collection ID. Empty string means "all owned games".
     */
    val currentCollectionId: Value<String>
    val currentCollectionName: Value<String>

    /**
     * Search query. Empty string means no search applied.
     */
    val currentSearchQuery: Value<String>

    fun clearCollection()
    fun setCollection(collection: LibraryCollection)
}