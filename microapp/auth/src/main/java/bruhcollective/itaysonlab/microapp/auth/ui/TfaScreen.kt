package bruhcollective.itaysonlab.microapp.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bruhcollective.itaysonlab.jetisteam.uikit.components.StateTextButton
import bruhcollective.itaysonlab.ksteam.models.account.AuthorizationState
import bruhcollective.itaysonlab.microapp.auth.R
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TfaScreen(
    viewModel: TfaScreenViewModel = hiltViewModel(),
    onSuccess: () -> Unit,
    onCancel: () -> Unit,
) {
    var isPolling by remember { mutableStateOf(false) }
    var currentAuthMethod by remember { mutableStateOf(AuthorizationState.AwaitingTwoFactor.ConfirmationMethod.DeviceConfirmation) }

    val authPollSignIn = viewModel.authFlow.collectAsStateWithLifecycle(initialValue = false)

    val focusManager = LocalFocusManager.current

    val (code, setCode) = rememberSaveable { mutableStateOf("") }

    val authFunc = {
        viewModel.enterCode(
            code = code,
            onSuccess = onSuccess
        )
    }

    if (authPollSignIn.value == AuthorizationState.Success) {
        LaunchedEffect(Unit) {
            onSuccess()
        }
    } else if (authPollSignIn.value is AuthorizationState.AwaitingTwoFactor) {
        (authPollSignIn.value as AuthorizationState.AwaitingTwoFactor).let {
            currentAuthMethod = it.supportedConfirmationMethods.first { m -> m != AuthorizationState.AwaitingTwoFactor.ConfirmationMethod.DeviceConfirmation }
            isPolling = it.supportedConfirmationMethods.contains(AuthorizationState.AwaitingTwoFactor.ConfirmationMethod.DeviceConfirmation)
        }
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(title = {
                Text(text = stringResource(R.string.auth_tfa_title))
            }, navigationIcon = {
                IconButton(onClick = onCancel) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                }
            })
        }, contentWindowInsets = EmptyWindowInsets, bottomBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding()
            ) {
                StateTextButton(
                    onClick = authFunc,
                    inLoadingState = viewModel.isAuthInProgress,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(R.string.login_next))
                        Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = null)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                value = code,
                onValueChange = setCode,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions {
                    focusManager.clearFocus()
                    authFunc()
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                        8.dp
                    )
                ),
                singleLine = true,
                isError = viewModel.isCodeError,
                label = { Text(stringResource(R.string.auth_tfa_hint)) },
                leadingIcon = {
                    Icon(Icons.Rounded.Message, contentDescription = null)
                },
                modifier = Modifier
                    .fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = when(currentAuthMethod) {
                    AuthorizationState.AwaitingTwoFactor.ConfirmationMethod.EmailCode -> stringResource(R.string.auth_tfa_email_code_desc)
                    AuthorizationState.AwaitingTwoFactor.ConfirmationMethod.EmailConfirmation -> stringResource(R.string.auth_tfa_email_confirm_desc)
                    else -> stringResource(R.string.auth_tfa_guard_desc)
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.weight(1f))

            if (isPolling) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(imageVector = Icons.Rounded.MobileFriendly, contentDescription = null)

                    Text(
                        text = stringResource(R.string.auth_tfa_remote),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.alpha(0.7f)
                    )
                }
            }
        }
    }
}