package bruhcollective.itaysonlab.microapp.guard.ui.variants

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.ksteam.guard.models.CodeModel
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.guard.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import soup.compose.material.motion.animation.materialSharedAxisY
import soup.compose.material.motion.animation.rememberSlideDistance

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
internal fun GuardMainScreen(
    modifier: Modifier,
    code: CodeModel,
    accountName: String,
    onSignWithQrClicked: () -> Unit,
    onMoreSettingsClicked: () -> Unit,
    onCopyClicked: () -> Unit
) {
    val intProgress = animateFloatAsState(targetValue = code.progressRemaining, animationSpec = tween(1000, easing = LinearEasing))

    val slideDistance = rememberSlideDistance()
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    var showPermissionAlert by remember { mutableStateOf(false) }

    if (showPermissionAlert) {
        AlertDialog(onDismissRequest = { showPermissionAlert = false }, icon = {
            Icon(imageVector = Icons.Rounded.PhotoCamera, contentDescription = null)
        }, title = {
            Text(stringResource(id = R.string.guard_camera_permission_title))
        }, text = {
            Text(stringResource(id = R.string.guard_camera_permission_text))
        }, confirmButton = {
            TextButton(onClick = {
                cameraPermissionState.launchPermissionRequest()
                showPermissionAlert = false
            }) {
                Text(stringResource(id = R.string.guard_camera_permission_allow))
            }
        }, dismissButton = {
            TextButton(onClick = { showPermissionAlert = false }) {
                Text(stringResource(id = R.string.guard_camera_permission_dismiss))
            }
        })
    }

    Scaffold(modifier, floatingActionButton = {
        FloatingActionButton(onClick = onSignWithQrClicked) {
            Icon(imageVector = Icons.Rounded.QrCodeScanner, contentDescription = null)
        }
    }, contentWindowInsets = EmptyWindowInsets) {
        Column(modifier) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                        8.dp
                    )
                ), modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column {
                    Box(Modifier.fillMaxWidth()) {
                        AnimatedContent(targetState = code.code, transitionSpec = {
                            if (initialState.isEmpty()) {
                                EnterTransition.None with ExitTransition.None
                            } else {
                                materialSharedAxisY(forward = true, slideDistance = slideDistance)
                            }.using(
                                SizeTransform(clip = false)
                            )
                        }, modifier = Modifier.fillMaxWidth()) { code ->
                            Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = code,
                                    fontSize = 40.sp,
                                    letterSpacing = 12.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                )

                                IconButton(onCopyClicked, colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = MaterialTheme.colorScheme.primary
                                )) {
                                    Icon(imageVector = Icons.Rounded.ContentCopy, contentDescription = null)
                                }
                            }
                        }

                        FilledIconButton(onMoreSettingsClicked, colors = IconButtonDefaults.filledTonalIconButtonColors(), modifier = Modifier.align(Alignment.CenterEnd).padding(horizontal = 16.dp)) {
                            Icon(imageVector = Icons.Rounded.Settings, contentDescription = null)
                        }
                    }

                    LinearProgressIndicator(
                        progress = intProgress.value,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Text(
                text = "Confirmations",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}