package bruhcollective.itaysonlab.microapp.guard.ui.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import bruhcollective.itaysonlab.jetisteam.uikit.components.BottomSheetLayout
import bruhcollective.itaysonlab.ksteam.handlers.guard
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.guard.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Composable
internal fun GuardRecoveryCodeSheet(
    viewModel: GuardRecoveryCodeViewModel = hiltViewModel(),
    onExit: () -> Unit
) {
    BottomSheetLayout(
        title = {
            stringResource(id = R.string.guard_recovery)
        }, subtitle = {
            buildAnnotatedString {
                append(stringResource(id = R.string.guard_for))
                append(" ")
                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                    append(viewModel.username)
                }
            }
        }
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                    4.dp
                )
            ), modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = viewModel.recoveryCode,
                    textAlign = TextAlign.Center,
                    modifier = Modifier,
                    fontSize = 40.sp,
                    letterSpacing = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(id = R.string.guard_recovery_hint),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.alpha(0.7f),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Text(
            text = stringResource(id = R.string.guard_recovery_desc),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        FilledTonalButton(
            onClick = onExit,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            contentPadding = PaddingValues(16.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Text(
                text = stringResource(id = R.string.guard_remove_sheet_action_close),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@HiltViewModel
internal class GuardRecoveryCodeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    steamClient: HostSteamClient,
) : ViewModel() {
    private val steamId = savedStateHandle.getSteamId()
    private val guardInstance = steamClient.client.guard.instanceFor(steamId)!!

    val username = guardInstance.username
    val recoveryCode = guardInstance.revocationCode
}