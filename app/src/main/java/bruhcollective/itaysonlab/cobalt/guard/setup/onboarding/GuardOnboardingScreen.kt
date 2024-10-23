package bruhcollective.itaysonlab.cobalt.guard.setup.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Security
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.GuardSetupOverrideExistingSheet
import bruhcollective.itaysonlab.cobalt.ui.components.StateButtonContent
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun GuardOnboardingScreen(
    component: GuardOnboardingComponent,
) {
    val isProcessing by component.isTryingToStartSetup.subscribeAsState()
    val alert by component.alertSlot.subscribeAsState()

    alert.child?.instance?.let {
        when (val child = it) {
            is GuardOnboardingComponent.AlertChild.OverrideExisting -> {
                GuardSetupOverrideExistingSheet(child.component)
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Icon(imageVector = Icons.Sharp.Security, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.guard_desc),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = component::onSetupClicked,
                contentPadding = PaddingValues(16.dp),
                enabled = isProcessing.not()
            ) {
                StateButtonContent(
                    inLoadingState = isProcessing
                ) {
                    Text(text = stringResource(id = R.string.guard_action))
                }
            }
        }
    }
}