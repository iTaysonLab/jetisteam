package bruhcollective.itaysonlab.cobalt.signin.pages

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.sharp.SecurityUpdateGood
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.signin.tfa.TwoFactorComponent
import bruhcollective.itaysonlab.cobalt.ui.components.ResizableCircularIndicator
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import soup.compose.material.motion.animation.materialSharedAxisY
import soup.compose.material.motion.animation.rememberSlideDistance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwoFactorScreen(
    component: TwoFactorComponent
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val code by component.code.subscribeAsState()
    val codeSource by component.codeSource.subscribeAsState()
    val codeState by component.state.subscribeAsState()

    val focusRequester = remember { FocusRequester() }
    val snackState = remember { SnackbarHostState() }
    val slideDistance = rememberSlideDistance()

    val isLoading = codeState == TwoFactorComponent.State.SubmittingCode
    val canSubmitCode = codeState == TwoFactorComponent.State.CanSubmitCode
    val didErrorOccur = codeState == TwoFactorComponent.State.RpcError || codeState == TwoFactorComponent.State.WrongCode

    LaunchedEffect(didErrorOccur) {
        if (codeState == TwoFactorComponent.State.WrongCode) {
            snackState.showSnackbar(message = context.getString(R.string.auth_error_invalid_2fa))
        } else if (codeState == TwoFactorComponent.State.RpcError) {
            snackState.showSnackbar(message = context.getString(R.string.auth_error_rpc_error))
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(R.string.auth_2fa_title,))
            }, navigationIcon = {
                IconButton(
                    onClick = component::onBackClicked,
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Sharp.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier
                    )
                }
            })
        }, snackbarHost = {
            SnackbarHost(hostState = snackState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).padding(horizontal = 16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = when (codeSource) {
                    is TwoFactorComponent.CodeSource.SteamGuard -> stringResource(R.string.auth_2fa_method_guard)
                    TwoFactorComponent.CodeSource.EmailCode -> stringResource(R.string.auth_2fa_method_email)
                    TwoFactorComponent.CodeSource.EmailConfirmation -> stringResource(R.string.auth_2fa_method_email_automatic)
                },
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
            )

            if ((codeSource as? TwoFactorComponent.CodeSource.SteamGuard)?.canAutomaticallyConfirm == true) {
                Card {
                    ListItem(headlineContent = {
                        Text(stringResource(R.string.auth_2fa_method_guard_automatic))
                    }, leadingContent = {
                        Icon(imageVector = Icons.Sharp.SecurityUpdateGood, contentDescription = null)
                    }, colors = ListItemDefaults.colors(containerColor = Color.Transparent))
                }
            }

            if (codeSource != TwoFactorComponent.CodeSource.EmailConfirmation) {
                OutlinedTextField(
                    isError = didErrorOccur,
                    value = code,
                    onValueChange = component::onCodeChanged,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    label = {
                        Text(stringResource(R.string.auth_field_2fa))
                    }, keyboardActions = KeyboardActions(onDone = {
                        if (canSubmitCode) {
                            focusManager.clearFocus()
                            component.submitCode()
                        }
                    }),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .focusTarget()
                )

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        component.submitCode()
                    },
                    colors = ButtonDefaults.buttonColors(),
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    enabled = isLoading.not() && canSubmitCode,
                    contentPadding = PaddingValues(16.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = stringResource(R.string.auth_action_2fa_submit))

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
        }
    }
}