package bruhcollective.itaysonlab.microapp.guard.ui.variants.pages

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetisteam.uikit.components.RoundedPage
import bruhcollective.itaysonlab.jetisteam.uikit.page.FullscreenError
import bruhcollective.itaysonlab.jetisteam.uikit.page.FullscreenLoading
import bruhcollective.itaysonlab.jetisteam.util.DateUtil
import bruhcollective.itaysonlab.ksteam.guard.models.CodeModel
import bruhcollective.itaysonlab.ksteam.guard.models.ConfirmationListState
import bruhcollective.itaysonlab.ksteam.guard.models.MobileConfirmationItem
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.guard.R
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import soup.compose.material.motion.animation.materialSharedAxisY
import soup.compose.material.motion.animation.rememberSlideDistance

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun GuardCodeAndConfirmationsPage(
    modifier: Modifier,
    code: CodeModel,
    connectedToSteam: Boolean,
    confirmationState: ConfirmationListState,
    onSignWithQrClicked: () -> Unit,
    onRecoveryClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onCopyClicked: () -> Unit,
    onConfirmationClicked: (MobileConfirmationItem) -> Unit
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA, onPermissionResult = { granted ->
        if (granted) {
            onSignWithQrClicked()
        }
    })

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
        val fabDiff by animateDpAsState(
            targetValue = if (connectedToSteam) 0.dp else 96.dp,
            animationSpec = spring(stiffness = 1000f)
        )

        FloatingActionButton(onClick = onSignWithQrClicked, modifier = Modifier.offset(y = fabDiff)) {
            Icon(imageVector = Icons.Rounded.QrCodeScanner, contentDescription = null)
        }
    }, contentWindowInsets = EmptyWindowInsets) {
        Column(modifier) {
            CodeBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onCopyClicked = onCopyClicked,
                code = code, onDeleteClicked = onDeleteClicked, onRecoveryClicked = onRecoveryClicked, connectedToSteam = connectedToSteam
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            RoundedPage(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()) {
                when (confirmationState) {
                    is ConfirmationListState.Loading -> {
                        FullscreenLoading()
                    }

                    is ConfirmationListState.NetworkError -> {
                        FullscreenError(onReload = { /*TODO*/ }, exception = confirmationState.e)
                    }

                    is ConfirmationListState.Error -> {
                        FullscreenPlaceholder(title = confirmationState.message, text = confirmationState.detail)
                    }

                    is ConfirmationListState.Success -> {
                        if (confirmationState.conf.isNotEmpty()) {
                            LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(confirmationState.conf) {
                                    ConfirmationCard(confirmation = it, onClick = {
                                        onConfirmationClicked(it)
                                    })
                                }
                            }
                        } else {
                            FullscreenPlaceholder(
                                title = stringResource(id = R.string.guard_confirmations_empty),
                                text = stringResource(id = R.string.guard_confirmations_empty_desc),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FullscreenPlaceholder(
    title: String,
    text: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Text(
                text = text,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CodeBox(
    modifier: Modifier,
    code: CodeModel,
    connectedToSteam: Boolean,
    onCopyClicked: () -> Unit,
    onRecoveryClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
) {
    var isMenuShown by remember { mutableStateOf(false) }

    val background by animateColorAsState(targetValue = if (isMenuShown) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceColorAtElevation(
        8.dp
    ))

    val progressBackground by animateColorAsState(targetValue = if (isMenuShown) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)

    val intProgress by animateFloatAsState(targetValue = code.progressRemaining, animationSpec = tween(1000, easing = LinearEasing))

    val slideDistance = rememberSlideDistance()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = background
        ), modifier = modifier
    ) {
        AnimatedContent(targetState = isMenuShown, transitionSpec = {
            materialSharedAxisY(forward = targetState, slideDistance = slideDistance).using(SizeTransform(clip = false))
        }) {
            if (it) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 21.dp)) {
                    IconButton(onClick = {
                        isMenuShown = false
                    }, colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ), modifier = Modifier) {
                        Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
                    }

                    Spacer(Modifier.weight(1f))

                    IconButton(onClick = onRecoveryClicked, colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ), modifier = Modifier) {
                        Icon(imageVector = Icons.Rounded.Help, contentDescription = null)
                    }

                    Spacer(Modifier.weight(1f))

                    IconButton(onClick = onDeleteClicked, colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ), modifier = Modifier) {
                        Icon(imageVector = Icons.Rounded.DeleteForever, contentDescription = null)
                    }
                }
            } else {
                Column {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)) {
                        IconButton(onClick = {
                            isMenuShown = true
                        }, colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        ), modifier = Modifier.align(Alignment.CenterStart), enabled = connectedToSteam) {
                            Icon(imageVector = Icons.Rounded.Settings, contentDescription = null)
                        }

                        AnimatedContent(targetState = code.code, transitionSpec = {
                            if (initialState.isEmpty()) {
                                EnterTransition.None with ExitTransition.None
                            } else {
                                materialSharedAxisY(forward = true, slideDistance = slideDistance)
                            }.using(
                                SizeTransform(clip = false)
                            )
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)) { code ->
                            Text(
                                text = code,
                                textAlign = TextAlign.Center,
                                fontSize = 40.sp,
                                letterSpacing = 12.sp,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }

                        IconButton(onCopyClicked, colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        ), modifier = Modifier.align(Alignment.CenterEnd)) {
                            Icon(imageVector = Icons.Rounded.ContentCopy, contentDescription = null)
                        }
                    }

                    LinearProgressIndicator(
                        progress = intProgress,
                        modifier = Modifier.fillMaxWidth(),
                        trackColor = progressBackground
                    )
                }
            }
        }
    }
}

@Composable
private fun ConfirmationCard(
    confirmation: MobileConfirmationItem,
    onClick: () -> Unit
) {
    val formattedDate = remember(confirmation.creationTime) {
        DateUtil.formatDateTimeToLocale(confirmation.creationTime * 1000L)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = confirmation.icon,
                contentDescription = null,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .size(64.dp),
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp))
            )

            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = confirmation.typeName, fontSize = 13.sp)
                    Text(text = formattedDate, modifier = Modifier.alpha(0.7f), fontSize = 13.sp)
                }

                Text(
                    text = confirmation.headline,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(text = remember(confirmation.summary) {
                    confirmation.summary.joinToString(separator = "\n")
                }, fontSize = 13.sp, lineHeight = 18.sp)
            }
        }
    }
}