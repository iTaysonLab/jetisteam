package bruhcollective.itaysonlab.microapp.profile.ui.screens.editsections

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.components.ResizableCircularIndicator
import bruhcollective.itaysonlab.jetisteam.uikit.components.RoundedPage
import bruhcollective.itaysonlab.jetisteam.uikit.page.FullscreenError
import bruhcollective.itaysonlab.jetisteam.uikit.page.FullscreenLoading
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.profile.R
import bruhcollective.itaysonlab.microapp.profile.core.ProfileEditEvent
import coil.compose.AsyncImage
import soup.compose.material.motion.animation.materialSharedAxisY
import soup.compose.material.motion.animation.rememberSlideDistance

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
internal fun ProfileEditThemeScreen(
    viewModel: ProfileEditThemeViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
    onChangesCommitted: (ProfileEditEvent) -> Unit
) {
    val tas = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val slideDistance = rememberSlideDistance()

    Scaffold(
        topBar = {
            LargeTopAppBar(title = {
                Text(
                    text = stringResource(R.string.edit_type_theme)
                )
            }, navigationIcon = {
                IconButton(onClick = onBackClicked) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                }
            }, scrollBehavior = tas, colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                scrolledContainerColor = MaterialTheme.colorScheme.surface,
            ))
        }, contentWindowInsets = EmptyWindowInsets, modifier = Modifier
            .fillMaxSize()
            .nestedScroll(tas.nestedScrollConnection), floatingActionButton = {
            val fabDiff by animateDpAsState(
                targetValue = if (viewModel.hasAnyChanges.not()) 96.dp else 0.dp,
                animationSpec = spring(stiffness = 1000f)
            )

            FloatingActionButton(onClick = { viewModel.commitChanges(onChangesCommitted) }, modifier = Modifier.offset(y = fabDiff)) {
                AnimatedContent(targetState = viewModel.isCommittingChanges, transitionSpec = {
                    materialSharedAxisY(forward = true, slideDistance = slideDistance).using(
                        SizeTransform(clip = false)
                    )
                }) { isCommittingChanges ->
                    if (isCommittingChanges) {
                        ResizableCircularIndicator(color = LocalContentColor.current, indicatorSize = 24.dp, strokeWidth = 2.dp)
                    } else {
                        Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
                    }
                }
            }
        }
    ) { innerPadding ->
        RoundedPage(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (val state = viewModel.state) {
                is PageViewModel.State.Error -> FullscreenError(
                    onReload = viewModel::reload,
                    exception = state.exception
                )

                is PageViewModel.State.Loading -> FullscreenLoading()

                is PageViewModel.State.Loaded -> {
                    LazyColumn {
                        items(viewModel.availableItems) { item ->
                            ListItem(
                                headlineText = {
                                    Text(text = item.name)
                                },
                                supportingText = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(top = 4.dp)
                                    ) {
                                        AsyncImage(
                                            model = item.fromApplicationIcon,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .size(18.dp)
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Text(text = item.fromApplication)
                                    }
                                },
                                leadingContent = {
                                    RadioButton(
                                        selected = viewModel.currentItemId == item.id,
                                        onClick = {
                                            viewModel.switchItemSelection(item.id)
                                        })
                                },
                                trailingContent = {
                                    AsyncImage(
                                        model = item.itemPreview,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(vertical = 4.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .size(56.dp),
                                        placeholder = ColorPainter(
                                            MaterialTheme.colorScheme.surfaceColorAtElevation(
                                                8.dp
                                            )
                                        )
                                    )
                                },
                                modifier = Modifier.fillMaxWidth().clickable {
                                    viewModel.switchItemSelection(item.id)
                                },
                                colors = ListItemDefaults.colors(
                                    containerColor = Color.Transparent
                                )
                            )

                            Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                        }
                    }
                }
            }
        }
    }
}