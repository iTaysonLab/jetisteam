package bruhcollective.itaysonlab.cobalt.compose

import androidx.compose.runtime.staticCompositionLocalOf
import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient

val LocalSteamClient = staticCompositionLocalOf<SteamClient> { error("Install a SteamClient instance to get started!") }