package bruhcollective.itaysonlab.cobalt.published_files

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.compose.nonNullableAnimatedVisibilityScope
import bruhcollective.itaysonlab.cobalt.compose.nonNullableNavSharedTransitionScope
import coil.compose.AsyncImage

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PublishedFullscreenPhotoViewer(
    component: PublishedFullscreenPhotoViewerComponent
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {}, navigationIcon = {
                IconButton(onClick = component::onBackPressed) {
                    Icon(Icons.Rounded.Close, contentDescription = null)
                }
            }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent))
        }, contentColor = Color.White, containerColor = Color.Black
    ) { padding ->
        Box(Modifier.fillMaxSize()) {
            with(nonNullableNavSharedTransitionScope()) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .sharedElement(
                            state = rememberSharedContentState(key = component.id),
                            animatedVisibilityScope = nonNullableAnimatedVisibilityScope(),
                        )
                ) {
                    AsyncImage(
                        model = component.previewUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )

                    AsyncImage(
                        model = component.url,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            LazyRow(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(padding),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                item {
                    AssistChip(
                        onClick = {},
                        label = {
                            Text("About")
                        }, leadingIcon = {
                            Icon(Icons.Rounded.Info, contentDescription = null)
                        }
                    )
                }

                item {
                    AssistChip(
                        onClick = {},
                        label = {
                            Text("Download")
                        }, leadingIcon = {
                            Icon(Icons.Rounded.Download, contentDescription = null)
                        }
                    )
                }

                item {
                    AssistChip(
                        onClick = {},
                        label = {
                            Text("Share")
                        }, leadingIcon = {
                            Icon(Icons.Rounded.Share, contentDescription = null)
                        }
                    )
                }

                item {
                    AssistChip(
                        onClick = {},
                        label = {
                            Text("Copy link")
                        }, leadingIcon = {
                            Icon(Icons.Rounded.Link, contentDescription = null)
                        }
                    )
                }
            }
        }
    }
}