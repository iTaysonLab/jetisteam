package bruhcollective.itaysonlab.microapp.gamepage.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.material3.Material3RichText

@Composable
fun GamePageDescription(
    html: String,
    modifier: Modifier
) {
    Material3RichText(modifier = modifier) {
        Markdown(content = html)
    }
}