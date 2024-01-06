package bruhcollective.itaysonlab.cobalt.core.ksteam

import bruhcollective.itaysonlab.cobalt.core.decompose.ViewModel
import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class SteamViewModel: ViewModel(), KoinComponent {
    val steam: SteamClient by inject()
}