package bruhcollective.itaysonlab.microapp.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.QrCode
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.compositeOver
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
            Column(
                Modifier
                    .statusBarsPadding()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.new_onboarding_title),
                    style = MaterialTheme.typography.labelMedium
                )

                Text(
                    text = stringResource(id = R.string.new_onboarding_desc),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }, bottomBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding(), horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onSignClicked,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(imageVector = Icons.Rounded.Key, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(id = R.string.new_onboarding_sign))
                }

                FilledTonalButton(
                    onClick = onQrClicked,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(imageVector = Icons.Rounded.QrCode, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(id = R.string.new_onboarding_qrcode))
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                OnboardingCard(
                    icon = {
                        Icon(imageVector = Icons.Rounded.Palette, contentDescription = null)
                    },
                    title = "Stylish",
                    text = "Familiar features, packed in a full-blown re-imagination inspired by Material You guidelines"
                )
            }

            item {
                OnboardingCard(
                    icon = {
                        Icon(imageVector = Icons.Rounded.Security, contentDescription = null)
                    },
                    title = "Secure",
                    text = "Always open-source without any data collection - all processing happens on the device"
                )
            }

            item {
                OnboardingCard(
                    icon = {
                        Icon(imageVector = Icons.Rounded.Speed, contentDescription = null)
                    },
                    title = "Fast",
                    text = "A native, mobile-first experience built with the latest Android technology stack"
                )
            }
        }
    }
}

@Composable
private fun OnboardingCard(
    icon: @Composable () -> Unit,
    title: String,
    text: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        Surface(
            tonalElevation = 16.dp,
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                .compositeOver(MaterialTheme.colorScheme.surfaceVariant),
            contentColor = MaterialTheme.colorScheme.primary,
        ) {
            Box(Modifier.padding(12.dp)) {
                icon()
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}