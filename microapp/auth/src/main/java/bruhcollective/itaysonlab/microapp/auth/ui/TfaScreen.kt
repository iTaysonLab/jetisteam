package bruhcollective.itaysonlab.microapp.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bruhcollective.itaysonlab.jetisteam.uikit.components.StateButton
import bruhcollective.itaysonlab.microapp.auth.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun TfaScreen(
    viewModel: TfaScreenViewModel = hiltViewModel(),
    onSuccess: () -> Unit
) {
    val authPollSignIn = viewModel.authFlow.collectAsStateWithLifecycle(initialValue = false)

    val focusManager = LocalFocusManager.current

    val (code, setCode) = rememberSaveable { mutableStateOf("") }

    val authFunc = {
        viewModel.enterCode(
            code = code,
            onSuccess = onSuccess
        )
    }

    if (authPollSignIn.value) {
        LaunchedEffect(Unit) {
            onSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.auth_tfa_title),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineLarge
            )

            Text(
                text = stringResource(R.string.auth_tfa_guard_desc),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

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
                colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)),
                singleLine = true,
                isError = viewModel.isCodeError,
                label = { Text(stringResource(R.string.auth_tfa_hint)) },
                leadingIcon = {
                    Icon(Icons.Rounded.Email, contentDescription = null)
                },
                modifier = Modifier
                    .fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (viewModel.isPollingActive) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.auth_tfa_remote),
                    fontSize = 14.sp,
                    modifier = Modifier.alpha(0.7f)
                )
            }

            Box(
                Modifier
                    .fillMaxWidth()
            ) {
                StateButton(
                    shape = RoundedCornerShape(8.dp),
                    onClick = authFunc,
                    inLoadingState = viewModel.isAuthInProgress,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Text(stringResource(R.string.login))
                }
            }
        }
    }
}