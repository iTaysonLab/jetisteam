package bruhcollective.itaysonlab.microapp.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.microapp.auth.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onSignClicked: () -> Unit,
    onQrClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = stringResource(id = R.string.new_onboarding_title), style = MaterialTheme.typography.labelMedium)
                        Text(text = stringResource(id = R.string.new_onboarding_desc), fontWeight = FontWeight.SemiBold)
                    }
                }
            )
        }, bottomBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = onSignClicked, modifier = Modifier.weight(1f), contentPadding = PaddingValues(16.dp), shape = MaterialTheme.shapes.medium) {
                    Icon(imageVector = Icons.Rounded.Key, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(id = R.string.new_onboarding_sign))
                }

                FilledTonalButton(onClick = onQrClicked, modifier = Modifier.weight(1f), contentPadding = PaddingValues(16.dp), shape = MaterialTheme.shapes.medium) {
                    Icon(imageVector = Icons.Rounded.QrCode, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(id = R.string.new_onboarding_qrcode))
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            // TODO: intro
        }
    }
}