package bruhcollective.itaysonlab.microapp.library.ui.remote.pages

import android.text.format.Formatter
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.DownloadDone
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.jetisteam.uikit.components.ResizableCircularIndicator
import bruhcollective.itaysonlab.jetisteam.uikit.page.FullscreenPlaceholder
import bruhcollective.itaysonlab.jetisteam.usecases.remote.GetRemoteMachineSummary
import bruhcollective.itaysonlab.jetisteam.usecases.remote.SetRemoteMachineDownloadState
import bruhcollective.itaysonlab.jetisteam.util.CdnUrlUtil
import bruhcollective.itaysonlab.microapp.library.R
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.BlurTransformation
import soup.compose.material.motion.animation.materialSharedAxisY
import soup.compose.material.motion.animation.rememberSlideDistance
import steam.clientcomm.CClientComm_GetClientAppList_Response_AppData

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun QueuePage(
    queueState: GetRemoteMachineSummary.RemoteMachineQueue,
    currentlyOperatingWith: Int,
    onClick: (Int, SetRemoteMachineDownloadState.Command) -> Unit
) {
    if (queueState.activeDownload == null && queueState.queueCompleted.isEmpty() && queueState.queueWaiting.isEmpty()) {
        FullscreenPlaceholder(
            icon = Icons.Rounded.DownloadDone,
            title = stringResource(
                R.string.library_remote_library_queue_empty
            ),
            text = stringResource(
                R.string.library_remote_library_queue_empty_text
            )
        )
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            if (queueState.activeDownload != null) {
                item {
                    val paused = queueState.activeDownload!!.download_paused == true

                    QueueGameCard(
                        queueState.activeDownload!!,
                        icon = if (paused) {
                            Icons.Rounded.PlayArrow
                        } else {
                            Icons.Rounded.Pause
                        },
                        onDownloadClicked = {
                            onClick(queueState.activeDownload?.appid ?: 0, if (paused) {
                                SetRemoteMachineDownloadState.Command.CurrentResume
                            } else {
                                SetRemoteMachineDownloadState.Command.CurrentPause
                            })
                        }, operatingAppId = currentlyOperatingWith
                    )
                }
            }

            if (queueState.queueWaiting.isNotEmpty()) {
                stickyHeader {
                    StickyHeader(
                        type = R.string.library_remote_category_next,
                        counter = queueState.queueWaiting.size
                    )
                }

                items(queueState.queueWaiting) { app ->
                    QueueGameCard(app, onDownloadClicked = {
                        onClick(app.appid ?: 0, SetRemoteMachineDownloadState.Command.QueueToTop)
                    }, operatingAppId = currentlyOperatingWith)
                }
            }

            if (queueState.queueCompleted.isNotEmpty()) {
                stickyHeader {
                    StickyHeader(
                        type = R.string.library_remote_category_done,
                        counter = queueState.queueCompleted.size
                    )
                }

                items(queueState.queueCompleted) { app ->
                    QueueGameCard(app, operatingAppId = currentlyOperatingWith)
                }
            }
        }
    }
}

@Composable
private fun StickyHeader(
    @StringRes type: Int,
    counter: Int
) {
    Row(
        Modifier
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = stringResource(id = type), color = MaterialTheme.colorScheme.onSurface)
        Text(text = counter.toString(), color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
private fun QueueGameCard(
    app: CClientComm_GetClientAppList_Response_AppData,
    icon: ImageVector = Icons.Rounded.Download,
    operatingAppId: Int,
    onDownloadClicked: () -> Unit = {},
) {
    val slideDistance = rememberSlideDistance()
    val ctx = LocalContext.current

    val formattedBytesDownloaded = remember(app.bytes_downloaded) {
        Formatter.formatFileSize(ctx, app.bytes_downloaded ?: 0L)
    }

    val formattedBytesToDownload = remember(app.bytes_to_download) {
        Formatter.formatFileSize(ctx, app.bytes_to_download ?: 0L)
    }

    val downloadProgress = remember(app.bytes_downloaded, app.bytes_to_download) {
        ((app.bytes_downloaded?.toFloat() ?: 1f) / (app.bytes_to_download?.toFloat()
            ?: 1f)).coerceIn(0f..1f)
    }

    val formattedEta = remember(app.estimated_seconds_remaining) {
        val etaSec = app.estimated_seconds_remaining ?: 0
        String.format("%02d:%02d", (etaSec % 3600) / 60, (etaSec % 60))
    }

    val isDownloaded =
        app.bytes_downloaded == app.bytes_to_download && app.bytes_staged == app.bytes_to_stage

    Surface(
        tonalElevation = 8.dp, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        val completedColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
            .compositeOver(MaterialTheme.colorScheme.primary)
            .copy(
                alpha = if (app.download_paused == true || (app.queue_position ?: 0) > 0) 0.5f else 1f
            )

        Box(Modifier.height(IntrinsicSize.Min)) {
            val bg = remember(app.appid) {
                CdnUrlUtil.buildAppUrl(app.appid ?: 0, "portrait.png")
            }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(bg)
                    .transformations(
                        BlurTransformation(LocalContext.current, 20f, 4f)
                    )
                    .build(), contentDescription = null, modifier = Modifier
                    .fillMaxSize()
                    .drawWithContent {
                        drawContent()
                        drawRect(color = Color.Black.copy(alpha = 0.5f))
                    }, contentScale = ContentScale.Crop
            )

            ListItem(
                headlineText = {
                    Text(text = app.app.orEmpty())
                }, supportingText = {
                    if (isDownloaded) {
                        Text(text = formattedBytesDownloaded)
                    } else {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = formattedBytesDownloaded,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            if (app.queue_position == 0 && app.download_paused != true) {
                                Text(
                                    text = stringResource(
                                        id = R.string.library_remote_progress_right_eta,
                                        formattedBytesToDownload,
                                        formattedEta
                                    ), color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                Text(
                                    text = stringResource(
                                        id = R.string.library_remote_progress_right,
                                        formattedBytesToDownload
                                    ), color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }, colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent
                ), trailingContent = {
                    if (isDownloaded.not()) {
                        Box(
                            Modifier
                                .clip(MaterialTheme.shapes.medium)
                                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), MaterialTheme.shapes.medium)
                                .clickable(enabled = operatingAppId == 0, onClick = onDownloadClicked)
                                .size(48.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            AnimatedContent(targetState = operatingAppId == app.appid, transitionSpec = {
                                materialSharedAxisY(slideDistance = slideDistance, forward = true).using(SizeTransform(clip = false))
                            }) { operating ->
                                if (operating) {
                                    ResizableCircularIndicator(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        indicatorSize = 24.dp,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }, modifier = Modifier
                    .drawBehind {
                        if (isDownloaded.not()) {
                            drawRect(
                                completedColor,
                                size = size.copy(width = size.width * downloadProgress)
                            )
                        }
                    }
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
    }
}