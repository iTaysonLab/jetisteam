package bruhcollective.itaysonlab.cobalt.library.games.alert

import bruhcollective.itaysonlab.ksteam.models.library.LibraryCollection
import com.arkivanov.decompose.value.Value
import kotlinx.collections.immutable.ImmutableList

interface SelectCollectionComponent {
    /**
     * Currently available CUSTOM collections.
     */
    val collections: Value<ImmutableList<LibraryCollection>>

    /**
     *
     */
    val currentCollectionId: String

    fun onCollectionClicked(collection: LibraryCollection)
    fun onFavoritesClicked()
    fun onHiddenClicked()
    fun onAllClicked()
}