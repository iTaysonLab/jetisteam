package bruhcollective.itaysonlab.microapp.guard.ui.setup

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bruhcollective.itaysonlab.jetisteam.uikit.components.StateTextButton
import bruhcollective.itaysonlab.jetisteam.uikit.page.FullscreenLoading
import bruhcollective.itaysonlab.ksteam.guard.models.SgCreationFlowState
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.guard.R
import bruhcollective.itaysonlab.microapp.guard.ui.components.CodeRow
import bruhcollective.itaysonlab.microapp.guard.ui.components.CodeRowState
import kotlinx.coroutines.launch
import soup.compose.material.motion.animation.materialSharedAxisX
import soup.compose.material.motion.animation.rememberSlideDistance

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun GuardSetupScreen(
    viewModel: GuardSetupViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onSuccess: (Long) -> Unit
) {
    val slideDistance = rememberSlideDistance()
    val state = viewModel.tfaAddState.collectAsStateWithLifecycle()

    AnimatedContent(targetState = state.value, transitionSpec = {
        val goingBack =
            targetState is SgCreationFlowState.SmsSent && initialState is SgCreationFlowState.Processing

        val stateRestart = initialState is SgCreationFlowState.AlreadyHasGuard && targetState is SgCreationFlowState.AlreadyHasGuard

        if (stateRestart) {
            EnterTransition.None with ExitTransition.None
        } else {
            materialSharedAxisX(forward = goingBack.not(), slideDistance = slideDistance).using(
                SizeTransform(clip = false)
            )
        }
    }) { currentState ->
        when (currentState) {
            SgCreationFlowState.TryingToAdd, SgCreationFlowState.Processing -> {
                FullscreenLoading()
            }

            is SgCreationFlowState.AlreadyHasGuard -> {
                AlreadyHasGuard(currentState, onBackClicked, viewModel::confirmMove)
            }

            is SgCreationFlowState.SmsSent -> {
                SmsSent(currentState, onBackClicked, viewModel::submitVerificationCode)
            }

            is SgCreationFlowState.Success -> {
                Success(currentState) {
                    onSuccess(viewModel.steamId.longId)
                }
            }

            is SgCreationFlowState.Error -> {
                // TODO
            }
        }
    }
}

// region State Variant - "AlreadyHasGuard"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlreadyHasGuard(
    state: SgCreationFlowState.AlreadyHasGuard,
    onBackClicked: () -> Unit,
    onMoveConfirmed: () -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(title = {}, navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
            }
        })
    }, contentWindowInsets = EmptyWindowInsets, bottomBar = {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TextButton(onClick = onBackClicked, modifier = Modifier.align(Alignment.CenterStart)) {
                Text(stringResource(R.string.guard_setup_move_cancel))
            }

            StateTextButton(
                onClick = onMoveConfirmed,
                inLoadingState = state.isProcessingRequest,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.guard_setup_move_allow))
                    Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = null)
                }
            }
        }
    }) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding), contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Update,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(id = R.string.guard_setup_move),
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = stringResource(id = R.string.guard_setup_move_desc),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
// endregion

// region State Variant - "SmsSent"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SmsSent(
    state: SgCreationFlowState.SmsSent,
    onBackClicked: () -> Unit,
    onCodeEntered: (String) -> Unit
) {
    val scope = rememberCoroutineScope()

    val rowState = remember {
        CodeRowState(5, onFinish = { crs, code ->
            if (code.length != 5) {
                crs.setErrorState(true)
            } else {
                scope.launch {
                    crs.setInactiveState(true)
                    onCodeEntered(code)
                    crs.setInactiveState(false)
                }
            }
        })
    }

    LaunchedEffect(state.returnedBecauseOfError) {
        rowState.setErrorState(state.returnedBecauseOfError)
    }

    Scaffold(topBar = {
        TopAppBar(title = {}, navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
            }
        })
    }, contentWindowInsets = EmptyWindowInsets, bottomBar = {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TextButton(onClick = onBackClicked, modifier = Modifier.align(Alignment.CenterStart)) {
                Text(stringResource(R.string.guard_setup_move_cancel))
            }

            TextButton(
                onClick = rowState::finish,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.guard_setup_move_allow))
                    Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = null)
                }
            }
        }
    }) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding), contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Sms,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(id = R.string.guard_setup_enter_code_action),
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = if (state.hint.isNotEmpty()) {
                        stringResource(id = R.string.guard_setup_enter_code_with_hint, state.hint)
                    } else {
                        stringResource(id = R.string.guard_setup_enter_code)
                    }, modifier = Modifier.padding(horizontal = 8.dp), textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(4.dp))

                CodeRow(state = rowState)
            }
        }
    }
}
// endregion

// region State Variant - "Success"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Success(state: SgCreationFlowState.Success, onBackClicked: () -> Unit) {
    Scaffold(topBar = {
        TopAppBar(title = {}, navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
            }
        })
    }, contentWindowInsets = EmptyWindowInsets, bottomBar = {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TextButton(
                onClick = onBackClicked,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Text(stringResource(R.string.guard_recovery_action))
            }
        }
    }) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding), contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(id = R.string.guard_recovery),
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                            4.dp
                        )
                    )
                ) {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.recoveryCode,
                            textAlign = TextAlign.Center,
                            modifier = Modifier,
                            fontSize = 40.sp,
                            letterSpacing = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = stringResource(id = R.string.guard_recovery_hint),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.alpha(0.7f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(id = R.string.guard_recovery_desc),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}
// endregion