package bruhcollective.itaysonlab.cobalt.library.screenshots

import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.handlers.PublishedFiles
import bruhcollective.itaysonlab.ksteam.models.AppId
import bruhcollective.itaysonlab.ksteam.models.enums.EPublishedFileInfoMatchingFileType
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnResume
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class DefaultSelectApplicationSheetComponent (
    private val browseFilter: PublishedFiles.PersonalBrowseFilter,
    private val onApplicationSelected: (ScreenshotsComponent.PickedApplication) -> Unit,
    componentContext: ComponentContext
): SelectApplicationSheetComponent, ComponentContext by componentContext, KoinComponent, CoroutineScope by componentContext.coroutineScope() {
    override val isLoading = MutableValue(true)
    override val applications = MutableValue<ImmutableList<ScreenshotsComponent.PickedApplication>>(persistentListOf())

    init {
        doOnResume(isOneTime = true) {
            launch {
                val steamClient = get<SteamClient>().ksteam

                runCatching {
                    applications.value = steamClient.publishedFiles.getFiles(
                        steamId = steamClient.currentSessionSteamId,
                        fileType = EPublishedFileInfoMatchingFileType.Screenshots,
                        count = 1,
                        browseFilter = browseFilter,
                        returnApps = true
                    ).apps.map {
                            app -> ScreenshotsComponent.PickedApplication(app.id.value, app.name)
                    }.asReversed().toImmutableList()
                }

                isLoading.value = false
            }
        }
    }

    override fun onApplicationClicked(app: ScreenshotsComponent.PickedApplication) {
        onApplicationSelected.invoke(app)
    }

    override fun onClearFilterClicked() {
        onApplicationSelected.invoke(ScreenshotsComponent.PickedApplication())
    }
}