package bruhcollective.itaysonlab.cobalt.news.entries.parts

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.ksteam.models.persona.SummaryPersona
import coil.compose.AsyncImage

@Composable
fun PostPersonaHeader(
    persona: SummaryPersona,
    postedDate: Int,
    modifier: Modifier = Modifier
) {
    val ctx = LocalContext.current

    val formattedDate = remember(postedDate) {
        DateUtils.getRelativeDateTimeString(
            ctx,
            postedDate * 1000L,
            DateUtils.MINUTE_IN_MILLIS,
            DateUtils.WEEK_IN_MILLIS,
            0
        ).toString().uppercase()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = persona.avatar.medium,
            contentDescription = null,
            modifier = Modifier.size(38.dp),
            placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
            error = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
        )

        Column {
            Text(text = persona.name)

            Text(
                text = formattedDate,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.alpha(0.5f)
            )
        }
    }
}