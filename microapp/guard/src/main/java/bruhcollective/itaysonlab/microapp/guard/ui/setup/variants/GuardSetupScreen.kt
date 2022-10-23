package bruhcollective.itaysonlab.microapp.guard.ui.setup.variants

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Update
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.proto.GuardData
import bruhcollective.itaysonlab.jetisteam.usecases.twofactor.FinalizeAddTfa
import bruhcollective.itaysonlab.microapp.guard.GuardMicroappImpl
import bruhcollective.itaysonlab.microapp.guard.R
import bruhcollective.itaysonlab.microapp.guard.core.GuardController
import bruhcollective.itaysonlab.microapp.guard.ui.components.CodeRowState
import bruhcollective.itaysonlab.microapp.guard.ui.setup.GuardSetupScreenImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.ByteString.Companion.decodeBase64
import steam.twofactor.CTwoFactor_AddAuthenticator_Response
import javax.inject.Inject

@Composable
internal fun GuardSetupScreen (
    viewModel: GuardSetupViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onSuccess: (steamId: SteamID) -> Unit,
) {
    val scope = rememberCoroutineScope()

    val rowState = remember {
        CodeRowState(5, onFinish = { crs, code ->
            scope.launch {
                crs.setInactiveState(true)

                if (viewModel.continueWithCode(code)) {
                    onSuccess(viewModel.steamId)
                } else {
                    crs.setErrorState(true)
                }

                crs.setInactiveState(false)
            }
        })
    }

    GuardSetupScreenImpl(
        onBackClicked = onBackClicked,
        state = rowState,
        icon = { Icons.Rounded.Update },
        title = stringResource(id = R.string.guard_setup_enter_code_action),
        hint = stringResource(id = R.string.guard_setup_enter_code)
    )
}

@HiltViewModel
internal class GuardSetupViewModel @Inject constructor(
    private val guardController: GuardController,
    private val finalizeAddTfa: FinalizeAddTfa,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val steamId = SteamID(savedStateHandle.get<String>(GuardMicroappImpl.InternalRoutes.ARG_STEAM_ID)!!.toLong())

    private val localGuardData = CTwoFactor_AddAuthenticator_Response.ADAPTER.decode(savedStateHandle.get<String>(GuardMicroappImpl.InternalRoutes.ARG_GC_DATA)!!.decodeBase64()!!).let { data ->
        GuardData(
            shared_secret = data.shared_secret!!,
            serial_number = data.serial_number!!,
            revocation_code = data.revocation_code!!,
            uri = data.uri!!,
            server_time = data.server_time!!,
            account_name = data.account_name!!,
            token_gid = data.token_gid!!,
            identity_secret = data.identity_secret!!,
            secret_1 = data.secret_1!!,
            steam_id = steamId.steamId,
        )
    }

    private val localGenerator by lazy { guardController.createInstance(steamId = steamId, configuration = localGuardData, save = false) }

    suspend fun continueWithCode(code: String): Boolean {
        return finalizeAddTfa(steamId, code) {
            return@finalizeAddTfa localGenerator.generateCodeWithTime()
        }.ifTrue {
            guardController.saveInstance(steamId, localGenerator)
        }
    }

    private inline fun Boolean.ifTrue(func: () -> Unit) = apply {
        if (this) func()
    }
}