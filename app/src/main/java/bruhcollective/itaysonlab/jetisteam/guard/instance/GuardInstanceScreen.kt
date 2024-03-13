package bruhcollective.itaysonlab.jetisteam.guard.instance

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.cobalt.guard.instance.GuardInstanceComponent
import bruhcollective.itaysonlab.jetisteam.R
import bruhcollective.itaysonlab.jetisteam.guard.GuardUtils
import bruhcollective.itaysonlab.jetisteam.ui.components.EmptyWindowInsets
import bruhcollective.itaysonlab.jetisteam.ui.components.FullscreenError
import bruhcollective.itaysonlab.jetisteam.ui.components.FullscreenLoading
import bruhcollective.itaysonlab.jetisteam.ui.components.FullscreenPlaceholder
import bruhcollective.itaysonlab.jetisteam.ui.components.IndicatorBehindScrollableTabRow
import bruhcollective.itaysonlab.jetisteam.ui.components.RoundedPage
import bruhcollective.itaysonlab.jetisteam.ui.components.tabIndicatorOffset
import bruhcollective.itaysonlab.jetisteam.ui.theme.partialShapes
import bruhcollective.itaysonlab.ksteam.guard.models.ActiveSession
import bruhcollective.itaysonlab.ksteam.guard.models.ConfirmationListState
import bruhcollective.itaysonlab.ksteam.guard.models.MobileConfirmationItem
import coil.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
import soup.compose.material.motion.animation.materialSharedAxisY
import soup.compose.material.motion.animation.materialSharedAxisYIn
import soup.compose.material.motion.animation.materialSharedAxisYOut
import soup.compose.material.motion.animation.rememberSlideDistance
import java.util.Locale

@Composable
fun GuardInstanceScreen(
    component: GuardInstanceComponent,
    topPadding: Dp
) {
    val slideDistance = rememberSlideDistance()
    val qrStateAnimationDuration = 400

    val qrSlot by component.qrSlot.subscribeAsState()
    val isQrScannerOpened = qrSlot.child != null

    val pagerState = rememberPagerState { 2 }
    val state by component.state.collectAsState()

    val innerSurfaceTransition = updateTransition(targetState = isQrScannerOpened, label = "[GuardInstance] Rounded page background + corners")

    val innerSurfaceBackgroundColor by innerSurfaceTransition.animateColor(
        label = "[GuardInstance] Rounded page background",
        transitionSpec = {
            tween(
                durationMillis = qrStateAnimationDuration,
                easing = FastOutSlowInEasing
            )
        }
    ) { qrVisible ->
        if (qrVisible) {
            Color.Black
        } else {
            MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
        }
    }

    val innerSurfaceCornerRadius by innerSurfaceTransition.animateDp(
        label = "[GuardInstance] Rounded page corners",
        transitionSpec = {
            tween(
                durationMillis = qrStateAnimationDuration,
                easing = FastOutSlowInEasing
            )
        }
    ) { qrVisible ->
        if (qrVisible) {
            0.dp
        } else {
            28.dp
        }
    }

    Scaffold(topBar = {
        AnimatedVisibility(
            visible = isQrScannerOpened.not(),
            enter = materialSharedAxisYIn(
                forward = isQrScannerOpened,
                slideDistance = slideDistance,
                durationMillis = qrStateAnimationDuration
            ) + expandVertically(
                expandFrom = Alignment.Top,
                animationSpec = tween(
                    durationMillis = qrStateAnimationDuration,
                    easing = FastOutSlowInEasing
                )
            ),
            exit = shrinkVertically(
                shrinkTowards = Alignment.Top,
                animationSpec = tween(
                    durationMillis = qrStateAnimationDuration,
                    easing = FastOutSlowInEasing
                )
            ) + materialSharedAxisYOut(
                forward = isQrScannerOpened,
                slideDistance = slideDistance,
                durationMillis = qrStateAnimationDuration
            )
        ) {
            CodeBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = topPadding + 16.dp, bottom = 16.dp)
                    .padding(horizontal = 16.dp),
                onCopyClicked = {  },
                code = state.code,
                codeProgress = state.codeProgress,
                onDeleteClicked = {

                },
                onRecoveryClicked = {

                },
                connectedToSteam = true
            )
        }
    }, floatingActionButton = {
        AnimatedVisibility(
            visible = isQrScannerOpened.not(),
            enter = materialSharedAxisYIn(forward = isQrScannerOpened.not(), slideDistance = slideDistance, durationMillis = qrStateAnimationDuration),
            exit = materialSharedAxisYOut(forward = isQrScannerOpened.not(), slideDistance = slideDistance, durationMillis = qrStateAnimationDuration),
        ) {
            FloatingActionButton(onClick = component::openQrScanner) {
                Icon(imageVector = Icons.Rounded.QrCodeScanner, contentDescription = stringResource(id = R.string.guard_qr_action))
            }
        }
    }, contentWindowInsets = EmptyWindowInsets) { innerPadding ->
        Surface(
            color = innerSurfaceBackgroundColor,
            contentColor = MaterialTheme.colorScheme.onSurface,
            shape = RoundedCornerShape(topStart = innerSurfaceCornerRadius, topEnd = innerSurfaceCornerRadius),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            AnimatedContent(
                targetState = qrSlot,
                label = "",
                transitionSpec = {
                    materialSharedAxisY(forward = targetState.child == null, slideDistance = slideDistance, durationMillis = qrStateAnimationDuration)
                }, modifier = Modifier.fillMaxSize()
            ) { qrSlotState ->
                if (qrSlotState.child != null) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)) {

                        Icon(
                            imageVector = Icons.Rounded.CameraAlt,
                            contentDescription = null,
                            modifier = Modifier.size(72.dp).align(Alignment.Center)
                        )
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize()) {
                        GuardInstanceHeader(
                            pagerState = pagerState,
                            modifier = Modifier.align(Alignment.TopCenter)
                        )

                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 64.dp)
                        ) { pos ->
                            if (pos == 0) {
                                GuardCodeAndConfirmationsPage(
                                    confirmationState = state.confirmations,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else if (pos == 1) {
                                GuardSessionsPage(
                                    sessionsLoaded = state.sessionsLoaded,
                                    sessions = state.sessions,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GuardInstanceHeader(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    IndicatorBehindScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        containerColor = Color.Transparent,
        indicator = { tabPositions ->
            Box(
                Modifier
                    .padding(vertical = 12.dp)
                    .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface)
            )
        },
        edgePadding = 16.dp,
        modifier = modifier,
        tabAlignment = Alignment.Center
    ) {
        Tab(
            selected = pagerState.currentPage == 0,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(0)
                }
            },
            selectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ) {
            Text(
                text = stringResource(id = R.string.guard).uppercase(Locale.getDefault()),
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp),
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )
        }

        Tab(
            selected = pagerState.currentPage == 1,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(1)
                }
            },
            selectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ) {
            Text(
                text = stringResource(id = R.string.guard_sessions).uppercase(Locale.getDefault()),
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp),
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
private fun GuardCodeAndConfirmationsPage(
    confirmationState: ConfirmationListState,
    modifier: Modifier = Modifier
) {
    when (confirmationState) {
        is ConfirmationListState.Loading -> {
            FullscreenLoading()
        }

        is ConfirmationListState.NetworkError -> {
            FullscreenError(onReload = { /*TODO*/ }, exception = confirmationState.e)
        }

        is ConfirmationListState.Error -> {
            FullscreenPlaceholder(title = confirmationState.message, text = confirmationState.detail)
        }

        is ConfirmationListState.Success -> {
            if (confirmationState.conf.isNotEmpty()) {
                LazyColumn(modifier = modifier, contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(confirmationState.conf) {
                        ConfirmationCard(confirmation = it, onClick = {
                            // onConfirmationClicked(it)
                        })
                    }
                }
            } else {
                FullscreenPlaceholder(
                    title = stringResource(id = R.string.guard_confirmations_empty),
                    text = stringResource(id = R.string.guard_confirmations_empty_desc),
                )
            }
        }
    }
}

@Composable
private fun ConfirmationCard(
    confirmation: MobileConfirmationItem,
    onClick: () -> Unit
) {
    val formattedDate = remember(confirmation.creationTime) {
        GuardUtils.formatDateTimeToLocale(confirmation.creationTime * 1000L)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(16.dp)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = confirmation.icon,
                contentDescription = null,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .size(64.dp),
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp))
            )

            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = confirmation.typeName, fontSize = 13.sp)
                    Text(text = formattedDate, modifier = Modifier.alpha(0.7f), fontSize = 13.sp)
                }

                Text(
                    text = confirmation.headline,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(text = remember(confirmation.summary) {
                    confirmation.summary.joinToString(separator = "\n")
                }, fontSize = 13.sp, lineHeight = 18.sp)
            }
        }
    }
}

@Composable
private fun GuardSessionsPage(
    sessionsLoaded: Boolean,
    sessions: ImmutableList<ActiveSession>,
    modifier: Modifier = Modifier
) {
    if (sessionsLoaded) {
        LazyColumn(contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp), modifier = modifier) {
            itemsIndexed(sessions, key = { _, session ->
                session.id
            }) { index, session ->
                SessionItem(session, top = index == 0, bottom = index == sessions.lastIndex, onClick = {
                    // onSessionClicked(session)
                })

                if (index != sessions.lastIndex) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
                }
            }
        }
    } else {
        FullscreenLoading()
    }
}

@Composable
private fun SessionItem(
    session: ActiveSession,
    top: Boolean,
    bottom: Boolean,
    onClick: () -> Unit
) {
    val ctx = LocalContext.current
    val visuals = remember(session) { GuardUtils.formatSessionDescByTime(ctx, session) }

    ListItem(
        headlineContent = {
            Text(text = remember(session) {
                session.deviceName.ifEmpty { visuals.fallbackName }
            }, maxLines = 1)
        }, leadingContent = {
            Icon(imageVector = visuals.icon(), contentDescription = null)
        }, supportingContent = {
            Text(text = stringResource(id = R.string.guard_sessions_last_seen, visuals.relativeLastSeen), maxLines = 1)
        }, colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(16.dp)
        ), modifier = Modifier
            .clip(
                when {
                    top && bottom -> MaterialTheme.shapes.large
                    top -> MaterialTheme.partialShapes.largeTopShape
                    bottom -> MaterialTheme.partialShapes.largeBottomShape
                    else -> RectangleShape
                }
            )
            .clickable(onClick = onClick)
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CodeBox(
    modifier: Modifier,
    code: String,
    codeProgress: Float,
    connectedToSteam: Boolean,
    onCopyClicked: () -> Unit,
    onRecoveryClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
) {
    var isMenuShown by remember { mutableStateOf(false) }

    val background by animateColorAsState(targetValue = if (isMenuShown) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceColorAtElevation(
        8.dp
    ), label = "")

    val progressBackground by animateColorAsState(targetValue = if (isMenuShown) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        label = ""
    )

    val intProgress by animateFloatAsState(targetValue = codeProgress, animationSpec = tween(1000, easing = LinearEasing),
        label = ""
    )

    val slideDistance = rememberSlideDistance()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = background
        ), modifier = modifier, shape = MaterialTheme.shapes.medium
    ) {
        AnimatedContent(targetState = isMenuShown, transitionSpec = {
            materialSharedAxisY(forward = targetState, slideDistance = slideDistance).using(
                SizeTransform(clip = false)
            )
        }, label = "") {
            if (it) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 18.dp)
                ) {
                    IconButton(onClick = {
                        isMenuShown = false
                    }, colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ), modifier = Modifier) {
                        Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
                    }

                    Spacer(Modifier.weight(1f))

                    IconButton(onClick = onRecoveryClicked, colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ), modifier = Modifier) {
                        Icon(imageVector = Icons.Rounded.Help, contentDescription = null)
                    }

                    Spacer(Modifier.weight(1f))

                    IconButton(onClick = onDeleteClicked, colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ), modifier = Modifier) {
                        Icon(imageVector = Icons.Rounded.DeleteForever, contentDescription = null)
                    }
                }
            } else {
                Column {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)) {
                        IconButton(onClick = {
                            isMenuShown = true
                        }, colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        ), modifier = Modifier.align(Alignment.CenterStart), enabled = connectedToSteam) {
                            Icon(imageVector = Icons.Rounded.Settings, contentDescription = null)
                        }

                        AnimatedContent(targetState = code, transitionSpec = {
                            if (initialState.isEmpty()) {
                                EnterTransition.None togetherWith ExitTransition.None
                            } else {
                                materialSharedAxisY(forward = true, slideDistance = slideDistance)
                            }.using(
                                SizeTransform(clip = false)
                            )
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)) { code ->
                            Text(
                                text = code,
                                textAlign = TextAlign.Center,
                                fontSize = 40.sp,
                                letterSpacing = 12.sp,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }

                        IconButton(onCopyClicked, colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        ), modifier = Modifier.align(Alignment.CenterEnd)) {
                            Icon(imageVector = Icons.Rounded.ContentCopy, contentDescription = null)
                        }
                    }

                    LinearProgressIndicator(
                        progress = { intProgress },
                        modifier = Modifier.fillMaxWidth(),
                        trackColor = progressBackground,
                    )
                }
            }
        }
    }
}