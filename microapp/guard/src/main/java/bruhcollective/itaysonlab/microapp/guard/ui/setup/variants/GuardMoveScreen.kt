package bruhcollective.itaysonlab.microapp.guard.ui.setup.variants

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Update
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.proto.GuardData
import bruhcollective.itaysonlab.jetisteam.uikit.page.FullscreenError
import bruhcollective.itaysonlab.jetisteam.uikit.page.FullscreenLoading
import bruhcollective.itaysonlab.jetisteam.usecases.twofactor.MoveTfaAfterSms
import bruhcollective.itaysonlab.jetisteam.usecases.twofactor.MoveTfaRequestSms
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments
import bruhcollective.itaysonlab.microapp.guard.GuardMicroapp
import bruhcollective.itaysonlab.microapp.guard.core.GuardController
import bruhcollective.itaysonlab.microapp.guard.ui.components.CodeRowState
import bruhcollective.itaysonlab.microapp.guard.ui.setup.GuardSetupScreenImpl
import bruhcollective.itaysonlab.microapp.guard.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
internal fun GuardMoveScreen (
    viewModel: GuardMoveViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onSuccess: (steamId: SteamID) -> Unit,
) {
    when (val state = viewModel.smsState) {
        GuardMoveViewModel.SmsState.ReadyToEnter -> {
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
                title = stringResource(id = R.string.guard_setup_move_text),
                hint = stringResource(id = R.string.guard_setup_enter_code)
            )
        }

        GuardMoveViewModel.SmsState.Awaiting -> FullscreenLoading()
        is GuardMoveViewModel.SmsState.Error -> FullscreenError(onReload = {}, exception = state.exception)
    }
}

@HiltViewModel
internal class GuardMoveViewModel @Inject constructor(
    private val guardController: GuardController,
    private val moveTfaRequestSms: MoveTfaRequestSms,
    private val moveTfaAfterSms: MoveTfaAfterSms,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var smsState by mutableStateOf<SmsState>(SmsState.Awaiting)
        private set

    val steamId = savedStateHandle.getSteamId()

    init {
        viewModelScope.launch {
            smsState = try {
                moveTfaRequestSms()
                SmsState.ReadyToEnter
            } catch (e: Exception) {
                SmsState.Error(e)
            }
        }
    }

    suspend fun continueWithCode(code: String): Boolean {
        val result = moveTfaAfterSms(code)

        if (result.success == true) {
            val data = result.replacement_token!!

            guardController.createInstance(
                steamId = steamId, configuration = GuardData(
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
            )
        }

        return result.success == true
    }

    sealed class SmsState {
        object ReadyToEnter : SmsState()
        object Awaiting : SmsState()
        class Error(val exception: Exception) : SmsState()
    }
}