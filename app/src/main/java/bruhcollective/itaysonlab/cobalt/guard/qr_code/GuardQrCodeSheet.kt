package bruhcollective.itaysonlab.cobalt.guard.qr_code

import android.Manifest
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.Router
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.guard.qr.GuardQrScannerComponent
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.ui.components.EmptyWindowInsets
import bruhcollective.itaysonlab.cobalt.ui.components.StateButton
import bruhcollective.itaysonlab.cobalt.ui.components.StateTonalButton
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import soup.compose.material.motion.animation.materialSharedAxisY
import soup.compose.material.motion.animation.rememberSlideDistance

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
internal fun GuardQrCodeSheet(
    component: GuardQrScannerComponent
) {
    val scope = rememberCoroutineScope()
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        component.registerBackListener {
            scope.launch {
                sheetState.hide()
                component.dismiss()
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = component::dismiss,
        sheetState = sheetState,
        contentWindowInsets = { EmptyWindowInsets },
        dragHandle = {},
        containerColor = Color.Black,
        contentColor = Color.White
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // 1. Handle
            BottomSheetDefaults.DragHandle(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
            )

            // 2. Content
            if (cameraPermissionState.status.isGranted) {
                val qrState by component.qrState.subscribeAsState()
                val scannedSession by component.scannedSession.subscribeAsState()
                val actionInProgress by component.actionInProgress.subscribeAsState()
                var lastScannedRect by remember { mutableStateOf(Rect(0f, 0f, 0f, 0f)) }

                CameraView(
                    modifier = Modifier.fillMaxSize(),
                    handler = { barcode ->
                        if (barcode != null) {
                            barcode.boundingBox?.let { box ->
                                lastScannedRect = box.toComposeRect()
                            }

                            component.submitQrContent(rawValue = barcode.rawValue.orEmpty())
                        } else {
                            component.submitQrEmpty()
                        }
                    }
                )

                QrExpandable(
                    state = qrState,
                    scannedBox = lastScannedRect,
                    modifier = Modifier.fillMaxSize()
                ) {
                    QrSignDialog(
                        onApprove = component::approveScannedSession,
                        onDeny = component::denyScannedSession,
                        data = scannedSession,
                        actionInProgress = actionInProgress
                    )
                }

                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                            component.dismiss()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .padding(bottom = 16.dp),
                    contentColor = Color.Black,
                    containerColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = stringResource(R.string.close)
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
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
}

@Composable
private fun QrSignDialog(
    onApprove: () -> Unit,
    onDeny: () -> Unit,
    data: GuardQrScannerComponent.ScannedSession,
    actionInProgress: GuardQrScannerComponent.Action
) {
    val slideDistance = rememberSlideDistance()

    AnimatedContent(targetState = data, transitionSpec = {
        if (data is GuardQrScannerComponent.ScannedSession.Found) {
            materialSharedAxisY(forward = true, slideDistance = slideDistance)
        } else {
            materialSharedAxisY(forward = false, slideDistance = slideDistance)
        }.using(
            SizeTransform(clip = false)
        )
    }, label = "") { dataInContainer ->
        when (dataInContainer) {
            is GuardQrScannerComponent.ScannedSession.Found -> {
                Column(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Icon(
                            imageVector = Icons.Rounded.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )

                        Text(
                            stringResource(id = R.string.guard_qr_dialog_title),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }

                    Card {
                        ListItem(
                            headlineContent = {
                                Text(stringResource(id = R.string.guard_session_info_ip))
                            }, supportingContent = {
                                Text(dataInContainer.session.ip.ifEmpty { "Unknown" })
                            }, modifier = Modifier.fillMaxWidth(), colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                            ), leadingContent = {
                                Icon(imageVector = Icons.Rounded.Router, contentDescription = null)
                            }
                        )

                        HorizontalDivider()

                        ListItem(
                            headlineContent = {
                                Text(stringResource(id = R.string.guard_session_info_loc))
                            }, supportingContent = {
                                Text("${dataInContainer.session.city}, ${dataInContainer.session.state}, ${dataInContainer.session.country}")
                            }, modifier = Modifier.fillMaxWidth(), colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                            ), leadingContent = {
                                Icon(
                                    imageVector = Icons.Rounded.LocationOn,
                                    contentDescription = null
                                )
                            }
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StateButton(
                            onClick = onApprove,
                            shape = MaterialTheme.shapes.medium,
                            inLoadingState = actionInProgress == GuardQrScannerComponent.Action.Approve,
                            enabled = actionInProgress == GuardQrScannerComponent.Action.None
                        ) {
                            Text(stringResource(id = R.string.guard_qr_dialog_action))
                        }

                        StateTonalButton(
                            onClick = onDeny,
                            shape = MaterialTheme.shapes.medium,
                            inLoadingState = actionInProgress == GuardQrScannerComponent.Action.Deny,
                            enabled = actionInProgress == GuardQrScannerComponent.Action.None
                        ) {
                            Text(stringResource(id = R.string.guard_qr_dialog_close))
                        }
                    }
                }
            }

            GuardQrScannerComponent.ScannedSession.ActionComplete -> {
                Box(
                    Modifier
                        .padding(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(32.dp)
                    )
                }
            }

            GuardQrScannerComponent.ScannedSession.Loading -> {
                Box(
                    Modifier
                        .padding(32.dp)
                        .fillMaxWidth()
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}