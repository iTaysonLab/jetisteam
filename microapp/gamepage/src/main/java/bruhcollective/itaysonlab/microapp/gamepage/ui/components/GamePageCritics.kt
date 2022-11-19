package bruhcollective.itaysonlab.microapp.gamepage.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetisteam.models.*
import bruhcollective.itaysonlab.jetisteam.util.DateUtil
import bruhcollective.itaysonlab.microapp.gamepage.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GamePageCritics(
    deckReport: SteamDeckSupportReport,
    metaCritic: GameFullDetailsData.MetacriticLocator?,
    onDeckClicked: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    Column(
        Modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
            .fillMaxWidth()
    ) {
        ListItem(
            supportingText = {
                Text(text = when (deckReport.category) {
                    SteamDeckSupport.Unknown -> stringResource(id = R.string.gamepage_deckcompat_type_unknown)
                    SteamDeckSupport.Unsupported -> stringResource(id = R.string.gamepage_deckcompat_type_unsupported)
                    SteamDeckSupport.Playable -> stringResource(id = R.string.gamepage_deckcompat_type_playable)
                    SteamDeckSupport.Verified -> stringResource(id = R.string.gamepage_deckcompat_type_verified)
                }, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }, headlineText = {
                Text(
                    text = stringResource(id = R.string.gamepage_deckcompat_title),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp
                )
            }, leadingContent = {
                Icon(imageVector = when (deckReport.category) {
                    SteamDeckSupport.Unknown -> Icons.Rounded.Help
                    SteamDeckSupport.Unsupported -> Icons.Rounded.Cancel
                    SteamDeckSupport.Playable -> Icons.Rounded.Error
                    SteamDeckSupport.Verified -> Icons.Rounded.CheckCircle
                }, contentDescription = null)
            }, trailingContent = {
                if (deckReport.category != SteamDeckSupport.Unknown) {
                    Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = null)
                }
            }, colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
            ), modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = deckReport.category != SteamDeckSupport.Unknown, onClick = onDeckClicked)
        )

        if (metaCritic != null) {
            Divider(
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(16.dp)
            )

            ListItem(
                supportingText = {
                    Text(
                        text = metaCritic.score.toString(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }, headlineText = {
                    Text(
                        text = stringResource(id = R.string.gamepage_metacritic),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    )
                }, leadingContent = {
                    Icon(imageVector = Icons.Rounded.Stars, contentDescription = null)
                }, trailingContent = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box {
                            CircularProgressIndicator(
                                progress = 1f,
                                color = MaterialTheme.colorScheme.surfaceColorAtElevation(16.dp)
                            )

                            CircularProgressIndicator(progress = remember(metaCritic.score) {
                                metaCritic.score / 100f
                            }, color = remember(metaCritic.score) {
                                when {
                                    metaCritic.score >= 75 -> Color(0xFF66CC33)
                                    metaCritic.score >= 50 -> Color(0xFFFFCC33)
                                    else -> Color(0xFFFF0000)
                                }
                            })
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))

                        Icon(imageVector = Icons.Rounded.Launch, contentDescription = null)
                    }
                }, colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                ), modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        uriHandler.openUri(metaCritic.url)
                    }
            )
        }
    }
}