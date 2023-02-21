package bruhcollective.itaysonlab.microapp.profile.ui.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.components.BottomSheetHandle
import bruhcollective.itaysonlab.microapp.core.LocalApplicationInfo
import bruhcollective.itaysonlab.microapp.profile.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalAppBottomSheet(
    viewModel: GlobalAppBottomSheetViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onEditProfileClicked: (Long) -> Unit,
    onSteamWrappedClicked: (Long) -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)
    ) {
        BottomSheetHandle(modifier = Modifier.align(Alignment.CenterHorizontally))

        Card(
            modifier = Modifier.padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
        ) {
            ListItem(
                leadingContent = {
                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
                }, headlineText = {
                    Text(text = stringResource(id = R.string.edit_title))
                }, modifier = Modifier.clickable(onClick = {
                    onEditProfileClicked(viewModel.steamId.longId)
                }), colors = ListItemDefaults.colors(
                    leadingIconColor = MaterialTheme.colorScheme.primary,
                    containerColor = Color.Transparent,
                )
            )

            Divider(color = MaterialTheme.colorScheme.surfaceVariant)

            ListItem(
                leadingContent = {
                    Icon(imageVector = Icons.Rounded.CalendarMonth, contentDescription = null)
                }, headlineText = {
                    Text(text = "Steam Replay")
                }, modifier = Modifier.clickable(onClick = {
                    onSteamWrappedClicked(viewModel.steamId.longId)
                }), colors = ListItemDefaults.colors(
                    leadingIconColor = MaterialTheme.colorScheme.primary,
                    containerColor = Color.Transparent,
                )
            )
        }

        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
        ) {
            ListItem(
                leadingContent = {
                    Icon(imageVector = Icons.Rounded.Settings, contentDescription = null)
                }, headlineText = {
                    Text(text = stringResource(id = R.string.global_sheet_options))
                }, modifier = Modifier
                    .alpha(0.7f)
                    .clickable(enabled = false, onClick = { }), colors = ListItemDefaults.colors(
                    leadingIconColor = MaterialTheme.colorScheme.primary,
                    containerColor = Color.Transparent,
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
                    leadingIconColor = MaterialTheme.colorScheme.primary,
                    containerColor = Color.Transparent,
                )
            )
        }

        Spacer(Modifier.height(16.dp))

        Column(
            Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.global_sheet_about, LocalApplicationInfo.current.versionNumber, LocalApplicationInfo.current.versionCode),
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