package bruhcollective.itaysonlab.cobalt.library.games

import bruhcollective.itaysonlab.ksteam.models.app.SteamApplication
import bruhcollective.itaysonlab.ksteam.models.library.LibraryCollection
import com.arkivanov.decompose.value.Value
import kotlinx.collections.immutable.ImmutableList

interface GamesComponent {
    /**
     * Currently available CUSTOM collections.
     */
    val collections: Value<ImmutableList<LibraryCollection>>

    /**
     * Filtered list of games.
     */
    val games: Value<ImmutableList<SteamApplication>>

    /**
     * Selected collection ID. Empty string means "all owned games".
     */
    val currentCollectionId: Value<String>

    /**
     * Search query. Empty string means no search applied.
     */
    val currentSearchQuery: Value<String>

    fun setCollection(id: String)
    fun setSearchQuery(id: String)
    fun onGameClicked(id: String)
}