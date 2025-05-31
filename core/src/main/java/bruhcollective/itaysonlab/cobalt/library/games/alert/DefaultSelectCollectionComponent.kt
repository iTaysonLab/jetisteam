package bruhcollective.itaysonlab.cobalt.library.games.alert

import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.handlers.library.Library
import bruhcollective.itaysonlab.ksteam.models.library.LibraryCollection
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class DefaultSelectCollectionComponent (
    override val currentCollectionId: String,
    private val onCollectionSelected: (newId: String, newName: String) -> Unit,
    componentContext: ComponentContext
): SelectCollectionComponent, ComponentContext by componentContext, KoinComponent, CoroutineScope by componentContext.coroutineScope() {
    override val collections = MutableValue(persistentListOf<LibraryCollection>())

    init {
        doOnCreate {
            launch {
                get<SteamClient>().ksteam.library.userCollections.collect { newCollectionState ->
                    collections.value = newCollectionState.values.toPersistentList()
                }
            }
        }
    }

    override fun onCollectionClicked(collection: LibraryCollection) {
        onCollectionSelected(collection.id, collection.name)
    }

    override fun onFavoritesClicked() {
        onCollectionSelected(Library.FavoriteCollection, "")
    }

    override fun onHiddenClicked() {
        onCollectionSelected(Library.HiddenCollection, "")
    }

    override fun onAllClicked() {
        onCollectionSelected("", "")
    }
}