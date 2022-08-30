package bruhcollective.itaysonlab.microapp.gamepage.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetisteam.models.NameUrlRelation
import bruhcollective.itaysonlab.jetisteam.util.DateUtil
import bruhcollective.itaysonlab.microapp.gamepage.R

@Composable
internal fun GamePageInfo(
    publishers: List<NameUrlRelation>,
    developers: List<NameUrlRelation>,
    franchises: List<NameUrlRelation>,
    releaseDate: Int
) {
    Column(
        Modifier
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
            .fillMaxWidth()
            .padding(16.dp)) {

        if (publishers.isNotEmpty()) {
            GPIHeadedText(title = stringResource(id = R.string.gamepage_publishers), text = remember(publishers) {
                publishers.joinToString { it.name }
            })

            Spacer(modifier = Modifier.height(8.dp))
        }

        if (developers.isNotEmpty()) {
            GPIHeadedText(title = stringResource(id = R.string.gamepage_developers), text = remember(developers) {
                developers.joinToString { it.name }
            })

            Spacer(modifier = Modifier.height(8.dp))
        }

        if (franchises.isNotEmpty()) {
            GPIHeadedText(title = stringResource(id = R.string.gamepage_franchises), text = remember(franchises) {
                franchises.joinToString { it.name }
            })

            Spacer(modifier = Modifier.height(8.dp))
        }

        GPIHeadedText(title = stringResource(id = R.string.gamepage_release), text = remember(releaseDate) {
            DateUtil.formatDateTimeToLocale(releaseDate * 1000L)
        })
    }
}

@Composable
private fun GPIHeadedText(
    title: String,
    text: String
) {
    Text(text = title, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 13.sp)
    Text(text = text, color = MaterialTheme.colorScheme.onSurface, fontSize = 18.sp)
}