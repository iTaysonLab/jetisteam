package bruhcollective.itaysonlab.microapp.guard.ui.session_detail

import android.content.Context
import android.text.format.DateUtils
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Router
import androidx.compose.material.icons.rounded.TextFormat
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import bruhcollective.itaysonlab.jetisteam.ext.ipString
import bruhcollective.itaysonlab.ksteam.guard.models.ActiveSession
import bruhcollective.itaysonlab.ksteam.handlers.guardManagement
import bruhcollective.itaysonlab.microapp.core.ext.getProto
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.guard.GuardMicroapp
import bruhcollective.itaysonlab.microapp.guard.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import steam.webui.authentication.EAuthSessionGuardType
import javax.inject.Inject

@HiltViewModel
internal class GuardSessionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext context: Context,
    private val steamClient: HostSteamClient
) : ViewModel() {
    val steamId = savedStateHandle.getSteamId()
    val sessionData = ActiveSession(savedStateHandle.getProto(GuardMicroapp.Arguments.SessionData.name))

    val infoBlocks = buildListBlocks(context)
    var revokingSession by mutableStateOf(false)

    fun requestRevokeSession(onDone: () -> Unit) {
        revokingSession = true

        viewModelScope.launch {
            steamClient.client.guardManagement.revokeSession(sessionData.id)
            revokingSession = false
            onDone()
        }
    }

    private fun buildListBlocks(ctx: Context) = buildList {
        add(
            ListItem(
                Triple(
                    { Icons.Rounded.TextFormat },
                    R.string.guard_session_info_desc,
                    sessionData.deviceName.ifEmpty { "Unknown" }
                )
            )
        )

        add(
            ListItem(
                Triple(
                    { Icons.Rounded.Timer },
                    R.string.guard_session_info_first,
                    DateUtils.getRelativeTimeSpanString(
                        ctx,
                        sessionData.firstSeen?.time?.toLong()?.times(1000L) ?: 0L
                    ).toString()
                )
            )
        )

        if (!sessionData.lastSeen?.city.isNullOrEmpty()) {
            add(
                ListItem(
                    Triple(
                        { Icons.Rounded.LocationOn },
                        R.string.guard_session_info_loc,
                        "${sessionData.lastSeen?.city}, ${sessionData.lastSeen?.state}, ${sessionData.lastSeen?.country}"
                    )
                )
            )
        }

        add(
            ListItem(
                Triple(
                    { Icons.Rounded.Key },
                    R.string.guard_session_info_signed,
                    when (sessionData.confirmedWith) {
                        EAuthSessionGuardType.k_EAuthSessionGuardType_None -> "None"
                        EAuthSessionGuardType.k_EAuthSessionGuardType_EmailCode -> "Email (code)"
                        EAuthSessionGuardType.k_EAuthSessionGuardType_DeviceCode -> "Steam Guard (code)"
                        EAuthSessionGuardType.k_EAuthSessionGuardType_DeviceConfirmation -> "Steam Guard (mobile)"
                        EAuthSessionGuardType.k_EAuthSessionGuardType_EmailConfirmation -> "Email (confirmation)"
                        EAuthSessionGuardType.k_EAuthSessionGuardType_MachineToken -> "Machine token"
                        else -> "Unknown"
                    }
                )
            )
        )

        if (sessionData.lastSeen?.ip != null) {
            add(
                ListItem(
                    Triple(
                        { Icons.Rounded.Router },
                        R.string.guard_session_info_ip,
                        sessionData.lastSeen?.ip?.ipString ?: "Unknown"
                    )
                )
            )
        }
    }

    @JvmInline
    value class ListItem(private val packed: Triple<() -> ImageVector, Int, String>) {
        val icon: () -> ImageVector get() = packed.first
        val titleRes: Int get() = packed.second
        val text: String get() = packed.third
    }
}