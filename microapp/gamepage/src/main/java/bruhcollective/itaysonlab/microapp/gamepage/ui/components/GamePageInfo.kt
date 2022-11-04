package bruhcollective.itaysonlab.microapp.gamepage.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetisteam.models.GameFullDetailsData
import bruhcollective.itaysonlab.jetisteam.models.NameUrlRelation
import bruhcollective.itaysonlab.jetisteam.util.DateUtil
import bruhcollective.itaysonlab.microapp.gamepage.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun GamePageInfo(
    publishers: List<String>,
    developers: List<String>,
    releaseDate: GameFullDetailsData.ReleaseDate
) {
    Column(
        Modifier
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        if (publishers.isNotEmpty()) {
            GPIHeadedText(
                title = pluralStringResource(id = R.plurals.gamepage_publishers, count = publishers.size),
                text = remember(publishers) {
                    publishers.joinToString()
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Divider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.surfaceColorAtElevation(16.dp))
        }

        if (developers.isNotEmpty()) {
            GPIHeadedText(
                title = pluralStringResource(id = R.plurals.gamepage_developers, count = developers.size),
                text = remember(developers) {
                    developers.joinToString()
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Divider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.surfaceColorAtElevation(16.dp))
        }

        GPIHeadedText(
            title = stringResource(id = R.string.gamepage_release),
            text = releaseDate.date,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun GPIHeadedText(
    title: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        fontSize = 13.sp,
        modifier = modifier
    )

    Text(
        text = text,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 18.sp,
        modifier = modifier
    )
}