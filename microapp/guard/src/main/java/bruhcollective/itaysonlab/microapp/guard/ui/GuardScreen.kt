package bruhcollective.itaysonlab.microapp.guard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.components.StateButton
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.guard.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GuardScreen(
    viewModel: GuardViewModel = hiltViewModel(),
    onMoveClicked: (Long) -> Unit,
    onAddClicked: (Long) -> Unit,
) {
    val scope = rememberCoroutineScope()

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(text = stringResource(id = R.string.guard))
            }
        )
    }, contentWindowInsets = EmptyWindowInsets) { innerPadding ->
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

        when (viewModel.state) {
            is GuardViewModel.GuardState.Available -> TODO()

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