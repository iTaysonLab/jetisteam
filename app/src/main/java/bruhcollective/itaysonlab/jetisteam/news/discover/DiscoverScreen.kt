package bruhcollective.itaysonlab.jetisteam.news.discover

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.news.discover.DiscoverComponent
import bruhcollective.itaysonlab.jetisteam.news.entries.NewAchievementsEntry
import bruhcollective.itaysonlab.jetisteam.ui.components.CobaltDivider
import bruhcollective.itaysonlab.jetisteam.ui.components.EmptyWindowInsets
import bruhcollective.itaysonlab.jetisteam.ui.font.robotoMonoFontFamily
import bruhcollective.itaysonlab.ksteam.models.news.usernews.ActivityFeedEntry
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    component: DiscoverComponent
) {
    val loadState by component.state.subscribeAsState()
    val items by component.items.subscribeAsState()

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(text = "News".uppercase(), fontFamily = robotoMonoFontFamily)
                    }, windowInsets = EmptyWindowInsets, colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )

                CobaltDivider(padding = 0.dp)
            }
        }, contentWindowInsets = EmptyWindowInsets
    ) { innerPadding ->
        when (loadState) {
            DiscoverComponent.DiscoverState.Idle -> {
                LaunchedEffect(Unit) {
                    component.dispatchInitialLoad()
                }
            }

            DiscoverComponent.DiscoverState.Loading -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            DiscoverComponent.DiscoverState.Loaded -> {
                LazyColumn(modifier = Modifier.padding(innerPadding)) {
                    items(items) { feedEntry ->
                        when (feedEntry) {
                            is ActivityFeedEntry.NewAchievements -> {
                                NewAchievementsEntry(feedEntry)
                            }

                            else -> {
                                Text(text = feedEntry.toString())
                            }
                        }

                        CobaltDivider(padding = 0.dp)
                    }
                }
            }

            DiscoverComponent.DiscoverState.Error -> TODO()
        }
    }
}