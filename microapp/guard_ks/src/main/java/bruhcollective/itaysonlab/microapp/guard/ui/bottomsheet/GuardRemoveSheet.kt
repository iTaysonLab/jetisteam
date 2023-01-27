package bruhcollective.itaysonlab.microapp.guard.ui.bottomsheet

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import bruhcollective.itaysonlab.jetisteam.uikit.components.BottomSheetLayout
import bruhcollective.itaysonlab.jetisteam.uikit.components.ResizableCircularIndicator
import bruhcollective.itaysonlab.ksteam.handlers.guard
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.guard.R
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
    private val steamClient: HostSteamClient,
) : ViewModel() {
    private val steamId = savedStateHandle.getSteamId()

    val guardInstance = steamClient.client.guard.instanceFor(steamId)!!

    var isRemoving by mutableStateOf(false)
        private set

    fun launchRemoval(onSuccess: () -> Unit) {
        if (isRemoving) return

        viewModelScope.launch {
            isRemoving = true

            steamClient.client.guard.delete(steamId, unsafe = true)
            onSuccess()

            isRemoving = false
        }
    }
}