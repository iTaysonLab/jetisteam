package bruhcollective.itaysonlab.cobalt.library.games

import bruhcollective.itaysonlab.cobalt.core.commons.CobaltScreenResult
import bruhcollective.itaysonlab.cobalt.sheets.DefaultOwnedGameSheetComponent
import bruhcollective.itaysonlab.cobalt.library.games.alert.DefaultSelectCollectionComponent
import bruhcollective.itaysonlab.ksteam.ExtendedSteamClient
import bruhcollective.itaysonlab.ksteam.models.app.OwnedSteamApplication
import bruhcollective.itaysonlab.ksteam.models.enums.ECollectionAppType
import bruhcollective.itaysonlab.ksteam.models.library.query.KsLibraryQueryBuilder
import bruhcollective.itaysonlab.ksteam.models.library.query.KsLibraryQueryOwnerFilter
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.coroutines.withLifecycle
import com.arkivanov.essenty.lifecycle.doOnCreate
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class DefaultGamesComponent(
    private val onAppAchievementsClicked: (Int) -> Unit,
    componentContext: ComponentContext
) : GamesComponent, KoinComponent, ComponentContext by componentContext,
    CoroutineScope by componentContext.coroutineScope() {
    private companion object {
        private const val PAGE_LOAD_COUNT = 25
    }

    private val steamClient: ExtendedSteamClient = get()

    private var currentCollectionId = ""
    private var libraryPollingJob: Job? = null

    private val alertNavigation = SlotNavigation<AlertConfig>()

    override val alertState = childSlot(
        source = alertNavigation,
        serializer = AlertConfig.serializer(),
        childFactory = ::createChild
    )

    override val screenResult = MutableValue<CobaltScreenResult>(CobaltScreenResult.Loading)
    override val picsState = MutableValue(false)
    override val picsProgress = MutableValue(0f)
    override val currentCollectionName = MutableValue("")
    override val wasDefaultQueryModified = MutableValue(false)
    override val games = MutableValue(persistentListOf<OwnedSteamApplication>())

    override val currentSearchQuery = MutableValue("")
    override fun setCurrentSearchQuery(value: String) {
        currentSearchQuery.value = value
    }

    init {
        doOnCreate {
            launch {
                steamClient.pics.picsInitializationProgress.withLifecycle(
                    lifecycle,
                    minActiveState = Lifecycle.State.RESUMED
                ).collect { progress ->
                    picsProgress.update { progress }
                }
            }

            launch {
                steamClient.pics.isPicsAvailable.withLifecycle(
                    lifecycle,
                    minActiveState = Lifecycle.State.RESUMED
                ).collect { available ->
                    picsState.update { available }

                    println("dispatchLibraryQuery: $available && ${screenResult.value}")

                    if (available && screenResult.value == CobaltScreenResult.Loading) {
                        dispatchLibraryQuery()
                    }
                }
            }
        }
    }

    /**
     * Creates a polling request.
     */
    private fun dispatchLibraryQuery() {
        libraryPollingJob?.cancel()
        libraryPollingJob = launch {
            if (currentCollectionId.isNotEmpty()) {
                steamClient.library.getAppsInCollection(
                    id = currentCollectionId,
                    full = true,
                    limit = 0
                ).withLifecycle(lifecycle, minActiveState = Lifecycle.State.RESUMED).collect { apps ->
                    screenResult.value = CobaltScreenResult.Loaded
                    games.value = apps.toPersistentList()
                }
            } else {
                requestGlobalLibraryPage()
                screenResult.value = CobaltScreenResult.Loaded
            }
        }
    }

    private var offset = 0

    private suspend fun requestGlobalLibraryPage() {
        val newApplications = steamClient.library.execute(
            KsLibraryQueryBuilder()
                .withAppType(ECollectionAppType.Game)
                .withOwnerFilter(KsLibraryQueryOwnerFilter.Default)
                .fetchFullInformation(true)
                .withLimit(PAGE_LOAD_COUNT)
                .withOffset(offset)
                .build()
        ).toImmutableList()

        offset += PAGE_LOAD_COUNT
        games.update { existing -> existing.addAll(newApplications) }

        canLoadMore.value = newApplications.size == PAGE_LOAD_COUNT
    }

    private fun setCollectionById(id: String, name: String) {
        wasDefaultQueryModified.value = id.isNotEmpty()
        offset = 0
        canLoadMore.value = false
        games.value = persistentListOf()
        currentCollectionId = id
        currentCollectionName.value = name
        dispatchLibraryQuery()
    }

    override val canLoadMore = MutableValue(false)

    override fun onPageRequested() {
        launch {
            requestGlobalLibraryPage()
        }
    }

    override fun onCollectionTileClicked() {
        alertNavigation.activate(AlertConfig.SelectCollection(selectedId = currentCollectionId))
    }

    override fun onFilterTileClicked() {
        alertNavigation.activate(AlertConfig.SelectSort())
    }

    override fun onSortTileClicked() {
        alertNavigation.activate(AlertConfig.EditCollection())
    }

    override fun onGameClicked(value: OwnedSteamApplication) {
        alertNavigation.activate(AlertConfig.GameSheet(id = value.application.id.value))
    }

    private fun createChild(config: AlertConfig, componentContext: ComponentContext): GamesComponent.AlertChild {
        return when (config) {
            is AlertConfig.EditCollection -> {
                TODO()
            }

            is AlertConfig.SelectCollection -> {
                GamesComponent.AlertChild.SelectCollection(
                    component = DefaultSelectCollectionComponent(
                        currentCollectionId = config.selectedId,
                        componentContext = componentContext,
                        onCollectionSelected = { id, name ->
                            setCollectionById(id, name)
                            alertNavigation.dismiss()
                        }
                    )
                )
            }

            is AlertConfig.SelectSort -> {
                TODO()
            }

            is AlertConfig.GameSheet -> {
                GamesComponent.AlertChild.GameSheet(
                    component = DefaultOwnedGameSheetComponent(
                        id = config.id,
                        componentContext = componentContext,
                        onAchievementsClicked = {
                            dismissAlert()
                            onAppAchievementsClicked(config.id)
                        },
                        onStorePageClicked = {
                            dismissAlert()
                        },
                        onRemoteInstallClicked = {
                            dismissAlert()
                        },
                        onGameNotesClicked = {
                            dismissAlert()
                        }
                    )
                )
            }
        }
    }

    override fun dismissAlert() {
        alertNavigation.dismiss()
    }

    @Serializable
    sealed interface AlertConfig {
        @Serializable
        @SerialName("select_collection")
        data class SelectCollection (
            val selectedId: String
        ): AlertConfig

        @Serializable
        @SerialName("select_sort")
        class SelectSort (

        ): AlertConfig

        @Serializable
        @SerialName("edit_collection")
        class EditCollection (

        ): AlertConfig

        @Serializable
        @SerialName("game_sheet")
        class GameSheet (
            val id: Int
        ): AlertConfig
    }
}