package bruhcollective.itaysonlab.microapp.gamepage.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetisteam.models.GameFullDetailsData
import bruhcollective.itaysonlab.microapp.gamepage.R

@Composable
internal fun GamePageTagList(
    tags: List<Pair<String, Int>>,
    genres: List<GameFullDetailsData.KeyValue>
) {
    Column(
        Modifier
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
            .fillMaxWidth()
            .padding(vertical = 12.dp)
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