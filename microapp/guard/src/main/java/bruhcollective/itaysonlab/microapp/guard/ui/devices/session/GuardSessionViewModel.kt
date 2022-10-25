package bruhcollective.itaysonlab.microapp.guard.ui.devices.session

import android.content.Context
import android.text.format.DateUtils
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import bruhcollective.itaysonlab.jetisteam.ext.ipString
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.microapp.guard.GuardMicroappImpl
import bruhcollective.itaysonlab.microapp.guard.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import okio.ByteString.Companion.decodeBase64
import steam.auth.CAuthentication_RefreshToken_Enumerate_Response
import steam.auth.EAuthSessionGuardType
import javax.inject.Inject

@HiltViewModel
internal class GuardSessionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext context: Context
) : ViewModel() {
    val steamId = SteamID(
        savedStateHandle.get<String>(GuardMicroappImpl.InternalRoutes.ARG_STEAM_ID)!!.toLong()
    )

    val sessionData =
        CAuthentication_RefreshToken_Enumerate_Response.RefreshTokenDescription.ADAPTER.decode(
            savedStateHandle.get<String>(GuardMicroappImpl.InternalRoutes.ARG_SESSION_DATA)!!
                .decodeBase64()!!
        )

    val infoBlocks = buildListBlocks(context)

    private fun buildListBlocks(ctx: Context) = buildList {
        add(
            ListItem(Triple(
                { Icons.Rounded.TextFormat }, R.string.guard_session_info_desc, sessionData.token_description ?: "Unknown"
            ))
        )

        add(
            ListItem(Triple(
                { Icons.Rounded.Timer }, R.string.guard_session_info_first, DateUtils.getRelativeTimeSpanString(ctx, sessionData.first_seen?.time?.toLong()?.times(1000L) ?: 0L).toString()
            ))
        )

        add(
            ListItem(Triple(
                { Icons.Rounded.Key }, R.string.guard_session_info_signed, when (sessionData.auth_type) {
                    EAuthSessionGuardType.k_EAuthSessionGuardType_None -> "None"
                    EAuthSessionGuardType.k_EAuthSessionGuardType_EmailCode -> "Email (code)"
                    EAuthSessionGuardType.k_EAuthSessionGuardType_DeviceCode -> "Steam Guard (code)"
                    EAuthSessionGuardType.k_EAuthSessionGuardType_DeviceConfirmation -> "Steam Guard (mobile)"
                    EAuthSessionGuardType.k_EAuthSessionGuardType_EmailConfirmation -> "Email (confirmation)"
                    EAuthSessionGuardType.k_EAuthSessionGuardType_MachineToken -> "Machine token"
                    else -> "Unknown"
                }
            ))
        )

        add(
            ListItem(Triple(
                { Icons.Rounded.Router }, R.string.guard_session_info_ip, sessionData.last_seen?.ip?.ipString ?: "Unknown"
            ))
        )
    }

    @JvmInline
    value class ListItem(private val packed: Triple<() -> ImageVector, Int, String>) {
        val icon: () -> ImageVector get() = packed.first
        val titleRes: Int get() = packed.second
        val text: String get() = packed.third
    }
}