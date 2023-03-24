package bruhcollective.itaysonlab.microapp.auth.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.components.BottomSheetLayout
import coil.compose.AsyncImage

@Composable
fun QrCodeBottomsheet(
    viewModel: QrCodeBottomsheetViewModel = hiltViewModel()
) {
    BottomSheetLayout(
        title = {
            "Sign in with QR"
        }
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterHorizontally)
                .padding(16.dp), shape = MaterialTheme.shapes.medium
        ) {
            when (val state = viewModel.qrCodeState) {
                is QrCodeBottomsheetViewModel.QrCodeState.Ready -> {
                    val qrCodeBackground = MaterialTheme.colorScheme.surfaceVariant
                    val qrCodeColor = MaterialTheme.colorScheme.secondary

                    AsyncImage(
                        model = remember(state) {
                            state.graphicsToRender.render(
                                margin = 75,
                                brightColor = qrCodeBackground.toArgb(),
                                marginColor = qrCodeBackground.toArgb(),
                                darkColor = qrCodeColor.toArgb()
                            ).nativeImage()
                        },
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                else -> {
                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }

        Text(
            text = "Scan this QR code from another device running any client that supports QR scanning.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        FilledTonalButton(
            onClick = {

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentPadding = PaddingValues(16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(imageVector = Icons.Rounded.Cancel, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Cancel")
        }
    }
}