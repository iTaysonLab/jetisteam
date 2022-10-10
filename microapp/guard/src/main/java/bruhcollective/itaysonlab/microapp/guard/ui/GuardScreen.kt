package bruhcollective.itaysonlab.microapp.guard.ui

import android.os.Build
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bruhcollective.itaysonlab.jetisteam.uikit.components.StateButton
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.guard.R
import bruhcollective.itaysonlab.microapp.guard.core.GuardInstance
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
internal fun GuardScreen(
    viewModel: GuardViewModel = hiltViewModel(),
    onMoveClicked: (Long) -> Unit,
    onAddClicked: (Long) -> Unit,
    onMoreClicked: (Long) -> Unit
) {
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val snackState = remember { SnackbarHostState() }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(text = stringResource(id = R.string.guard))
            }
        )
    }, contentWindowInsets = EmptyWindowInsets, snackbarHost = {
        SnackbarHost(hostState = snackState) {
            Snackbar(snackbarData = it)
        }
    }) { innerPadding ->
        when (viewModel.addState) {
            GuardViewModel.AddGuardState.RequestToMove -> {
                AlertDialog(
                    onDismissRequest = viewModel::resetAddState,
                    title = {
                        Text(text = stringResource(id = R.string.guard_setup_move))
                    }, icon = {
                        Icon(imageVector = Icons.Rounded.Update, contentDescription = null)
                    }, text = {
                        Text(text = stringResource(id = R.string.guard_setup_move_desc))
                    }, confirmButton = {
                        TextButton(onClick = { onMoveClicked(viewModel.steamId) }) {
                            Text(text = stringResource(id = R.string.guard_setup_move_allow))
                        }
                    }, dismissButton = {
                        TextButton(onClick = viewModel::resetAddState) {
                            Text(text = stringResource(id = R.string.guard_setup_move_cancel))
                        }
                    }
                )
            }

            is GuardViewModel.AddGuardState.AwaitForSms -> {
                LaunchedEffect(Unit) {
                    onAddClicked(viewModel.steamId)
                }
            }

            GuardViewModel.AddGuardState.Noop -> {}
        }

        when (val state = viewModel.state) {
            is GuardViewModel.GuardState.Available -> {
                val codeState = state.instance.code.collectAsStateWithLifecycle(initialValue = GuardInstance.CodeModel.DefaultInstance)

                GuardInstanceAvailableScreen(
                    modifier = Modifier.fillMaxSize(),
                    code = codeState.value,
                    onMoreSettingsClicked = {
                        onMoreClicked(viewModel.steamId)
                    }, onSignWithQrClicked = {

                    }, onCopyClicked = {
                        scope.launch {
                            clipboard.setText(AnnotatedString(codeState.value.code))

                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                                snackState.showSnackbar(message = context.getString(R.string.guard_actions_copy_snack))
                            }
                        }
                    }
                )
            }

            GuardViewModel.GuardState.Setup -> GuardNoInstanceAvailableScreen(
                onAddClicked = {
                    scope.launch {
                        viewModel.addAuthenticator()
                    }
                },
                inLoadingState = viewModel.isTryingToAddAuth,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}

@Composable
private fun GuardNoInstanceAvailableScreen(
    modifier: Modifier,
    inLoadingState: Boolean,
    onAddClicked: () -> Unit
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Rounded.Lock,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(8.dp))

            CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.headlineMedium) {
                Text(
                    text = stringResource(id = R.string.guard_title),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(Modifier.height(4.dp))

            Text(
                text = stringResource(id = R.string.guard_desc),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(8.dp))

            StateButton(onClick = onAddClicked, inLoadingState = inLoadingState) {
                Text(text = stringResource(id = R.string.guard_setup))
            }
        }
    }
}

@Composable
private fun GuardInstanceAvailableScreen(
    modifier: Modifier,
    code: GuardInstance.CodeModel,
    onSignWithQrClicked: () -> Unit,
    onMoreSettingsClicked: () -> Unit,
    onCopyClicked: () -> Unit
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .align(Alignment.Center)
        ) {
            GuardProgressCircle(
                progress = code.progressRemaining,
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxSize()
            )

            Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = code.code,
                    textAlign = TextAlign.Center,
                    fontSize = 40.sp,
                    letterSpacing = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                TextButton(onCopyClicked) {
                    Icon(imageVector = Icons.Rounded.ContentCopy, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(id = R.string.guard_actions_copy))
                }
            }
        }

        Row(modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(16.dp)) {
            TextButton(onClick = onSignWithQrClicked, enabled = false) {
                Icon(imageVector = Icons.Rounded.QrCode, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(id = R.string.guard_actions_qr))
            }

            Spacer(modifier = Modifier.weight(1f))

            TextButton(onClick = onMoreSettingsClicked) {
                Icon(imageVector = Icons.Rounded.Settings, contentDescription = null)
            }
        }
    }
}

@Composable
private fun GuardProgressCircle(
    modifier: Modifier,
    progress: Float
) {
    val colorForeground = MaterialTheme.colorScheme.primary
    val colorBackground = MaterialTheme.colorScheme.surfaceVariant

    val stroke = with(LocalDensity.current) {
        Stroke(width = ProgressIndicatorDefaults.CircularStrokeWidth.toPx(), cap = StrokeCap.Round)
    }
    
    val progressAnimated by animateFloatAsState(targetValue = progress, animationSpec = tween(1000))
    
    Canvas(modifier = modifier) {
        val diameterOffset = stroke.width / 2
        val arcDimen = size.width - 2 * diameterOffset

        drawArc(
            color = colorBackground,
            startAngle = 270f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = Offset(diameterOffset, diameterOffset),
            size = Size(arcDimen, arcDimen),
            style = stroke
        )
        
        drawArc(
            color = colorForeground,
            startAngle = 270f,
            sweepAngle = 360f * progressAnimated,
            useCenter = false,
            topLeft = Offset(diameterOffset, diameterOffset),
            size = Size(arcDimen, arcDimen),
            style = stroke
        )
    }
}