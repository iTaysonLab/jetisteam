package bruhcollective.itaysonlab.microapp.library.ui.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.components.BottomSheetHandle
import bruhcollective.itaysonlab.jetisteam.uikit.components.ResizableCircularIndicator
import bruhcollective.itaysonlab.microapp.library.R
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.BlurTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnedGameBottomSheet(
    viewModel: OwnedGameBottomSheetViewModel = hiltViewModel(),
    onNavigateToGamePage: (Int) -> Unit,
    onNavigateToAchievements: (Long, Int) -> Unit,
    onOpenRemoteInstallations: (Long, Int) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Header

        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(viewModel.headerBgUrl?.url)
                    .transformations(
                        if (viewModel.headerBgUrl?.blur == true) listOf(
                            BlurTransformation(LocalContext.current, 20f, 4f)
                        ) else emptyList()
                    )
                    .build(), contentDescription = null, modifier = Modifier
                    .height(300.dp)
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.8f), Color.Transparent),
                                start = Offset(x = size.width / 2f, y = 0f),
                                end = Offset(x = size.width / 2f, y = size.height),
                            )
                        )
                    }, contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (viewModel.headerLogoUrl != null) {
                    if (viewModel.headerLogoUrl == "") {
                        Text(
                            text = viewModel.gameName,
                            color = Color.White,
                            fontSize = 21.sp,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        AsyncImage(
                            model = viewModel.headerLogoUrl,
                            contentDescription = null,
                            modifier = Modifier.heightIn(min = 0.dp, max = 100.dp),
                            contentScale = ContentScale.FillHeight
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Timer,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = viewModel.totalPlaytime, color = Color.White)
                }
            }

            BottomSheetHandle(modifier = Modifier.align(Alignment.TopCenter))

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .clip(
                        MaterialTheme.shapes.extraLarge.copy(
                            bottomStart = CornerSize(0.dp),
                            bottomEnd = CornerSize(0.dp)
                        )
                    )
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxWidth()
                    .height(16.dp)
            )
        }

        Column(Modifier.navigationBarsPadding()) {

            Spacer(modifier = Modifier.height(4.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                )
            ) {
                when (val state = viewModel.achievementCardState) {
                    OwnedGameBottomSheetViewModel.AchievementCardState.Error -> {
                        // TODO
                    }

                    OwnedGameBottomSheetViewModel.AchievementCardState.Loading -> {
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

                    is OwnedGameBottomSheetViewModel.AchievementCardState.Ready -> {
                        val completedColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                            .compositeOver(MaterialTheme.colorScheme.primary)

                        ListItem(
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Rounded.MilitaryTech,
                                    contentDescription = null
                                )
                            }, headlineText = {
                                Text(text = stringResource(id = R.string.library_sheet_achv))
                            }, supportingText = {
                                Text(text = if (state.available) {
                                    state.formattedString
                                } else {
                                    stringResource(id = R.string.library_sheet_achv_none)
                                })
                            }, colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent
                            ), trailingContent = {
                                if (state.available) {
                                    Icon(
                                        imageVector = Icons.Rounded.ChevronRight,
                                        contentDescription = null
                                    )
                                }
                            }, modifier = Modifier
                                .clickable(onClick = {
                                    onNavigateToAchievements(viewModel.longSteamId, viewModel.appId)
                                }, enabled = state.available)
                                .drawBehind {
                                    drawRect(
                                        completedColor,
                                        size = size.copy(width = size.width * state.percentage)
                                    )
                                }
                                .fillMaxWidth()
                        )

                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            ListItem(
                leadingContent = {
                    Icon(imageVector = Icons.Rounded.Download, contentDescription = null)
                },
                headlineText = {
                    Text(text = "Manage remote installation")
                },
                modifier = Modifier.clickable(onClick = {
                    onOpenRemoteInstallations(
                        viewModel.longSteamId,
                        viewModel.appId
                    )
                }),
                colors = ListItemDefaults.colors(
                    leadingIconColor = MaterialTheme.colorScheme.primary
                )
            )

            Divider(color = MaterialTheme.colorScheme.surfaceVariant)

            ListItem(
                leadingContent = {
                    Icon(imageVector = Icons.Rounded.Store, contentDescription = null)
                },
                headlineText = {
                    Text(text = "Open Store Page")
                },
                modifier = Modifier.clickable(onClick = { onNavigateToGamePage(viewModel.appId) }),
                colors = ListItemDefaults.colors(
                    leadingIconColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}