package bruhcollective.itaysonlab.cobalt.screens.library.games.alert

import androidx.collection.mutableScatterMapOf
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.StickyNote2
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.MilitaryTech
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.Timelapse
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.ext.DurationUtils
import bruhcollective.itaysonlab.cobalt.ext.GuardUtils
import bruhcollective.itaysonlab.cobalt.library.games.alert.GameSheetComponent
import bruhcollective.itaysonlab.cobalt.ui.components.M3ECardListItem
import bruhcollective.itaysonlab.cobalt.ui.components.M3ECardListItemShapes
import bruhcollective.itaysonlab.cobalt.ui.components.ResizableCircularIndicator
import bruhcollective.itaysonlab.cobalt.ui.components.quickies.GameHeaderElement
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import soup.compose.material.motion.animation.materialSharedAxisX
import soup.compose.material.motion.animation.materialSharedAxisY
import soup.compose.material.motion.animation.rememberSlideDistance
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

private enum class ModalGameSheetMode {
    General, Playtime, LaunchDates
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
internal fun ModalGameSheet(
    onDismiss: () -> Unit,
    component: GameSheetComponent
) {
    val sheetBackgroundUrl by component.sheetBackgroundUrl.subscribeAsState()
    val sheetForegroundUrl by component.sheetForegroundUrl.subscribeAsState()
    val gameTitle by component.title.subscribeAsState()
    val achievements by component.achievements.subscribeAsState()
    val playtime by component.playtime.subscribeAsState()

    val slideDistance = rememberSlideDistance()
    var mode by remember { mutableStateOf(ModalGameSheetMode.General) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = null,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        GameHeaderElement(
            backgroundImageUrl = sheetBackgroundUrl,
            foregroundImageUrl = sheetForegroundUrl,
            foregroundImageAlt = gameTitle,
            additionalContentHeader = 48.dp + 32.dp,
            modifier = Modifier.fillMaxWidth(),
            additionalContent = if (playtime.totalPlaytime != Duration.ZERO) {
                {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp + 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (playtime.totalPlaytime != Duration.ZERO) {
                            FilterChip(
                                selected = mode == ModalGameSheetMode.Playtime,
                                leadingIcon = {
                                    Icon(Icons.Rounded.Timelapse, contentDescription = null)
                                },
                                label = {
                                    Text(stringResource(R.string.library_hours, playtime.totalPlaytime.toDouble(DurationUnit.HOURS)))
                                },
                                onClick = {
                                    mode = if (mode == ModalGameSheetMode.Playtime) {
                                        ModalGameSheetMode.General
                                    } else {
                                        ModalGameSheetMode.Playtime
                                    }
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = Color.Black.copy(alpha = 0.75f),
                                    iconColor = Color.White,
                                    labelColor = Color.White,
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                    selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                                )
                            )

                            FilterChip(
                                selected = mode == ModalGameSheetMode.LaunchDates,
                                leadingIcon = {
                                    Icon(Icons.Rounded.CalendarToday, contentDescription = null)
                                },
                                label = {
                                    Text(
                                        text = remember(playtime.lastLaunch) {
                                            DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(
                                                playtime.lastLaunch?.toLocalDateTime(TimeZone.UTC)?.toJavaLocalDateTime()
                                            )
                                        }
                                    )
                                },
                                onClick = {
                                    mode = if (mode == ModalGameSheetMode.LaunchDates) {
                                        ModalGameSheetMode.General
                                    } else {
                                        ModalGameSheetMode.LaunchDates
                                    }
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = Color.Black.copy(alpha = 0.75f),
                                    iconColor = Color.White,
                                    labelColor = Color.White,
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                    selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                                )
                            )
                        }
                    }
                }
            } else null
        )

        Column(Modifier.navigationBarsPadding().padding(bottom = 8.dp)) {
            Spacer(modifier = Modifier.height(4.dp))

            AnimatedContent(
                targetState = mode,
                modifier = Modifier.fillMaxWidth(),
                transitionSpec = {
                    if (targetState == ModalGameSheetMode.General) {
                        materialSharedAxisY(forward = false, slideDistance = slideDistance)
                    } else if (initialState == ModalGameSheetMode.General) {
                        materialSharedAxisY(forward = true, slideDistance = slideDistance)
                    } else {
                        materialSharedAxisX(forward = targetState == ModalGameSheetMode.LaunchDates, slideDistance = slideDistance)
                    }.using(SizeTransform(clip = false))
                }
            ) { m ->
                Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    when (m) {
                        ModalGameSheetMode.General -> {
                            ModalGameSheetGenericView(
                                achievements = achievements,
                                onAchievementsClicked = component::onAchievementsClicked,
                                onRemoteInstallClicked = component::onRemoteInstallClicked,
                                onStorePageClicked = component::onStorePageClicked,
                                onGameNotesClicked = component::onGameNotesClicked,
                            )
                        }

                        ModalGameSheetMode.Playtime -> {
                            ModalGameSheetPlaytimeView(
                                playtime = playtime
                            )
                        }

                        ModalGameSheetMode.LaunchDates -> {
                            ModalGameSheetLaunchDatesView(
                                playtime = playtime
                            )
                        }
                    }
                }
            }
        }
    }
}

//

private val colors = mutableScatterMapOf(
    GameSheetComponent.Platform.Win to Color(0, 145, 255), // win
    GameSheetComponent.Platform.Linux to Color(255, 146, 48), // linux
    GameSheetComponent.Platform.Mac to Color(255, 55, 95), // mac
    GameSheetComponent.Platform.Deck to Color(48, 209, 88), // deck
)

private val strings = mutableScatterMapOf(
    GameSheetComponent.Platform.Win to R.string.library_sheet_platform_win,
    GameSheetComponent.Platform.Linux to R.string.library_sheet_platform_linux,
    GameSheetComponent.Platform.Mac to R.string.library_sheet_platform_mac,
    GameSheetComponent.Platform.Deck to R.string.library_sheet_platform_deck,
)

@OptIn(ExperimentalTime::class)
@Composable
private fun ModalGameSheetLaunchDatesView(
    playtime: GameSheetComponent.PlaytimeInformation
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        item {
            Text(
                text = stringResource(R.string.library_sheet_last),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.fillParentMaxWidth().padding(horizontal = 16.dp).padding(bottom = 4.dp),
            )
        }

        itemsIndexed(playtime.platformEntries) { index, entry ->
            val top = index == 0
            val bottom = index == playtime.platformEntries.lastIndex

            M3ECardListItem(
                onClick = {},
                modifier = Modifier.fillParentMaxWidth(),
                shape = M3ECardListItemShapes.shape(top, bottom),
                headlineContent = {
                    Text(text = remember(entry.lastLaunch) {
                        GuardUtils.formatDateTimeToLocaleFull(entry.lastLaunch.toEpochMilliseconds())
                    })
                },
                supportingContent = {
                    Text(text = stringResource(id = strings[entry.platform]!!))
                }
            )
        }

        item {
            Text(
                text = stringResource(R.string.library_sheet_first),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.fillParentMaxWidth().padding(horizontal = 16.dp).padding(top = 12.dp, bottom = 4.dp),
            )
        }

        itemsIndexed(playtime.platformEntries) { index, entry ->
            val top = index == 0
            val bottom = index == playtime.platformEntries.lastIndex

            M3ECardListItem(
                onClick = {},
                modifier = Modifier.fillParentMaxWidth(),
                shape = M3ECardListItemShapes.shape(top, bottom),
                headlineContent = {
                    Text(text = remember(entry.firstLaunch) {
                        GuardUtils.formatDateTimeToLocaleFull(entry.firstLaunch.toEpochMilliseconds())
                    })
                },
                supportingContent = {
                    Text(text = stringResource(id = strings[entry.platform]!!))
                }
            )
        }
    }
}

@Composable
private fun ModalGameSheetPlaytimeView(
    playtime: GameSheetComponent.PlaytimeInformation
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        item {
            Card(
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.fillParentMaxWidth().padding(bottom = 12.dp),
            ) {
                Canvas(Modifier.fillMaxWidth().height(24.dp)) {
                    val width = size.width
                    var x = 0f

                    playtime.platformEntries.forEachIndexed { index, entry ->
                        val widthP = width * entry.percentage

                        drawRect(
                            color = colors[entry.platform]!!,
                            topLeft = Offset(x = x, y = 0f),
                            size = this.size.copy(width = widthP)
                        )

                        if (index == playtime.platformEntries.lastIndex) {
                            drawRect(
                                color = colors[entry.platform]!!,
                                topLeft = Offset(x = x, y = 0f),
                            )
                        } else {
                            x += widthP
                        }
                    }
                }
            }
        }

        itemsIndexed(playtime.platformEntries) { index, entry ->
            val ctx = LocalContext.current
            val top = index == 0
            val bottom = index == playtime.platformEntries.lastIndex

            M3ECardListItem(
                onClick = {},
                modifier = Modifier.fillParentMaxWidth(),
                shape = M3ECardListItemShapes.shape(top, bottom),
                leadingContent = {
                    Box(Modifier.clip(CircleShape).size(16.dp).background(colors[entry.platform]!!))
                },
                headlineContent = {
                    Text(text = remember(entry.duration) {
                        DurationUtils.formatDuration(ctx, entry.duration)
                    })
                },
                supportingContent = {
                    Text(text = stringResource(id = strings[entry.platform]!!))
                },
                trailingContent = {
                    Text(text = "${(entry.percentage * 100).roundToInt()}%")
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ModalGameSheetGenericView(
    achievements: GameSheetComponent.AchievementsState,
    onAchievementsClicked: () -> Unit,
    onRemoteInstallClicked: () -> Unit,
    onStorePageClicked: () -> Unit,
    onGameNotesClicked: () -> Unit,
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        item {
            Card(
                shape = MaterialTheme.shapes.largeIncreased,
                modifier = Modifier.fillParentMaxWidth().padding(bottom = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                )
            ) {
                when (val state = achievements) {
                    is GameSheetComponent.AchievementsState.Available -> {
                        val completedColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                            .compositeOver(MaterialTheme.colorScheme.primary)

                        ListItem(
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Rounded.MilitaryTech,
                                    contentDescription = null
                                )
                            }, headlineContent = {
                                Text(text = stringResource(id = R.string.library_achievements))
                            }, supportingContent = {
                                Text(text = if (state.total > 0) {
                                    "${state.completed} / ${state.total}"
                                } else {
                                    stringResource(id = R.string.library_achievements_none)
                                })
                            }, colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent,
                                leadingIconColor = MaterialTheme.colorScheme.onSurface,
                                headlineColor = MaterialTheme.colorScheme.onSurface,
                                supportingColor = MaterialTheme.colorScheme.onSurface,
                            ), trailingContent = {
                                if (state.total > 0) {
                                    Icon(
                                        imageVector = Icons.Rounded.ChevronRight,
                                        contentDescription = null
                                    )
                                }
                            }, modifier = Modifier
                                .clickable(onClick = onAchievementsClicked, enabled = state.total > 0)
                                .drawBehind {
                                    drawRect(
                                        completedColor,
                                        size = size.copy(width = size.width * (state.percentage / 100f))
                                    )
                                }
                                .fillMaxWidth()
                        )
                    }

                    GameSheetComponent.AchievementsState.Loading -> {
                        Box(
                            Modifier
                                .height(72.dp)
                                .fillMaxWidth()
                        ) {
                            ResizableCircularIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                indicatorSize = 24.dp,
                                strokeWidth = 2.dp
                            )
                        }
                    }

                    GameSheetComponent.AchievementsState.Unavailable -> {

                    }
                }
            }
        }

        item {
            M3ECardListItem(
                onClick = onRemoteInstallClicked,
                modifier = Modifier.fillParentMaxWidth(),
                shape = M3ECardListItemShapes.top,
                leadingContent = {
                    Icon(imageVector = Icons.Rounded.Download, contentDescription = null)
                },
                headlineContent = {
                    Text(text = stringResource(id = R.string.devices_remote_install))
                },
            )
        }

        item {
            M3ECardListItem(
                onClick = onStorePageClicked,
                modifier = Modifier.fillParentMaxWidth(),
                leadingContent = {
                    Icon(imageVector = Icons.Rounded.ShoppingCart, contentDescription = null)
                },
                headlineContent = {
                    Text(text = stringResource(id = R.string.store_view))
                },
            )
        }

        item {
            M3ECardListItem(
                onClick = onGameNotesClicked,
                modifier = Modifier.fillParentMaxWidth(),
                shape = M3ECardListItemShapes.bottom,
                leadingContent = {
                    Icon(imageVector = Icons.AutoMirrored.Rounded.StickyNote2, contentDescription = null)
                },
                headlineContent = {
                    Text(text = stringResource(id = R.string.library_notes))
                },
            )
        }
    }
}