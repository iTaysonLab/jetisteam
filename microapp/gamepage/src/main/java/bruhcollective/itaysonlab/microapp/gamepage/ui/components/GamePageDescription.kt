package bruhcollective.itaysonlab.microapp.gamepage.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.jetisteam.uikit.components.ExpandableRichText
import bruhcollective.itaysonlab.jetisteam.uikit.components.FixedSizeRichText
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.material3.Material3RichText

@Composable
fun GamePageDescription(
    html: String,
    modifier: Modifier
) {
    ExpandableRichText(
        modifier = modifier,
        markdown = html,
        textColor = MaterialTheme.colorScheme.primary,
        backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
    )
}