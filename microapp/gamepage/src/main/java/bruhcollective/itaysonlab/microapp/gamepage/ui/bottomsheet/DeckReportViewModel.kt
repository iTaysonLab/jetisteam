package bruhcollective.itaysonlab.microapp.gamepage.ui.bottomsheet

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import bruhcollective.itaysonlab.jetisteam.controllers.LocaleService
import bruhcollective.itaysonlab.jetisteam.models.SteamDeckSupport
import bruhcollective.itaysonlab.jetisteam.models.SteamDeckSupportReport
import bruhcollective.itaysonlab.jetisteam.models.SteamDeckTestResult
import bruhcollective.itaysonlab.jetisteam.repository.GameRepository
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.microapp.gamepage.GamePageMicroapp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeckReportViewModel @Inject constructor(
    private val localeService: LocaleService,
    private val gameRepository: GameRepository,
    savedState: SavedStateHandle
) : PageViewModel<DeckReportViewModel.Model>() {
    private val appId = savedState.get<Int>(GamePageMicroapp.Arguments.GameId.name) ?: 0

    init { reload() }

    override suspend fun load(): Model {
        return gameRepository.getDeckCompat(appId = appId.toString()).let { report ->
            val header = localeService.dynamicLocaleForAwait(
                "SteamDeckVerified_DescriptionHeader_" + when (report.category) {
                    SteamDeckSupport.Unknown -> "Unknown"
                    SteamDeckSupport.Unsupported -> "Unsupported"
                    SteamDeckSupport.Playable -> "Playable"
                    SteamDeckSupport.Verified -> "Verified"
                }
            )

            Model(
                report = report,
                items = report.resolvedItems.map { it.displayType to localeService.dynamicLocaleFor(it.stringRef) },
                headerString = header
            )
        }
    }

    class Model(
        val report: SteamDeckSupportReport,
        val items: List<Pair<SteamDeckTestResult, String>>,
        val headerString: String
    )

    companion object {
        val SteamColorVerified = Color(
            0xFF59BF40
        )

        val SteamColorPlayable = Color(
            0xFFFFC82C
        )
    }
}