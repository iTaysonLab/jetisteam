package bruhcollective.itaysonlab.microapp.guard.ui.bottomsheet

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.uikit.components.BottomSheetLayout
import bruhcollective.itaysonlab.jetisteam.uikit.components.ResizableCircularIndicator
import bruhcollective.itaysonlab.jetisteam.usecases.twofactor.RemoveSg
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments
import bruhcollective.itaysonlab.microapp.guard.GuardMicroapp
import bruhcollective.itaysonlab.microapp.guard.R
import bruhcollective.itaysonlab.microapp.guard.core.GuardController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
internal fun GuardRemoveSheet(
    viewModel: GuardRemoveSheetViewModel = hiltViewModel(),
    onGuardRemoved: () -> Unit,
    onGuardRemovalCancelled: () -> Unit
) {
    BottomSheetLayout(
        title = {
            stringResource(id = R.string.guard_remove_sheet_header)
        }, subtitle = {
            buildAnnotatedString {
                append(stringResource(id = R.string.guard_for))
                append(" ")
                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                    append(viewModel.guardInstance.username)
                }
            }
        }
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            ), modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.guard_remove_sheet_text),
                modifier = Modifier.padding(16.dp)
            )
        }

        Text(
            text = stringResource(id = R.string.guard_remove_sheet_warn),
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Row(Modifier.padding(horizontal = 16.dp)) {
            Button(
                onClick = onGuardRemovalCancelled,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                colors = ButtonDefaults.filledTonalButtonColors(),
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    text = stringResource(id = R.string.guard_remove_sheet_action_cancel),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { viewModel.launchRemoval(onGuardRemoved) },
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                shape = MaterialTheme.shapes.large
            ) {
                if (viewModel.isRemoving) {
                    ResizableCircularIndicator(
                        indicatorSize = 19.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.guard_remove_sheet_action_remove),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@HiltViewModel
internal class GuardRemoveSheetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val guardController: GuardController,
    private val removeSg: RemoveSg
) : ViewModel() {
    val steamId = savedStateHandle.getSteamId()
    val guardInstance = guardController.getInstance(steamId)!!

    var isRemoving by mutableStateOf(false)
        private set

    fun launchRemoval(onSuccess: () -> Unit) {
        if (isRemoving) return

        viewModelScope.launch {
            isRemoving = true

            if (removeSg(guardInstance.revocationCode, 1, 1).success == true) {
                guardController.deleteInstance(steamId)
                onSuccess()
            }

            isRemoving = false
        }
    }
}