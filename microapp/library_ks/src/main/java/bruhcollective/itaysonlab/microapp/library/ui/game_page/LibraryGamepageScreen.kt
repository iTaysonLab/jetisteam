package bruhcollective.itaysonlab.microapp.library.ui.game_page

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.components.IndicatorBehindScrollableTabRow
import bruhcollective.itaysonlab.jetisteam.uikit.components.RoundedPage
import bruhcollective.itaysonlab.jetisteam.uikit.components.tabIndicatorOffset
import bruhcollective.itaysonlab.jetisteam.uikit.page.PageLayout
import bruhcollective.itaysonlab.ksteam.models.pics.libraryHeader
import bruhcollective.itaysonlab.ksteam.models.pics.logoLarge
import bruhcollective.itaysonlab.microapp.library.ui.game_page.activity.GamepageActivityScreen
import bruhcollective.itaysonlab.microapp.library.ui.game_page.community.GamepageCommunityScreen
import bruhcollective.itaysonlab.microapp.library.ui.game_page.pages.GamepageMoreScreen
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun LibraryGamepageScreen(
    viewModel: LibraryGamepageViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val surfaceColor = MaterialTheme.colorScheme.surface

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    val state = rememberCollapsingToolbarScaffoldState()

    PageLayout(state = viewModel.state) { appInfo ->
        CollapsingToolbarScaffold(modifier = Modifier.fillMaxSize(), state = state, scrollStrategy = ScrollStrategy.ExitUntilCollapsed, toolbar = {
            Box(modifier = Modifier
                .fillMaxWidth()
                .pin()
                .offset(y = -(150.dp * (1f - state.toolbarState.progress)))
                .alpha(state.toolbarState.progress)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(appInfo.libraryHeader.url)
                        .transformations(
                            /*if (viewModel.headerBgUrl?.blur == true) listOf(
                                BlurTransformation(LocalContext.current, 20f, 4f)
                            ) else */emptyList()
                        )
                        .build(), contentDescription = null, modifier = Modifier
                        .height(300.dp)
                        .drawWithContent {
                            drawContent()
                            drawRect(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color.Black.copy(alpha = 0.25f), surfaceColor),
                                    start = Offset(x = size.width / 2f, y = 0f),
                                    end = Offset(x = size.width / 2f, y = size.height * 0.95f),
                                )
                            )
                        }, contentScale = ContentScale.Crop
                )

                AsyncImage(
                    model = appInfo.logoLarge.url,
                    contentDescription = null,
                    modifier = Modifier.heightIn(min = 0.dp, max = 100.dp).align(Alignment.Center),
                    contentScale = ContentScale.FillHeight
                )
            }

            CenterAlignedTopAppBar(
                title = {
                    Text(appInfo.common.name, modifier = Modifier.alpha(1f - state.toolbarState.progress), maxLines = 1)
                }, modifier = Modifier
                    .pin(), colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ), navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                }
            )
        }) {
            Column(Modifier.fillMaxSize()) {
                IndicatorBehindScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    containerColor = Color.Transparent,
                    indicator = { tabPositions ->
                        Box(
                            Modifier
                                .padding(vertical = 12.dp)
                                .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onSurface)
                        )
                    },
                    edgePadding = 16.dp,
                    tabAlignment = Alignment.Center
                ) {
                    Tab(
                        selected = pagerState.currentPage == 0,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        },
                        selectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ) {
                        Text(
                            text = "Activity".uppercase(Locale.getDefault()),
                            modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp),
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                    }

                    Tab(
                        selected = pagerState.currentPage == 1,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        },
                        selectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ) {
                        Text(
                            text = "Community".uppercase(Locale.getDefault()),
                            modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp),
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                    }

                    Tab(
                        selected = pagerState.currentPage == 2,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(2)
                            }
                        },
                        selectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ) {
                        Text(
                            text = "More".uppercase(Locale.getDefault()),
                            modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp),
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                    }
                }

                HorizontalPager(
                    pageCount = 3,
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { pos ->
                    RoundedPage(modifier = Modifier.fillMaxSize()) {
                        when (pos) {
                            0 -> {
                                GamepageActivityScreen()
                            }

                            1 -> {
                                GamepageCommunityScreen()
                            }

                            2 -> {
                                GamepageMoreScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}