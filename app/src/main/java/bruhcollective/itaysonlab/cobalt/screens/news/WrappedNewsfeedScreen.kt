package bruhcollective.itaysonlab.cobalt.screens.news

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Newspaper
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.news.PickNewsfeedComponent
import bruhcollective.itaysonlab.cobalt.news.WrappedNewsfeedComponent
import bruhcollective.itaysonlab.cobalt.news.models.NewsfeedType
import bruhcollective.itaysonlab.cobalt.ui.components.EmptyWindowInsets
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WrappedNewsfeedScreen(
    component: WrappedNewsfeedComponent,
) {
    val currentType by component.currentType.subscribeAsState()
    val pickerSlot by component.pickerSlot.subscribeAsState()
    val feedSlot by component.feedSlot.subscribeAsState()

    pickerSlot.child?.instance?.let { component ->
        FeedPickerModalBottomSheet(component)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            component.openPicker()
                        }
                    ) {
                        Text(text = currentType.title)
                        Icon(imageVector = Icons.Rounded.ArrowDropDown, contentDescription = null)
                    }
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }, contentWindowInsets = EmptyWindowInsets
    ) { innerPadding ->
        feedSlot.child?.instance?.let { component ->
            NewsfeedScreenContent(
                component = component,
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FeedPickerModalBottomSheet(component: PickNewsfeedComponent) {
    ModalBottomSheet(onDismissRequest = component::dismiss) {
        LazyColumn {
            item {
                FeedEntry(
                    selected = component.selectedFeed == NewsfeedType.Everything,
                    entry = NewsfeedType.Everything,
                    modifier = Modifier.fillMaxWidth().clickable {
                        component.selectFeed(NewsfeedType.Everything)
                    }
                )
            }

            item {
                FeedEntry(
                    selected = component.selectedFeed == NewsfeedType.Upcoming,
                    entry = NewsfeedType.Upcoming,
                    modifier = Modifier.fillMaxWidth().clickable {
                        component.selectFeed(NewsfeedType.Upcoming)
                    }
                )
            }

            item {
                HorizontalDivider()
            }

            item {
                FeedEntry(
                    selected = component.selectedFeed == NewsfeedType.News,
                    entry = NewsfeedType.News,
                    modifier = Modifier.fillMaxWidth().clickable {
                        component.selectFeed(NewsfeedType.News)
                    }
                )
            }

            item {
                FeedEntry(
                    selected = component.selectedFeed == NewsfeedType.Activity,
                    entry = NewsfeedType.Activity,
                    modifier = Modifier.fillMaxWidth().clickable {
                        component.selectFeed(NewsfeedType.Activity)
                    }
                )
            }

            item {
                HorizontalDivider()
            }

            item {
                FeedEntry(
                    selected = component.selectedFeed == NewsfeedType.Featured,
                    entry = NewsfeedType.Featured,
                    modifier = Modifier.fillMaxWidth().clickable {
                        component.selectFeed(NewsfeedType.Featured)
                    }
                )
            }

            item {
                FeedEntry(
                    selected = component.selectedFeed == NewsfeedType.Press,
                    entry = NewsfeedType.Press,
                    modifier = Modifier.fillMaxWidth().clickable {
                        component.selectFeed(NewsfeedType.Press)
                    }
                )
            }

            item {
                FeedEntry(
                    selected = component.selectedFeed == NewsfeedType.Steam,
                    entry = NewsfeedType.Steam,
                    modifier = Modifier.fillMaxWidth().clickable {
                        component.selectFeed(NewsfeedType.Steam)
                    }
                )
            }
        }
    }
}

@Composable
private fun FeedEntry(
    selected: Boolean,
    entry: NewsfeedType,
    modifier: Modifier
) {
    ListItem(
        headlineContent = {
            Text(entry.title)
        },
        supportingContent = {
            Text(entry.description)
        },
        leadingContent = {
            Icon(
                imageVector = entry.icon,
                contentDescription = entry.title
            )
        },
        trailingContent = {
            if (selected) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null
                )
            }
        },
        modifier = modifier,
        colors = ListItemDefaults.colors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            } else {
                Color.Transparent
            }
        )
    )
}

//

private val NewsfeedType.title @Composable get() = when (this) {
    NewsfeedType.Everything -> stringResource(R.string.news_feed_unified)
    NewsfeedType.Activity -> stringResource(R.string.news_feed_user_activity)
    NewsfeedType.News -> stringResource(R.string.news_feed_game_news)
    NewsfeedType.Upcoming -> stringResource(R.string.news_feed_upcoming)
    //
    NewsfeedType.Featured -> "Featured"
    NewsfeedType.Press -> "Press"
    NewsfeedType.Steam -> "Steam"
}

private val NewsfeedType.description @Composable get() = when (this) {
    NewsfeedType.Everything -> stringResource(R.string.news_feed_unified_desc)
    NewsfeedType.Activity -> stringResource(R.string.news_feed_user_activity_desc)
    NewsfeedType.News -> stringResource(R.string.news_feed_game_news_desc)
    NewsfeedType.Upcoming -> stringResource(R.string.news_feed_upcoming_desc)
    //
    NewsfeedType.Featured -> stringResource(R.string.news_feed_upcoming)
    NewsfeedType.Press -> stringResource(R.string.news_feed_upcoming)
    NewsfeedType.Steam -> stringResource(R.string.news_feed_upcoming)
}

private val NewsfeedType.icon @Composable get() = when (this) {
    NewsfeedType.Everything -> Icons.Rounded.Bolt
    NewsfeedType.Activity -> Icons.Rounded.Group
    NewsfeedType.News -> Icons.Rounded.Newspaper
    NewsfeedType.Upcoming -> Icons.Rounded.Update
    //
    NewsfeedType.Featured -> Icons.Rounded.Star
    NewsfeedType.Press -> Icons.Rounded.Star
    NewsfeedType.Steam -> Icons.Rounded.Star
}