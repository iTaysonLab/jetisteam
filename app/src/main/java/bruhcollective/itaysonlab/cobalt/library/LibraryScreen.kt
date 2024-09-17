package bruhcollective.itaysonlab.cobalt.library

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.library.devices.DevicesScreen
import bruhcollective.itaysonlab.cobalt.library.games.GamesScreen
import bruhcollective.itaysonlab.cobalt.library.screenshots.ScreenshotsScreen
import bruhcollective.itaysonlab.cobalt.ui.components.EmptyWindowInsets
import bruhcollective.itaysonlab.cobalt.ui.components.IndicatorBehindScrollableTabRow
import bruhcollective.itaysonlab.cobalt.ui.components.RoundedPage
import bruhcollective.itaysonlab.cobalt.ui.components.tabIndicatorOffset
import com.arkivanov.decompose.extensions.compose.pages.ChildPages
import com.arkivanov.decompose.extensions.compose.pages.PagesScrollAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun LibraryScreen(component: LibraryComponent) {
    val pages by component.pages.subscribeAsState()

    Scaffold(topBar = {
        val currentPage = pages.selectedIndex

        IndicatorBehindScrollableTabRow(
            selectedTabIndex = pages.selectedIndex,
            containerColor = Color.Transparent,
            indicator = { tabPositions ->
                Box(
                    Modifier
                        .padding(vertical = 12.dp)
                        .tabIndicatorOffset(tabPositions[currentPage])
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface)
                )
            },
            edgePadding = 16.dp,
            modifier = Modifier.fillMaxWidth().statusBarsPadding(),
            tabAlignment = Alignment.Center,
        ) {
            Tab(
                selected = currentPage == 0,
                onClick = {
                    if (pages.selectedIndex == 0) {
                        component.scrollToTop()
                    } else {
                        component.selectPage(0)
                    }
                },
                selectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Text(
                    text = stringResource(id = R.string.library_section_devices).uppercase(Locale.getDefault()),
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp),
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            }

            Tab(
                selected = currentPage == 1,
                onClick = {
                    if (pages.selectedIndex == 1) {
                        component.scrollToTop()
                    } else {
                        component.selectPage(1)
                    }
                },
                selectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Text(
                    text = stringResource(id = R.string.library_section_games).uppercase(Locale.getDefault()),
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp),
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            }

            Tab(
                selected = currentPage == 2,
                onClick = {
                    if (pages.selectedIndex == 2) {
                        component.scrollToTop()
                    } else {
                        component.selectPage(2)
                    }
                },
                selectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Text(
                    text = stringResource(id = R.string.library_section_screenshots).uppercase(Locale.getDefault()),
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp),
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
            }

        }
    }, contentWindowInsets = EmptyWindowInsets) { innerPadding ->
        ChildPages(
            pages,
            onPageSelected = component::selectPage,
            scrollAnimation = PagesScrollAnimation.Default,
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) { _, child ->
            when (child) {
                is LibraryComponent.Child.Devices -> {
                    DevicesScreen(child.component)
                }

                is LibraryComponent.Child.Games -> {
                    GamesScreen(child.component)
                }

                is LibraryComponent.Child.Screenshots -> {
                    ScreenshotsScreen(component = child.component)
                }
            }
        }
    }
}