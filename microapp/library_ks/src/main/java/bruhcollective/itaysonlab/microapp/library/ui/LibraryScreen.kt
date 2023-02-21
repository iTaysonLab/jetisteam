package bruhcollective.itaysonlab.microapp.library.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bruhcollective.itaysonlab.jetisteam.uikit.components.IndicatorBehindScrollableTabRow
import bruhcollective.itaysonlab.jetisteam.uikit.components.tabIndicatorOffset
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.library.ui.library_pages.CollectionPage
import bruhcollective.itaysonlab.microapp.library.ui.library_pages.Homescreen
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
internal fun LibraryScreen(
    onGameClick: (Int) -> Unit,
    // onRemoteClick: (Long, List<CClientComm_GetAllClientLogonInfo_Response_Session>) -> Unit,
    viewModel: LibraryScreenViewModel = hiltViewModel()
) {
    val collections = viewModel.allCollections.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val snackState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState()

    Scaffold(topBar = {
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
            modifier = Modifier.statusBarsPadding(),
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
                Icon(
                    imageVector = Icons.Rounded.Home,
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            collections.value.forEachIndexed { index, collection ->
                Tab(
                    selected = pagerState.currentPage == index + 1,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index + 1)
                        }
                    },
                    selectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ) {
                    Text(
                        text = collection.name.uppercase(Locale.getDefault()),
                        modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp),
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }, contentWindowInsets = EmptyWindowInsets, snackbarHost = {
        SnackbarHost(hostState = snackState) {
            Snackbar(snackbarData = it)
        }
    }, modifier = Modifier.fillMaxSize()) { innerPadding ->
        HorizontalPager(
            pageCount = collections.value.size + 1, state = pagerState, modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), beyondBoundsPageCount = 1
        ) { page ->
            if (page == 0) {
                Homescreen(onClick = onGameClick)
            } else {
                CollectionPage(collection = collections.value[page - 1], onClick = onGameClick)
            }
        }
    }
}