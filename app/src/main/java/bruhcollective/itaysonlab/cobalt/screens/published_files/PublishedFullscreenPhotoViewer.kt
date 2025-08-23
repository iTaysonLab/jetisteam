package bruhcollective.itaysonlab.cobalt.screens.published_files

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.text.format.Formatter
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DatasetLinked
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.FileDownload
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.PermMedia
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.ext.GuardUtils
import bruhcollective.itaysonlab.cobalt.published_files.PublishedFullscreenPhotoViewerComponent
import bruhcollective.itaysonlab.cobalt.ui.components.M3ECardListItem
import coil.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.launch
import soup.compose.material.motion.animation.materialSharedAxisY
import soup.compose.material.motion.animation.rememberSlideDistance
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun ModalPublishedFullscreenPhotoViewer(
    onDismiss: () -> Unit,
    component: PublishedFullscreenPhotoViewerComponent
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = null,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        sheetGesturesEnabled = false,
        contentWindowInsets = { WindowInsets() }
    ) {
        PublishedFullscreenPhotoViewer(onDismiss, component)
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class,
    ExperimentalTime::class
)
@Composable
internal fun PublishedFullscreenPhotoViewer(
    onDismiss: () -> Unit,
    component: PublishedFullscreenPhotoViewerComponent
) {
    var shareContextMenu by remember { mutableStateOf(false) }
    var localPermissionDialog by remember { mutableStateOf(false) }

    val saveToGalleryState by component.saveToGalleryState.subscribeAsState()
    val saveToGalleryProgress by component.saveToGalleryProgress.subscribeAsState()

    val saveToGalleryAnimatedProgress by animateFloatAsState(targetValue = saveToGalleryProgress)

    val localContext = LocalContext.current
    val scope = rememberCoroutineScope()
    val slideDistance = rememberSlideDistance()

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = true
        )
    )

    BackHandler(scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded) {
        scope.launch {
            scaffoldState.bottomSheetState.partialExpand()
        }
    }

    LaunchedEffect(saveToGalleryState) {
        if (saveToGalleryState == PublishedFullscreenPhotoViewerComponent.SaveToGalleryState.PermissionRequired) {
            localPermissionDialog = true
        }
    }

    if (localPermissionDialog) {
        AlertDialog(
            icon = {
                Icon(Icons.Rounded.PermMedia, contentDescription = null)
            },
            title = {
                Text(stringResource(R.string.files_save_permission))
            }, text = {
                Text(stringResource(R.string.files_save_permission_text))
            }, confirmButton = {
                TextButton(onClick = {
                    localPermissionDialog = false

                    localContext.startActivity(
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also { i ->
                            i.data = Uri.fromParts("package", localContext.packageName, null)
                        }
                    )
                }) {
                    Text(stringResource(R.string.files_save_permission_action))
                }
            }, dismissButton = {
                TextButton(onClick = { localPermissionDialog = false }) {
                    Text(stringResource(R.string.guard_revoke_alert_dismiss))
                }
            }, onDismissRequest = { localPermissionDialog = false }
        )
    }

    val navigationBarsDp = with(LocalDensity.current) { WindowInsets.systemBars.getBottom(this).toDp() }

    BottomSheetScaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Rounded.Close, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = Color.White
                ),
            )
        },
        contentColor = Color.White,
        containerColor = Color.Black,
        scaffoldState = scaffoldState,
        sheetPeekHeight = 48.dp + navigationBarsDp,
        sheetContent = {
            LazyColumn(Modifier.padding(vertical = navigationBarsDp)) {
                item {
                    ListItem(leadingContent = {
                        Icon(Icons.Rounded.GridView, contentDescription = stringResource(R.string.files_info_app))
                    }, headlineContent = {
                        Text(component.infoSheetContent.appName)
                    }, supportingContent = {
                        Text(stringResource(R.string.files_info_app))
                    }, colors = ListItemDefaults.colors(containerColor = Color.Transparent))
                }

                item {
                    ListItem(leadingContent = {
                        Icon(Icons.Rounded.CalendarToday, contentDescription = stringResource(R.string.files_info_date))
                    }, headlineContent = {
                        Text(text = remember(component.infoSheetContent.createdAt) {
                            GuardUtils.formatDateTimeToLocaleFull(component.infoSheetContent.createdAt.toEpochMilliseconds())
                        })
                    }, supportingContent = {
                        Text(stringResource(R.string.files_info_date))
                    }, colors = ListItemDefaults.colors(containerColor = Color.Transparent))
                }

                item {
                    ListItem(leadingContent = {
                        Icon(Icons.Rounded.Image, contentDescription = stringResource(R.string.files_info_resolution))
                    }, headlineContent = {
                        Text("${component.infoSheetContent.resolutionX}x${component.infoSheetContent.resolutionY}")
                    }, supportingContent = {
                        Text(stringResource(R.string.files_info_resolution))
                    }, colors = ListItemDefaults.colors(containerColor = Color.Transparent))
                }

                item {
                    ListItem(leadingContent = {
                        Icon(Icons.Rounded.FileDownload, contentDescription = stringResource(R.string.files_info_resolution))
                    }, headlineContent = {
                        Text(text = remember(component.infoSheetContent.sizeBytes) {
                            Formatter.formatFileSize(localContext, component.infoSheetContent.sizeBytes)
                        })
                    }, supportingContent = {
                        Text(stringResource(R.string.files_info_size))
                    }, colors = ListItemDefaults.colors(containerColor = Color.Transparent))
                }

                item {
                    ListItem(leadingContent = {
                        Icon(Icons.Rounded.Visibility, contentDescription = stringResource(R.string.files_info_views))
                    }, headlineContent = {
                        Text(text = component.infoSheetContent.views.toString())
                    }, supportingContent = {
                        Text(stringResource(R.string.files_info_views))
                    }, colors = ListItemDefaults.colors(containerColor = Color.Transparent))
                }

                item {
                    ListItem(leadingContent = {
                        Icon(Icons.Rounded.ThumbUp, contentDescription = stringResource(R.string.files_info_likes))
                    }, headlineContent = {
                        Text(text = component.infoSheetContent.likes.toString())
                    }, supportingContent = {
                        Text(stringResource(R.string.files_info_likes))
                    }, colors = ListItemDefaults.colors(containerColor = Color.Transparent))
                }

                item {
                    ListItem(leadingContent = {
                        Icon(Icons.Rounded.Star, contentDescription = stringResource(R.string.files_info_favorites))
                    }, headlineContent = {
                        Text(text = component.infoSheetContent.favorites.toString())
                    }, supportingContent = {
                        Text(stringResource(R.string.files_info_favorites))
                    }, colors = ListItemDefaults.colors(containerColor = Color.Transparent))
                }
            }
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AsyncImage(
                    model = component.url,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    IconButton(
                        onClick = {
                            shareContextMenu = true
                        }
                    ) {
                        Icon(
                            Icons.Rounded.Share,
                            contentDescription = stringResource(R.string.files_action_share)
                        )
                    }

                    DropdownMenu(
                        expanded = shareContextMenu,
                        onDismissRequest = { shareContextMenu = false }) {
                        DropdownMenuItem(
                            text = {
                                Text(stringResource(R.string.files_action_share_post_link))
                            }, onClick = {
                                share(localContext, component.formatDocumentShareUrl())
                            }, leadingIcon = {
                                Icon(
                                    Icons.Rounded.Link,
                                    contentDescription = stringResource(R.string.files_action_share_post_link)
                                )
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text(stringResource(R.string.files_action_share_file_link))
                            }, onClick = {
                                share(localContext, component.url)
                            }, leadingIcon = {
                                Icon(
                                    Icons.Rounded.DatasetLinked,
                                    contentDescription = stringResource(R.string.files_action_share_file_link)
                                )
                            }
                        )
                    }
                }

                Spacer(Modifier.weight(1f))

                FloatingActionButton(
                    onClick = component::dispatchSaveToGallery
                ) {
                    AnimatedContent(
                        targetState = saveToGalleryState,
                        transitionSpec = {
                            materialSharedAxisY(forward = true, slideDistance = slideDistance)
                        }
                    ) { state ->
                        when (state) {
                            PublishedFullscreenPhotoViewerComponent.SaveToGalleryState.Awaiting -> {
                                Icon(
                                    Icons.Rounded.Download,
                                    contentDescription = stringResource(R.string.files_action_save)
                                )
                            }

                            PublishedFullscreenPhotoViewerComponent.SaveToGalleryState.InProgress -> {
                                CircularProgressIndicator(progress = { saveToGalleryAnimatedProgress })
                            }

                            PublishedFullscreenPhotoViewerComponent.SaveToGalleryState.PermissionRequired -> {
                                Icon(
                                    Icons.Rounded.PermMedia,
                                    contentDescription = stringResource(R.string.files_action_save)
                                )
                            }

                            PublishedFullscreenPhotoViewerComponent.SaveToGalleryState.Error -> {
                                Icon(
                                    Icons.Rounded.Error,
                                    contentDescription = stringResource(R.string.files_action_save)
                                )
                            }

                            PublishedFullscreenPhotoViewerComponent.SaveToGalleryState.Success -> {
                                Icon(
                                    Icons.Rounded.CheckCircle,
                                    contentDescription = stringResource(R.string.files_action_save)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun share(context: Context, url: String) {
    context.startActivity(
        Intent.createChooser(
            Intent(Intent.ACTION_SEND).also { i ->
                i.type = "text/plain"
                i.putExtra(Intent.EXTRA_TEXT, url)
            }, "Share URL"
        )
    )
}