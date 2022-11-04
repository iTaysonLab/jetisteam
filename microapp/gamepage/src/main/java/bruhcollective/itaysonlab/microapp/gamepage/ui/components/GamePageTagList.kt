package bruhcollective.itaysonlab.microapp.gamepage.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetisteam.models.GameFullDetailsData
import bruhcollective.itaysonlab.jetisteam.models.NameUrlRelation
import bruhcollective.itaysonlab.jetisteam.models.Review
import bruhcollective.itaysonlab.jetisteam.models.Reviews
import bruhcollective.itaysonlab.jetisteam.util.DateUtil
import bruhcollective.itaysonlab.microapp.gamepage.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GamePageTagList(
    tags: List<Pair<String, Int>>,
    genres: List<GameFullDetailsData.KeyValue>,
    metaCritic: GameFullDetailsData.MetacriticLocator?
) {
    val uriHandler = LocalUriHandler.current

    Column(
        Modifier
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
            .fillMaxWidth()
            .padding(top = 12.dp).padding(bottom = if (metaCritic == null) 12.dp else 0.dp)
    ) {
        GPTList(
            title = stringResource(id = R.string.gamepage_tags),
            items = remember(tags) { tags.map { it.first } }
        )

        Divider(
            modifier = Modifier.padding(vertical = 12.dp),
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(16.dp)
        )

        GPTList(
            title = stringResource(id = R.string.gamepage_genres),
            items = remember(genres) { genres.map { it.description } }
        )

        if (metaCritic != null) {
            Divider(
                modifier = Modifier.padding(top = 12.dp),
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(16.dp)
            )

            ListItem(
                supportingText = {
                    Text(text = "Metacritic Score", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }, headlineText = {
                    Text(text = metaCritic.score.toString(), color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp)
                }, leadingContent = {
                    CircularProgressIndicator(progress = 1f, color = MaterialTheme.colorScheme.surfaceColorAtElevation(16.dp))

                    CircularProgressIndicator(progress = remember(metaCritic.score) {
                        metaCritic.score / 100f
                    }, color = remember(metaCritic.score) {
                        when {
                            metaCritic.score >= 75 -> Color(android.graphics.Color.parseColor("#66CC33"))
                            metaCritic.score >= 50 -> Color(android.graphics.Color.parseColor("#FFCC33"))
                            else -> Color(android.graphics.Color.parseColor("#FF0000"))
                        }
                    })
                }, trailingContent = {
                    Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = null)
                }, colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                ), modifier = Modifier.fillMaxWidth().clickable {
                    uriHandler.openUri(metaCritic.url)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GPTList(
    title: String,
    items: List<String>,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        fontSize = 13.sp,
        modifier = modifier.padding(horizontal = 16.dp)
    )

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(items) { item ->
            SuggestionChip(onClick = { /*TODO*/ }, label = {
                Text(
                    text = item,
                    color = MaterialTheme.colorScheme.onSurface
                )
            })
        }
    }
}