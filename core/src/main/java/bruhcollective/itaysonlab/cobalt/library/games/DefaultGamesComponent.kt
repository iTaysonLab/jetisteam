package bruhcollective.itaysonlab.cobalt.library.games

import bruhcollective.itaysonlab.cobalt.core.commons.CobaltScreenResult
import bruhcollective.itaysonlab.ksteam.ExtendedSteamClient
import bruhcollective.itaysonlab.ksteam.models.app.OwnedSteamApplication
import bruhcollective.itaysonlab.ksteam.models.enums.EAppType
import bruhcollective.itaysonlab.ksteam.models.library.LibraryCollection
import bruhcollective.itaysonlab.ksteam.models.library.query.KsLibraryQueryBuilder
import bruhcollective.itaysonlab.ksteam.models.library.query.KsLibraryQueryOwnerFilter
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.coroutines.withLifecycle
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnResume
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class DefaultGamesComponent (
    componentContext: ComponentContext
): GamesComponent, KoinComponent, ComponentContext by componentContext {
    private val scope = coroutineScope()
    private var libraryPollingJob: Job? = null

    private val steamClient: ExtendedSteamClient = get()

    override val screenResult = MutableValue<CobaltScreenResult>(CobaltScreenResult.Loading)
    override val collections = MutableValue<ImmutableList<LibraryCollection>>(persistentListOf())
    override val games = MutableValue<ImmutableList<OwnedSteamApplication>>(persistentListOf())
    override val currentCollectionId = MutableValue<String>("")
    override val currentCollectionName = MutableValue<String>("")
    override val currentSearchQuery = MutableValue<String>("")

    override val scrollToTopFlag = MutableValue<Boolean>(false)

    override fun scrollToTop() {
        scrollToTopFlag.value = true
    }

    override fun resetScrollToTop() {
        scrollToTopFlag.value = false
    }

    init {
        doOnCreate {
            dispatchLibraryQuery()
        }

        doOnResume {
            scope.launch {
                steamClient.library.userCollections.collect {
                    collections.value = it.values.toImmutableList()
                }
            }
        }
    }

    /**
     * Creates a polling request.
     */
    private fun dispatchLibraryQuery() {
        libraryPollingJob?.cancel()
        libraryPollingJob = scope.launch {
            if (currentCollectionId.value.isNotEmpty()) {
                steamClient.library.getAppsInCollection(id = currentCollectionId.value, limit = 0)
                    .withLifecycle(lifecycle, minActiveState = Lifecycle.State.RESUMED)
                    .collect { apps ->
                        screenResult.value = CobaltScreenResult.Loaded
                        games.value = apps.toImmutableList()
                    }
            } else {
                games.value = steamClient.library.execute(KsLibraryQueryBuilder()
                    .withAppType(EAppType.Game)
                    .withOwnerFilter(KsLibraryQueryOwnerFilter.Default)
                    .build()).toImmutableList()
                screenResult.value = CobaltScreenResult.Loaded
            }
        }
    }

    override fun setCollection(collection: LibraryCollection) {
        currentCollectionId.value = collection.id
        currentCollectionName.value = collection.name
        dispatchLibraryQuery()
    }
}