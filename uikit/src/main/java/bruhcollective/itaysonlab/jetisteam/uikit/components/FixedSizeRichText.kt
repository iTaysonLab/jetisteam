package bruhcollective.itaysonlab.jetisteam.uikit.components

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.material3.Material3RichText

@Composable
fun FixedSizeRichText(
    modifier: Modifier = Modifier,
    markdown: String
) {
    val density = LocalDensity.current

    val size = rememberSaveable(markdown) {
        mutableStateOf(0)
    }

    LaunchedEffect(markdown) {
        size.value = 0
    }

    Material3RichText(modifier.onSizeChanged {
        if (size.value == 0 && it.height != 0) {
            size.value = it.height
        }
    }.let {
        if (size.value != 0) it.height(with(density) { size.value.toDp() }) else it
    }) {
        Markdown(content = markdown)
    }
}