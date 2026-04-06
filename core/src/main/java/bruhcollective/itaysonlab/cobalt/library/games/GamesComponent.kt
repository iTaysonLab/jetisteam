package bruhcollective.itaysonlab.cobalt.library.games

import bruhcollective.itaysonlab.cobalt.core.commons.CobaltScreenResult
import bruhcollective.itaysonlab.cobalt.library.games.alert.EditCollectionComponent
import bruhcollective.itaysonlab.cobalt.sheets.OwnedGameSheetComponent
import bruhcollective.itaysonlab.cobalt.library.games.alert.SelectCollectionComponent
import bruhcollective.itaysonlab.cobalt.library.games.alert.SelectSortComponent
import bruhcollective.itaysonlab.ksteam.models.app.OwnedSteamApplication
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import kotlinx.collections.immutable.ImmutableList

interface GamesComponent {
    /**
     * Alert state.
     */
    val alertState: Value<ChildSlot<*, AlertChild>>
    fun dismissAlert()

    /**
     * Page state.
     */
    val screenResult: Value<CobaltScreenResult>

    /**
     * PICS state.
     */
    val picsState: Value<Boolean>
    val picsProgress: Value<Float>

    /**
     * Filtered list of games.
     */
    val games: Value<ImmutableList<OwnedSteamApplication>>

    /**
     * Current collection name.
     */
    val currentCollectionName: Value<String>

    /**
     * If the KsLibraryQueue is not standard.
     *
     * Actually always true for everything out of "All Games", "Favorite" and "Hidden".
     * Also needs to be false for static collections.
     *
     * If this is true, "Filter" tile will be highlighted.
     */
    val wasDefaultQueryModified: Value<Boolean>

    /**
     * Search query. Empty string means no search applied.
     */
    val currentSearchQuery: Value<String>
    fun setCurrentSearchQuery(value: String)

    val canLoadMore: Value<Boolean>
    fun onPageRequested()

    fun onCollectionTileClicked()
    fun onFilterTileClicked()
    fun onSortTileClicked()
    fun onGameClicked(value: OwnedSteamApplication)

    sealed interface AlertChild {
        class EditCollection (
            val component: EditCollectionComponent
        ): AlertChild

        class SelectCollection (
            val component: SelectCollectionComponent
        ): AlertChild

        class SelectSort (
            val component: SelectSortComponent
        ): AlertChild

        class GameSheet (
            val component: OwnedGameSheetComponent
        ): AlertChild
    }
}