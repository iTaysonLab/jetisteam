package bruhcollective.itaysonlab.microapp.profile.ui.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.jetisteam.uikit.components.BottomSheetHandle
import bruhcollective.itaysonlab.microapp.profile.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalAppBottomSheet(
    onBackClicked: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    Column(Modifier.fillMaxWidth()) {
        BottomSheetHandle(modifier = Modifier.align(Alignment.CenterHorizontally))

        ListItem(
            leadingContent = {
                Icon(imageVector = Icons.Rounded.Settings, contentDescription = null)
            }, headlineText = {
                Text(text = stringResource(id = R.string.global_sheet_options))
            }, modifier = Modifier
                .alpha(0.7f)
                .clickable(enabled = false, onClick = { }), colors = ListItemDefaults.colors(
                leadingIconColor = MaterialTheme.colorScheme.primary
            )
        )

        Divider(color = MaterialTheme.colorScheme.surfaceVariant)

        ListItem(
            leadingContent = {
                Icon(imageVector = Icons.Rounded.Logout, contentDescription = null)
            }, headlineText = {
                Text(text = stringResource(id = R.string.global_sheet_sign_out))
            }, modifier = Modifier
                .alpha(0.7f)
                .clickable(enabled = false, onClick = { }), colors = ListItemDefaults.colors(
                leadingIconColor = MaterialTheme.colorScheme.primary
            )
        )

        Column(
            Modifier
                .fillMaxWidth()
                .background(
                    Color.Black
                        .copy(alpha = 0.5f)
                        .compositeOver(MaterialTheme.colorScheme.background)
                )
                .navigationBarsPadding()
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.global_sheet_about, "1.9", "1"),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Text(
                text = stringResource(id = R.string.global_sheet_about_sub),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(Modifier.padding(horizontal = 4.dp)) {
                IconButton(onClick = {
                    uriHandler.openUri("https://t.me/bruhcollective")
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Language,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = {
                    uriHandler.openUri("https://github.com/itaysonlab/jetisteam")
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Code,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier)
    }
}