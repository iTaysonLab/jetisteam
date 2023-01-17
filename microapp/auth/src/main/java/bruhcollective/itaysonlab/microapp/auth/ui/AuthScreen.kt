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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.*
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.components.StateTextButton
import bruhcollective.itaysonlab.microapp.auth.R

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun AuthScreen(
    viewModel: AuthScreenViewModel = hiltViewModel(),
    onProceedToNextStep: () -> Unit,
    onBackClicked: () -> Unit
) {
    val autofill = LocalAutofill.current
    val focusManager = LocalFocusManager.current

    val (username, setUsername) = rememberSaveable { mutableStateOf("") }
    val (password, setPassword) = rememberSaveable { mutableStateOf("") }
    val (usernameFocusRequester, passwordFocusRequester) = remember { FocusRequester.createRefs() }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val authFunc = {
        viewModel.auth(
            username = username,
            password = password,
            onSuccess = onProceedToNextStep
        )
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(title = {
                Text(text = stringResource(id = R.string.new_onboarding_sign))
            }, navigationIcon = {
                IconButton(onClick = onBackClicked) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                }
            })
        }, bottomBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding()) {
                StateTextButton(
                    onClick = authFunc,
                    inLoadingState = viewModel.isAuthInProgress,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
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
            Card(colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            )) {
                Column(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(imageVector = Icons.Rounded.Warning, contentDescription = null)
                    Text(text = stringResource(id = R.string.disclaimers), style = MaterialTheme.typography.headlineSmall)
                    Text(text = stringResource(id = R.string.auth_disclaimer_text), style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Autofill(
                autofillTypes = listOf(AutofillType.EmailAddress, AutofillType.Username),
                onFill = setUsername
            ) { autofillNode ->
                OutlinedTextField(
                    value = username,
                    onValueChange = setUsername,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions {
                        passwordFocusRequester.requestFocus()
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)),
                    singleLine = true,
                    isError = viewModel.isEmailError,
                    label = { Text(stringResource(R.string.email)) },
                    leadingIcon = {
                        Icon(Icons.Rounded.Email, contentDescription = null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusTarget()
                        .focusRequester(usernameFocusRequester)
                        .onFocusChanged {
                            autofill?.apply {
                                if (it.isFocused) {
                                    requestAutofillForNode(autofillNode)
                                } else {
                                    cancelAutofillForNode(autofillNode)
                                }
                            }
                        }
                        .focusProperties { next = passwordFocusRequester }, shape = MaterialTheme.shapes.medium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Autofill(
                autofillTypes = listOf(AutofillType.Password),
                onFill = setPassword
            ) { autofillNode ->
                OutlinedTextField(
                    value = password,
                    onValueChange = setPassword,
                    label = { Text(stringResource(R.string.password)) },
                    singleLine = true,
                    isError = viewModel.isPasswordError,
                    leadingIcon = {
                        Icon(Icons.Rounded.Password, contentDescription = null)
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)),
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusTarget()
                        .focusRequester(passwordFocusRequester)
                        .onFocusChanged {
                            autofill?.apply {
                                if (it.isFocused) {
                                    requestAutofillForNode(autofillNode)
                                } else {
                                    cancelAutofillForNode(autofillNode)
                                }
                            }
                        }
                        .focusProperties { previous = usernameFocusRequester },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions {
                        focusManager.clearFocus()
                        authFunc()
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisible = !passwordVisible }
                        ) {
                            if (passwordVisible)
                                Icon(
                                    Icons.Rounded.Visibility,
                                    stringResource(R.string.hide_password)
                                )
                            else
                                Icon(
                                    Icons.Rounded.VisibilityOff,
                                    stringResource(R.string.show_password)
                                )
                        }
                    }, shape = MaterialTheme.shapes.medium
                )
            }
        }
    }
}

// TODO migrate from Composable wrapper to modifier
@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Autofill(
    autofillTypes: List<AutofillType>,
    onFill: ((String) -> Unit),
    content: @Composable (AutofillNode) -> Unit
) {
    val autofillNode = AutofillNode(onFill = onFill, autofillTypes = autofillTypes)

    val autofillTree = LocalAutofillTree.current
    autofillTree += autofillNode

    Box(
        modifier = Modifier.onGloballyPositioned {
            autofillNode.boundingBox = it.boundsInWindow()
        }
    ) {
        content(autofillNode)
    }
}