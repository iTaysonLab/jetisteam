package bruhcollective.itaysonlab.microapp.gamepage.ui.bottomsheet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.models.SteamDeckSupport
import bruhcollective.itaysonlab.jetisteam.models.SteamDeckTestResult
import bruhcollective.itaysonlab.jetisteam.uikit.components.BottomSheetLayout
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.microapp.gamepage.R

@Composable
fun DeckReportBottomSheet(
    viewModel: DeckReportViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    BottomSheetLayout(title = {
        stringResource(id = R.string.gamepage_deckcompat_title)
    }) {
        when (val state = viewModel.state) {
            is PageViewModel.State.Loaded -> {
                LazyColumn {
                    item {
                        Column(Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = when (state.data.report.category) {
                                    SteamDeckSupport.Unknown -> Icons.Rounded.Help
                                    SteamDeckSupport.Unsupported -> Icons.Rounded.Cancel
                                    SteamDeckSupport.Playable -> Icons.Rounded.Error
                                    SteamDeckSupport.Verified -> Icons.Rounded.CheckCircle
                                }, tint = when (state.data.report.category) {
                                    SteamDeckSupport.Playable -> DeckReportViewModel.SteamColorPlayable
                                    SteamDeckSupport.Verified -> DeckReportViewModel.SteamColorVerified
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                }, contentDescription = null, modifier = Modifier.size(32.dp))

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(text = when (state.data.report.category) {
                                    SteamDeckSupport.Unknown -> stringResource(id = R.string.gamepage_deckcompat_type_unknown)
                                    SteamDeckSupport.Unsupported -> stringResource(id = R.string.gamepage_deckcompat_type_unsupported)
                                    SteamDeckSupport.Playable -> stringResource(id = R.string.gamepage_deckcompat_type_playable)
                                    SteamDeckSupport.Verified -> stringResource(id = R.string.gamepage_deckcompat_type_verified)
                                }, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold, fontSize = 21.sp)
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(state.data.headerString, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Divider(color = MaterialTheme.colorScheme.outlineVariant)
                    }

                    itemsIndexed(state.data.items) { index, item ->
                        Column(modifier = Modifier.padding(16.dp)) {
                            Icon(imageVector = when (item.first) {
                                SteamDeckTestResult.Note -> Icons.Rounded.Help
                                SteamDeckTestResult.Failed -> Icons.Rounded.Cancel
                                SteamDeckTestResult.Info -> Icons.Rounded.Error
                                SteamDeckTestResult.Verified -> Icons.Rounded.CheckCircle
                            }, tint = when (item.first) {
                                SteamDeckTestResult.Info -> DeckReportViewModel.SteamColorPlayable
                                SteamDeckTestResult.Verified -> DeckReportViewModel.SteamColorVerified
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }, contentDescription = null)

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(item.second, color = MaterialTheme.colorScheme.onSurface)
                        }

                        if (index != state.data.items.lastIndex) {
                            Divider(color = MaterialTheme.colorScheme.outlineVariant)
                        }
                    }
                }
            }

            is PageViewModel.State.Loading -> {
                CircularProgressIndicator(modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp))
            }

            is PageViewModel.State.Error -> {
                LaunchedEffect(Unit) {
                    onBackClick()
                }
            }
        }
    }
}