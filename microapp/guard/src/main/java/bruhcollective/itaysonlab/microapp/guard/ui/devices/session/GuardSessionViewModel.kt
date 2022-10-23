package bruhcollective.itaysonlab.microapp.guard.ui.devices.session

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.microapp.guard.GuardMicroappImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import okio.ByteString.Companion.decodeBase64
import steam.auth.CAuthentication_RefreshToken_Enumerate_Response
import steam.twofactor.CTwoFactor_AddAuthenticator_Response
import javax.inject.Inject

@HiltViewModel
class GuardSessionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val steamId = SteamID(
        savedStateHandle.get<String>(GuardMicroappImpl.InternalRoutes.ARG_STEAM_ID)!!.toLong()
    )

    val sessionData =
        CAuthentication_RefreshToken_Enumerate_Response.RefreshTokenDescription.ADAPTER.decode(
            savedStateHandle.get<String>(GuardMicroappImpl.InternalRoutes.ARG_SESSION_DATA)!!
                .decodeBase64()!!
        )

    val infoBlocks = buildListBlocks()

    private fun buildListBlocks() = buildList<ListItem> {

    }

    @JvmInline
    value class ListItem(private val packed: Triple<() -> ImageVector, Int, String>) {
        val icon: () -> ImageVector get() = packed.first
        val titleRes: Int get() = packed.second
        val text: String get() = packed.third
    }
}