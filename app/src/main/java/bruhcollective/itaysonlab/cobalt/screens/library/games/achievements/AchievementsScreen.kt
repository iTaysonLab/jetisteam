package bruhcollective.itaysonlab.cobalt.screens.library.games.achievements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.core.commons.CobaltScreenResult
import bruhcollective.itaysonlab.cobalt.store_page.achievements.GameAchievementsComponent
import bruhcollective.itaysonlab.cobalt.ui.components.EmptyWindowInsets
import bruhcollective.itaysonlab.cobalt.ui.components.ExceptionPage
import bruhcollective.itaysonlab.cobalt.ui.components.M3ECardListItem
import bruhcollective.itaysonlab.cobalt.ui.components.M3ECardListItemShapes
import bruhcollective.itaysonlab.cobalt.ui.components.RoundedPage
import coil.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun AchievementsScreen(
    component: GameAchievementsComponent,
    onBackPressed: () -> Unit
) {
    val screenResult by component.screenResult.subscribeAsState()
    val achievements by component.achievements.subscribeAsState()

    var spoilerAlertConfirmed by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.library_achievements)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ), navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.guard_setup_next)
                        )
                    }
                }
            )
        }, contentWindowInsets = EmptyWindowInsets
    ) { innerPadding ->
        RoundedPage(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when (val s = screenResult) {
                CobaltScreenResult.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        LoadingIndicator()
                    }
                }

                is CobaltScreenResult.Error -> {
                    ExceptionPage(
                        result = s,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    )
                }

                CobaltScreenResult.Loaded -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        itemsIndexed(achievements) { index, item ->
                            val shape = M3ECardListItemShapes.shape(index == 0, index == achievements.lastIndex)
                            var displayed by remember { mutableStateOf(item.isUnlocked || item.isHidden.not()) }

                            var spoilerAlert by remember { mutableStateOf(false) }
                            if (spoilerAlert) {
                                AlertDialog(
                                    icon = {
                                        Icon(Icons.Rounded.VisibilityOff, contentDescription = null)
                                    },
                                    title = {
                                        Text(stringResource(R.string.library_achievements_hidden_spoiler_warning))
                                    }, text = {
                                        Text(stringResource(R.string.library_achievements_hidden_spoiler_warning_text))
                                    }, confirmButton = {
                                        TextButton(onClick = { spoilerAlertConfirmed = true; spoilerAlert = false; displayed = true }) {
                                            Text(stringResource(R.string.guard_revoke_alert_confirm))
                                        }
                                    }, dismissButton = {
                                        TextButton(onClick = { spoilerAlert = false }) {
                                            Text(stringResource(R.string.guard_revoke_alert_dismiss))
                                        }
                                    }, onDismissRequest = { spoilerAlert = false }
                                )
                            }

                            M3ECardListItem(
                                leadingContent = {
                                    if (displayed) {
                                        AsyncImage(
                                            model = if (item.isUnlocked) item.icon else item.iconGray,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .clip(MaterialTheme.shapes.small)
                                                .size(64.dp),
                                            contentScale = ContentScale.Crop,
                                            placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceContainerHighest)
                                        )
                                    } else {
                                        Box(
                                            Modifier
                                                .clip(MaterialTheme.shapes.small)
                                                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                                                .size(64.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                Icons.Rounded.VisibilityOff,
                                                contentDescription = stringResource(id = R.string.library_achievements_hidden),
                                                modifier = Modifier.size(28.dp)
                                            )
                                        }
                                    }
                                }, headlineContent = {
                                    Text(if (displayed) item.localizedName else stringResource(R.string.library_achievements_hidden))
                                }, supportingContent = {
                                    Text(if (displayed) item.localizedDescription else stringResource(R.string.library_achievements_hidden_text))
                                }, overlineContent = {
                                    Text(stringResource(R.string.library_achievements_world, item.globalUnlockPercent))
                                }, trailingContent = {
                                    if (item.isUnlocked) {
                                        Icon(Icons.Rounded.Check, contentDescription = null)
                                    }
                                }, onClick = {
                                    // TODO: Options? Maybe Google Search like on PS4?
                                    if (item.isHidden && !displayed) {
                                        if (spoilerAlertConfirmed) {
                                            displayed = true
                                        } else {
                                            spoilerAlert = true
                                        }
                                    }
                                }, modifier = Modifier.fillParentMaxWidth(), colors = CardDefaults.cardColors(
                                    containerColor = if (item.isUnlocked) {
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.25f).compositeOver(MaterialTheme.colorScheme.surfaceContainerHigh)
                                    } else {
                                        MaterialTheme.colorScheme.surfaceContainerHigh
                                    }
                                ), shape = shape
                            )
                        }
                    }
                }
            }
        }
    }
}