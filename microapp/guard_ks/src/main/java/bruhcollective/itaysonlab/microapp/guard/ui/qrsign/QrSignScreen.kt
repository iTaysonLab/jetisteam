package bruhcollective.itaysonlab.microapp.guard.ui.qrsign

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.components.RoundedPage
import bruhcollective.itaysonlab.jetisteam.uikit.components.StateButton
import bruhcollective.itaysonlab.jetisteam.uikit.components.StateTonalButton
import bruhcollective.itaysonlab.ksteam.guard.models.AwaitingSession
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.guard.R
import bruhcollective.itaysonlab.microapp.guard.ui.qrsign.camerakit.CameraView
import soup.compose.material.motion.animation.materialSharedAxisY
import soup.compose.material.motion.animation.rememberSlideDistance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrSignScreen(
    onBackClicked: () -> Unit,
    viewModel: QrSignScreenViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(stringResource(id = R.string.guard_actions_qr))
            }, navigationIcon = {
                IconButton(onClick = onBackClicked) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                }
            })
        }, contentWindowInsets = EmptyWindowInsets
    ) { padding ->
        RoundedPage(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CameraView(modifier = Modifier.fillMaxSize(), handler = viewModel::handleScannedBarcode)

            QrExpandable(
                state = viewModel.state,
                scannedBox = viewModel.lastScannedRect,
                modifier = Modifier.fillMaxSize()
            ) {
                QrSignDialog(
                    onApprove = {
                        viewModel.updateCurrentSignIn(true, onBackClicked)
                    },
                    onDeny = {
                        viewModel.updateCurrentSignIn(false, null)
                    },
                    data = viewModel.qrSessionInfo,
                    guardAccountName = viewModel.username,
                    processState = viewModel.isProcessingLogin,
                    operation = viewModel.currentOperation
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
private fun QrSignDialog(
    onApprove: () -> Unit,
    onDeny: () -> Unit,
    data: AwaitingSession?,
    guardAccountName: String,
    processState: QrSignScreenViewModel.LoginProcessState,
    operation: QrSignScreenViewModel.CurrentOperation
) {
    val slideDistance = rememberSlideDistance()

    AnimatedContent(targetState = data, transitionSpec = {
        if (data != null) {
            materialSharedAxisY(forward = true, slideDistance = slideDistance)
        } else {
            materialSharedAxisY(forward = false, slideDistance = slideDistance)
        }.using(
            SizeTransform(clip = false)
        )
    }) { dataInContainer ->
        if (dataInContainer != null) {
            Column(Modifier.padding(16.dp)) {
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    stringResource(id = R.string.guard_qr_dialog_title),
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    buildAnnotatedString {
                        append(stringResource(id = R.string.guard_as))
                        append(" ")
                        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                            append(guardAccountName)
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Card {
                    ListItem(
                        headlineText = {
                            Text(stringResource(id = R.string.guard_session_info_ip))
                        }, supportingText = {
                            Text(dataInContainer.ip.ifEmpty { "Unknown" })
                        }, modifier = Modifier.fillMaxWidth(), colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ), leadingContent = {
                            Icon(imageVector = Icons.Rounded.Router, contentDescription = null)
                        }
                    )

                    Divider(color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))

                    ListItem(
                        headlineText = {
                            Text(stringResource(id = R.string.guard_session_info_loc))
                        }, supportingText = {
                            Text("${dataInContainer.city}, ${dataInContainer.state}, ${dataInContainer.country}")
                        }, modifier = Modifier.fillMaxWidth(), colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ), leadingContent = {
                            Icon(imageVector = Icons.Rounded.LocationOn, contentDescription = null)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    StateButton(
                        onClick = onApprove,
                        shape = MaterialTheme.shapes.medium,
                        inLoadingState = operation == QrSignScreenViewModel.CurrentOperation.Approve
                    ) {
                        Text(stringResource(id = R.string.guard_qr_dialog_action))
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    StateTonalButton(
                        onClick = onDeny,
                        shape = MaterialTheme.shapes.medium,
                        inLoadingState = operation == QrSignScreenViewModel.CurrentOperation.Deny
                    ) {
                        Text(stringResource(id = R.string.guard_qr_dialog_close))
                    }
                }
            }
        } else {
            Box(
                Modifier
                    .padding(32.dp)
                    .fillMaxWidth()
            ) {
                if (processState == QrSignScreenViewModel.LoginProcessState.Success) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(32.dp)
                    )
                } else {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}