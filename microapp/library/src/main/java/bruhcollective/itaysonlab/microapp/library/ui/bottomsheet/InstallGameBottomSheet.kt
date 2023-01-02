package bruhcollective.itaysonlab.microapp.library.ui.bottomsheet

import android.text.format.Formatter
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.DiscFull
import androidx.compose.material.icons.rounded.DownloadDone
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.components.BottomSheetHandle
import bruhcollective.itaysonlab.jetisteam.uikit.components.BottomSheetHeader
import bruhcollective.itaysonlab.jetisteam.uikit.components.ResizableCircularIndicator
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.jetisteam.usecases.remote.GetRemoteInstallTargets
import bruhcollective.itaysonlab.microapp.library.R
import coil.compose.AsyncImage
import soup.compose.material.motion.animation.materialSharedAxisY
import soup.compose.material.motion.animation.rememberSlideDistance

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
internal fun InstallGameBottomSheet(
    viewModel: InstallGameViewModel = hiltViewModel(),
    onSuccess: () -> Unit,
    onOpenMachine: (Long, Long) -> Unit,
) {
    val slideDistance = rememberSlideDistance()
    val appInfo = (viewModel.state as? PageViewModel.State.Loaded)?.data?.appInfo

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = 16.dp)
            .navigationBarsPadding()
    ) {
        BottomSheetHandle(modifier = Modifier.align(Alignment.CenterHorizontally))

        BottomSheetHeader(
            text = stringResource(id = R.string.library_remote_pick_install),
            modifier = Modifier.padding(bottom = if (appInfo != null) 4.dp else 16.dp)
        )

        if (appInfo != null) {
            Row(
                Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    model = appInfo.second,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .size(24.dp),
                    placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)),
                    error = ColorPainter(MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp))
                )

                Text(
                    appInfo.first,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        AnimatedContent(targetState = viewModel.isInstalling, transitionSpec = {
            materialSharedAxisY(forward = true, slideDistance = slideDistance).using(
                SizeTransform(clip = false)
            )
        }) { isInstalling ->
            if (isInstalling) {
                Box(
                    Modifier
                        .height(72.dp)
                        .fillMaxWidth()
                ) {
                    ResizableCircularIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        indicatorSize = 24.dp,
                        strokeWidth = 2.dp
                    )
                }
            } else {
                Column {
                    Card(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                3.dp
                            )
                        )
                    ) {
                        AnimatedContent(targetState = viewModel.state, transitionSpec = {
                            materialSharedAxisY(
                                forward = true,
                                slideDistance = slideDistance
                            ).using(
                                SizeTransform(clip = false)
                            )
                        }) { data ->
                            when (data) {
                                is PageViewModel.State.Error -> {}

                                is PageViewModel.State.Loading -> {
                                    Box(
                                        Modifier
                                            .height(72.dp)
                                            .fillMaxWidth()
                                    ) {
                                        ResizableCircularIndicator(
                                            modifier = Modifier.align(Alignment.Center),
                                            indicatorSize = 24.dp,
                                            strokeWidth = 2.dp
                                        )
                                    }
                                }

                                is PageViewModel.State.Loaded -> {
                                    Column(Modifier.fillMaxWidth()) {
                                        if (data.data.targets.isNotEmpty()) {
                                            data.data.targets.forEachIndexed { index, target ->
                                                val canInstall =
                                                    target.status == GetRemoteInstallTargets.InstallTargetStatus.Ready ||
                                                            target.status == GetRemoteInstallTargets.InstallTargetStatus.InProgress ||
                                                            target.status == GetRemoteInstallTargets.InstallTargetStatus.Installed

                                                ListItem(
                                                    headlineText = {
                                                        Text(text = target.machineName)
                                                    },
                                                    supportingText = {
                                                        val ctx = LocalContext.current

                                                        val formattedFreeSpace =
                                                            remember(target.freeSpace) {
                                                                Formatter.formatFileSize(
                                                                    ctx,
                                                                    target.freeSpace
                                                                )
                                                            }

                                                        val formattedReqSpace =
                                                            remember(target.requiredSpace) {
                                                                Formatter.formatFileSize(
                                                                    ctx,
                                                                    target.requiredSpace
                                                                )
                                                            }

                                                        Text(
                                                            text = when (target.status) {
                                                                GetRemoteInstallTargets.InstallTargetStatus.Ready -> stringResource(
                                                                    id = R.string.library_remote_pick_status_ready,
                                                                    formattedReqSpace,
                                                                    formattedFreeSpace
                                                                )

                                                                GetRemoteInstallTargets.InstallTargetStatus.NotSupportedOs -> stringResource(
                                                                    id = R.string.library_remote_pick_status_os
                                                                )

                                                                GetRemoteInstallTargets.InstallTargetStatus.NoFreeSpace -> stringResource(
                                                                    id = R.string.library_remote_pick_status_space,
                                                                    formattedReqSpace,
                                                                    formattedFreeSpace
                                                                )

                                                                GetRemoteInstallTargets.InstallTargetStatus.InProgress -> stringResource(
                                                                    id = R.string.library_remote_pick_status_progress
                                                                )

                                                                GetRemoteInstallTargets.InstallTargetStatus.Installed -> stringResource(
                                                                    id = R.string.library_remote_pick_status_installed
                                                                )
                                                            }
                                                        )
                                                    },
                                                    modifier = Modifier.clickable(onClick = {
                                                        if (target.status == GetRemoteInstallTargets.InstallTargetStatus.Ready) {
                                                            viewModel.installOn(
                                                                target.machineId,
                                                                onSuccess
                                                            )
                                                        } else {
                                                            onOpenMachine(
                                                                viewModel.steamId.steamId,
                                                                target.machineId
                                                            )
                                                        }
                                                    }, enabled = canInstall),
                                                    colors = ListItemDefaults.colors(
                                                        leadingIconColor = MaterialTheme.colorScheme.primary,
                                                        containerColor = Color.Transparent,
                                                    ),
                                                    trailingContent = {
                                                        when (target.status) {
                                                            GetRemoteInstallTargets.InstallTargetStatus.Ready -> {
                                                                Icon(
                                                                    Icons.Rounded.ChevronRight,
                                                                    contentDescription = null
                                                                )
                                                            }

                                                            GetRemoteInstallTargets.InstallTargetStatus.NotSupportedOs -> {
                                                                Icon(
                                                                    Icons.Rounded.Lock,
                                                                    contentDescription = null
                                                                )
                                                            }

                                                            GetRemoteInstallTargets.InstallTargetStatus.NoFreeSpace -> {
                                                                Icon(
                                                                    Icons.Rounded.DiscFull,
                                                                    contentDescription = null
                                                                )
                                                            }

                                                            GetRemoteInstallTargets.InstallTargetStatus.InProgress -> {
                                                                ResizableCircularIndicator(
                                                                    color = LocalContentColor.current,
                                                                    indicatorSize = 24.dp,
                                                                    strokeWidth = 2.dp
                                                                )
                                                            }

                                                            GetRemoteInstallTargets.InstallTargetStatus.Installed -> {
                                                                Icon(
                                                                    Icons.Rounded.DownloadDone,
                                                                    contentDescription = null
                                                                )
                                                            }
                                                        }
                                                    }
                                                )

                                                if (index != data.data.targets.lastIndex) {
                                                    Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                                                }
                                            }
                                        } else {
                                            Text(
                                                text = stringResource(id = R.string.library_remote_pick_empty),
                                                modifier = Modifier
                                                    .align(Alignment.CenterHorizontally)
                                                    .padding(16.dp),
                                                textAlign = TextAlign.Center,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}