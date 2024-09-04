package bruhcollective.itaysonlab.cobalt.signin.pages

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.signin.auth.AuthComponent
import bruhcollective.itaysonlab.cobalt.ui.components.ResizableCircularIndicator
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import soup.compose.material.motion.animation.materialSharedAxisY
import soup.compose.material.motion.animation.rememberSlideDistance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    component: AuthComponent
) {
    val context = LocalContext.current
    val slideDistance = rememberSlideDistance()

    val username by component.username.subscribeAsState()
    val password by component.password.subscribeAsState()
    val state by component.signInState.subscribeAsState()

    val (usernameRequester, passwordRequester) = remember { FocusRequester.createRefs() }
    val focusManager = LocalFocusManager.current

    val isLoading = state == AuthComponent.SignInState.Processing
    val canSignIn = state == AuthComponent.SignInState.CanSignIn
    val didErrorOccur = state == AuthComponent.SignInState.InvalidInformation || state == AuthComponent.SignInState.RpcError

    var isPasswordVisible by remember { mutableStateOf(false) }
    val snackState = remember { SnackbarHostState() }

    LaunchedEffect(didErrorOccur) {
        if (state == AuthComponent.SignInState.InvalidInformation) {
            snackState.showSnackbar(message = context.getString(R.string.auth_error_invalid_data))
        } else if (state == AuthComponent.SignInState.RpcError) {
            snackState.showSnackbar(message = context.getString(R.string.auth_error_rpc_error))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = stringResource(
                        R.string.auth_header_title,
                        stringResource(R.string.app_name)
                    )
                )
            })
        }, snackbarHost = {
            SnackbarHost(hostState = snackState)
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).padding(innerPadding).verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    isError = didErrorOccur,
                    value = username,
                    onValueChange = component::onUsernameChanged,
                    label = {
                        Text(stringResource(R.string.auth_field_username))
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(usernameRequester)
                        .focusProperties { next = passwordRequester }
                        .focusTarget()
                )

                OutlinedTextField(
                    isError = didErrorOccur,
                    value = password,
                    onValueChange = component::onPasswordChanged,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    label = {
                        Text(stringResource(R.string.auth_field_password))
                    }, keyboardActions = KeyboardActions(onDone = {
                        if (canSignIn) {
                            focusManager.clearFocus()
                            component.trySignIn()
                        }
                    }), trailingIcon = {
                        IconButton(onClick = {
                            isPasswordVisible = !isPasswordVisible
                        }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                                contentDescription = stringResource(id = if (isPasswordVisible) R.string.auth_hide_password else R.string.auth_show_password)
                            )
                        }
                    },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(passwordRequester)
                        .focusProperties {
                            previous = usernameRequester
                        }
                        .focusTarget()
                )

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        component.trySignIn()
                    },
                    colors = ButtonDefaults.buttonColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    enabled = isLoading.not() && canSignIn,
                    contentPadding = PaddingValues(16.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = stringResource(R.string.auth_action_sign_in)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    AnimatedContent(targetState = isLoading, transitionSpec = {
                        materialSharedAxisY(forward = true, slideDistance = slideDistance)
                    }, label = "") { showSpinner ->
                        if (showSpinner) {
                            ResizableCircularIndicator(
                                indicatorSize = 24.dp,
                                strokeWidth = 2.dp,
                                color = LocalContentColor.current
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                contentDescription = null,
                                tint = LocalContentColor.current
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.auth_disclaimer_p0))

                    append(" ")
                    withLink(
                        link = LinkAnnotation.Url(
                            url = "https://store.steampowered.com/subscriber_agreement/",
                            styles = TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.primary))
                        )
                    ) {
                        append(stringResource(R.string.auth_disclaimer_p1))
                    }
                    append(" ")

                    append(stringResource(R.string.auth_disclaimer_p2))

                    append(" ")
                    withLink(
                        link = LinkAnnotation.Url(
                            url = "https://store.steampowered.com/privacy_agreement/",
                            styles = TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.primary))
                        )
                    ) {
                        append(stringResource(R.string.auth_disclaimer_p3))
                    }

                    append(stringResource(R.string.auth_disclaimer_p4))

                    append(" ")
                    withLink(
                        link = LinkAnnotation.Url(
                            url = "https://itaysonlab.github.io/Jetisteam/privacy_policy.html",
                            styles = TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.primary))
                        )
                    ) {
                        append(stringResource(R.string.auth_disclaimer_p5))
                    }

                    append(stringResource(R.string.auth_disclaimer_p6))
                },
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }
    }
}