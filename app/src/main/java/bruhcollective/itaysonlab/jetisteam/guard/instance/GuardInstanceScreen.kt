package bruhcollective.itaysonlab.jetisteam.guard.instance

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.cobalt.guard.instance.GuardInstanceComponent
import bruhcollective.itaysonlab.jetisteam.R
import bruhcollective.itaysonlab.jetisteam.guard.bottom_sheet.GuardRecoveryCodeSheet
import bruhcollective.itaysonlab.jetisteam.guard.bottom_sheet.GuardRemoveSheet
import bruhcollective.itaysonlab.jetisteam.guard.session.GuardSessionScreen
import bruhcollective.itaysonlab.jetisteam.ui.components.EmptyWindowInsets
import bruhcollective.itaysonlab.jetisteam.ui.components.IndicatorBehindScrollableTabRow
import bruhcollective.itaysonlab.jetisteam.ui.components.tabIndicatorOffset
import com.arkivanov.decompose.extensions.compose.subscribeAsState
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

    val fullscreenAlertSlot by component.fullscreenAlertSlot.subscribeAsState()
    val alertSlot by component.alertSlot.subscribeAsState()

    val fullscreenExpandDuration = 300
    val isAnyFullscreenAlertPresent = fullscreenAlertSlot.child != null
    val isQrFullscreenAlertPresent = remember(fullscreenAlertSlot.child) { fullscreenAlertSlot.child?.instance is GuardInstanceComponent.FullscreenAlertChild.QrCodeScanner }

    val pagerState = rememberPagerState { 2 }
    val state by component.state.collectAsState()
    val guardSessionListState = rememberLazyListState()
    val clipboardManager = LocalClipboardManager.current

    val innerSurfaceBackgroundColor by animateColorAsState(
        label = "[GuardInstance] Rounded page background",
        targetValue = if (isQrFullscreenAlertPresent) {
            Color.Black
        } else {
            MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
        },
        animationSpec =
        tween(
            durationMillis = fullscreenExpandDuration,
            easing = FastOutSlowInEasing
        )
    )

    val innerSurfaceCornerRadius by animateDpAsState(
        label = "[GuardInstance] Rounded page corners",
        targetValue = if (isAnyFullscreenAlertPresent) {
            0.dp
        } else {
            28.dp
        },
        animationSpec = tween(
            durationMillis = fullscreenExpandDuration,
            easing = FastOutSlowInEasing
        )
    )

    alertSlot.child?.instance?.let { child ->
        when (child) {
            is GuardInstanceComponent.AlertChild.RecoveryCode -> {
                GuardRecoveryCodeSheet(component = child.component)
            }

            is GuardInstanceComponent.AlertChild.DeleteGuard -> {
                GuardRemoveSheet(component = child.component)
            }
        }
    }

    Scaffold(topBar = {
        AnimatedVisibility(
            visible = isAnyFullscreenAlertPresent.not(),
            enter = materialSharedAxisYIn(
                forward = isAnyFullscreenAlertPresent,
                slideDistance = slideDistance,
                durationMillis = fullscreenExpandDuration
            ) + expandVertically(
                expandFrom = Alignment.Top,
                animationSpec = tween(
                    durationMillis = fullscreenExpandDuration,
                    easing = FastOutSlowInEasing
                )
            ),
            exit = shrinkVertically(
                shrinkTowards = Alignment.Top,
                animationSpec = tween(
                    durationMillis = fullscreenExpandDuration,
                    easing = FastOutSlowInEasing
                )
            ) + materialSharedAxisYOut(
                forward = isAnyFullscreenAlertPresent,
                slideDistance = slideDistance,
                durationMillis = fullscreenExpandDuration
            )
        ) {
            CodeBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = topPadding + 16.dp, bottom = 16.dp)
                    .padding(horizontal = 16.dp),
                onCopyClicked = {
                    // TODO: Lower Androids without a toast
                    clipboardManager.setText(
                        AnnotatedString(state.code)
                    )
                },
                code = state.code,
                codeProgress = state.codeProgress,
                onDeleteClicked = component::openDeleteSheet,
                onRecoveryClicked = component::openRecoveryCodeSheet,
                connectedToSteam = true
            )
        }
    }, floatingActionButton = {
        AnimatedVisibility(
            visible = isAnyFullscreenAlertPresent.not(),
            enter = materialSharedAxisYIn(
                forward = isAnyFullscreenAlertPresent.not(),
                slideDistance = slideDistance,
                durationMillis = fullscreenExpandDuration
            ),
            exit = materialSharedAxisYOut(
                forward = isAnyFullscreenAlertPresent.not(),
                slideDistance = slideDistance,
                durationMillis = fullscreenExpandDuration
            ),
        ) {
            FloatingActionButton(onClick = component::openQrScanner) {
                Icon(
                    imageVector = Icons.Rounded.QrCodeScanner,
                    contentDescription = stringResource(id = R.string.guard_qr_action)
                )
            }
        }
    }, contentWindowInsets = EmptyWindowInsets) { innerPadding ->
        Surface(
            color = innerSurfaceBackgroundColor,
            contentColor = MaterialTheme.colorScheme.onSurface,
            shape = RoundedCornerShape(
                topStart = innerSurfaceCornerRadius,
                topEnd = innerSurfaceCornerRadius
            ),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            AnimatedContent(
                targetState = fullscreenAlertSlot,
                label = "",
                transitionSpec = {
                    materialSharedAxisY(
                        forward = targetState.child == null,
                        slideDistance = slideDistance,
                        durationMillis = fullscreenExpandDuration
                    )
                }, modifier = Modifier.fillMaxSize()
            ) { qrSlotState ->
                if (qrSlotState.child != null) {
                    qrSlotState.child?.instance?.let { child ->
                        when (child) {
                            is GuardInstanceComponent.FullscreenAlertChild.MobileConfirmationDetail -> {

                            }

                            is GuardInstanceComponent.FullscreenAlertChild.QrCodeScanner -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.CameraAlt,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(72.dp)
                                            .align(Alignment.Center)
                                    )
                                }
                            }

                            is GuardInstanceComponent.FullscreenAlertChild.SessionDetail -> {
                                GuardSessionScreen(
                                    component = child.component,
                                    topPadding = topPadding
                                )
                            }
                        }
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
                                    listState = guardSessionListState,
                                    sessionsLoaded = state.sessionsLoaded,
                                    sessions = state.sessions,
                                    onSessionClicked = component::openSessionDetail,
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
private fun CodeBox(
    code: String,
    codeProgress: Float,
    connectedToSteam: Boolean,
    onCopyClicked: () -> Unit,
    onRecoveryClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isMenuShown by remember { mutableStateOf(false) }

    val background by animateColorAsState(
        targetValue = if (isMenuShown) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceColorAtElevation(
            8.dp
        ), label = ""
    )

    val progressBackground by animateColorAsState(
        targetValue = if (isMenuShown) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        label = ""
    )

    val intProgress by animateFloatAsState(
        targetValue = codeProgress, animationSpec = tween(1000, easing = LinearEasing),
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
                    IconButton(
                        onClick = {
                            isMenuShown = false
                        }, colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ), modifier = Modifier
                    ) {
                        Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
                    }

                    Spacer(Modifier.weight(1f))

                    IconButton(
                        onClick = onRecoveryClicked, colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ), modifier = Modifier
                    ) {
                        Icon(imageVector = Icons.Rounded.Help, contentDescription = null)
                    }

                    Spacer(Modifier.weight(1f))

                    IconButton(
                        onClick = onDeleteClicked, colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ), modifier = Modifier
                    ) {
                        Icon(imageVector = Icons.Rounded.DeleteForever, contentDescription = null)
                    }
                }
            } else {
                Column {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        IconButton(
                            onClick = {
                                isMenuShown = true
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.align(Alignment.CenterStart),
                            enabled = connectedToSteam
                        ) {
                            Icon(imageVector = Icons.Rounded.Settings, contentDescription = null)
                        }

                        AnimatedContent(
                            targetState = code, transitionSpec = {
                                if (initialState.isEmpty()) {
                                    EnterTransition.None togetherWith ExitTransition.None
                                } else {
                                    materialSharedAxisY(
                                        forward = true,
                                        slideDistance = slideDistance
                                    )
                                }.using(
                                    SizeTransform(clip = false)
                                )
                            }, modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                        ) { code ->
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

                        IconButton(
                            onCopyClicked, colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            ), modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
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