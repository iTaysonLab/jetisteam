package bruhcollective.itaysonlab.jetisteam.guard.qr_code

import android.Manifest
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.guard.qr.GuardQrScannerComponent
import bruhcollective.itaysonlab.jetisteam.R
import bruhcollective.itaysonlab.jetisteam.ui.components.EmptyWindowInsets
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
internal fun GuardQrCodeSheet(
    component: GuardQrScannerComponent
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    ModalBottomSheet(
        onDismissRequest = component::dismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        contentWindowInsets = { EmptyWindowInsets },
        dragHandle = {},
        containerColor = Color.Black,
        contentColor = Color.White
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // 1. Handle
            BottomSheetDefaults.DragHandle(modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding())

            // 2. Content
            if (cameraPermissionState.status.isGranted) {
                CameraView(
                    modifier = Modifier.fillMaxSize(),
                    handler = viewModel::handleScannedBarcode
                )

                QrExpandable(
                    state = viewModel.state,
                    scannedBox = viewModel.lastScannedRect,
                    modifier = Modifier.fillMaxSize()
                ) {
                    QrSignDialog(
                        onApprove = {
                            viewModel.updateCurrentSignIn(true, onFinish)
                        },
                        onDeny = {
                            viewModel.updateCurrentSignIn(false, null)
                        },
                        data = viewModel.qrSessionInfo,
                        guardAccountName = viewModel.username,
                        processState = viewModel.isProcessingLogin,
                        operation = viewModel.эcurrentOperation
                    )
                }
            } else {
                Column(
                    modifier = Modifier.align(Alignment.Center).fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PhotoCamera,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp)
                    )

                    Text(
                        stringResource(id = R.string.guard_camera_permission_title),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        stringResource(id = R.string.guard_camera_permission_text),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )

                    TextButton(onClick = cameraPermissionState::launchPermissionRequest) {
                        Text(stringResource(id = R.string.guard_camera_permission_allow))
                    }
                }
            }
        }
    }

    /*Scaffold(
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
                        viewModel.updateCurrentSignIn(true, onFinish)
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
    }*/
}

/*@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
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
                        headlineContent = {
                            Text(stringResource(id = R.string.guard_session_info_ip))
                        }, supportingContent = {
                            Text(dataInContainer.ip.ifEmpty { "Unknown" })
                        }, modifier = Modifier.fillMaxWidth(), colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ), leadingContent = {
                            Icon(imageVector = Icons.Rounded.Router, contentDescription = null)
                        }
                    )

                    Divider(color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))

                    ListItem(
                        headlineContent = {
                            Text(stringResource(id = R.string.guard_session_info_loc))
                        }, supportingContent = {
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
}*/