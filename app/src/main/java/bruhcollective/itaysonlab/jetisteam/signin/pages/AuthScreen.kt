package bruhcollective.itaysonlab.jetisteam.signin.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Gamepad
import androidx.compose.material.icons.sharp.Key
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.signin.auth.AuthComponent
import bruhcollective.itaysonlab.jetisteam.ui.components.CobaltDivider
import bruhcollective.itaysonlab.jetisteam.ui.components.CobaltTextField
import bruhcollective.itaysonlab.jetisteam.ui.components.StateInlineMonoButton
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    component: AuthComponent
) {
    var focusWasRequested by rememberSaveable {
        mutableStateOf(false)
    }

    val username by component.username.subscribeAsState()
    val password by component.password.subscribeAsState()
    val state by component.signInState.subscribeAsState()

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val isLoading = state == AuthComponent.SignInState.Processing
    val canSignIn = state == AuthComponent.SignInState.CanSignIn

    LaunchedEffect(focusWasRequested) {
        if (focusWasRequested.not()) {
            focusRequester.requestFocus()
            focusWasRequested = true
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .imePadding()
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .verticalScroll(rememberScrollState())
        ) {
            Icon(
                imageVector = Icons.Sharp.Gamepad,
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .size(64.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Welcome to Cobalt!",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Elevate your Steam experience.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            CobaltDivider()

            CobaltTextField(
                value = username,
                onValueChange = component::onUsernameChanged,
                label = "Username",
                placeholder = "example",
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Text
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .focusRequester(focusRequester)
            )

            CobaltDivider()

            CobaltTextField(
                value = password,
                onValueChange = component::onPasswordChanged,
                label = "Password",
                placeholder = "••••••••••",
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Password
                ), visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )

            CobaltDivider()

            StateInlineMonoButton(
                icon = {
                    Icon(
                        imageVector = Icons.Sharp.Key,
                        contentDescription = null
                    )
                },
                title = "Sign in with Steam",
                onClick = {
                    focusManager.clearFocus()
                    component.trySignIn()
                },
                isLoading = isLoading,
                enabled = canSignIn
            )

            /* CobaltDivider()

            InlineMonoButton(
                icon = {
                    Icon(
                        imageVector = Icons.Sharp.QrCode,
                        contentDescription = null
                    )
                },
                title = "Show QR code",
                onClick = {},
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
            ) */

            CobaltDivider()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = buildAnnotatedString {
                    append("By signing in, you agree to ")

                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("Steam's SLA")
                    }

                    append(" and ")

                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("Privacy Policy")
                    }

                    append(", as well as with ")

                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("Cobalt's Privacy Policy")
                    }

                    append(".")

                    appendLine()
                    appendLine()

                    append("Cobalt is an unofficial Steam Network client and not affiliated, created, endorsed or connected to Valve.")
                },
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}