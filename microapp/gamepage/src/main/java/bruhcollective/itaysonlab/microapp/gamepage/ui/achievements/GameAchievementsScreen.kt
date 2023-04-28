package bruhcollective.itaysonlab.microapp.gamepage.ui.achievements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.components.RoundedPage
import bruhcollective.itaysonlab.jetisteam.uikit.page.PageLayout
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.gamepage.R
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GameAchievementsScreen(
    viewModel: GameAchievementsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val tas = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            LargeTopAppBar(title = {
                Text(text = stringResource(id = R.string.gamepage_achievements))
            }, navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                }
            }, scrollBehavior = tas, colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                scrolledContainerColor = MaterialTheme.colorScheme.surface,
            )
            )
        }, contentWindowInsets = EmptyWindowInsets, modifier = Modifier
            .fillMaxSize()
            .nestedScroll(tas.nestedScrollConnection)
    ) { innerPadding ->
        RoundedPage(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PageLayout(state = viewModel.state, onReload = viewModel::reload) { data ->
                LazyColumn(
                    contentPadding = floatingWindowInsetsAsPaddings(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(data.global) { achievement ->
                        val isUnlocked = remember(achievement.internal_name) {
                            data.unlocked[achievement.localized_name]
                        }

                        val icon = remember(achievement.internal_name) {
                            viewModel.wrapIcon(
                                if (isUnlocked != null) {
                                    achievement.icon.orEmpty()
                                } else {
                                    achievement.icon_gray.orEmpty()
                                }
                            )
                        }

                        AchievementCard(
                            name = achievement.localized_name.orEmpty(),
                            desc = achievement.localized_desc.orEmpty(),
                            icon = icon,
                            globalPercent = achievement.player_percent_unlocked.orEmpty(),
                            isUnlocked = isUnlocked != null,
                            isHidden = isUnlocked == null && (achievement.hidden ?: false),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AchievementCard(
    name: String,
    desc: String,
    icon: String?,
    globalPercent: String,
    isUnlocked: Boolean,
    isHidden: Boolean,
    modifier: Modifier = Modifier
) {
    var isHiddenState by remember {
        mutableStateOf(isHidden)
    }

    Card(
        modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                if (isUnlocked) 16.dp else 4.dp
            )
        )
    ) {
        Box {
            ListItem(
                headlineContent = {
                    Text(name)
                }, supportingContent = {
                    Column {
                        Text(desc)
                        Text(
                            stringResource(
                                id = R.string.gamepage_achievements_percent,
                                globalPercent
                            ), style = MaterialTheme.typography.labelSmall
                        )
                    }
                }, leadingContent = {
                    AsyncImage(
                        model = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .size(56.dp),
                        placeholder = ColorPainter(
                            MaterialTheme.colorScheme.surfaceColorAtElevation(
                                8.dp
                            )
                        ),
                        error = ColorPainter(MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)),
                    )
                }, colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent
                ), trailingContent = {
                    if (isUnlocked) {
                        Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (isHiddenState) 0f else 1f)
            )

            ListItem(
                headlineContent = {
                    Text(stringResource(id = R.string.gamepage_achievements_hidden))
                }, supportingContent = {
                    Text(stringResource(id = R.string.gamepage_achievements_hidden_desc))
                }, colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent
                ), modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (isHiddenState) 1f else 0f)
                    .clickable {
                        isHiddenState = false
                    }
                    .align(Alignment.Center), leadingContent = {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(16.dp))
                            .size(56.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Lock,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        )
                    }
                }
            )
        }
    }
}