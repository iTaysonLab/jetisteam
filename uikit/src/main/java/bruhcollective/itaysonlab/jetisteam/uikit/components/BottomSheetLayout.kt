package bruhcollective.itaysonlab.jetisteam.uikit.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheetLayout(
    modifier: Modifier = Modifier,
    title: @Composable (() -> String)? = null,
    subtitle: @Composable (() -> AnnotatedString)? = null,
    hasSubtitlePadding: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier
            .fillMaxWidth()
            .navigationBarsPadding()) {
        BottomSheetHandle(modifier = Modifier.align(Alignment.CenterHorizontally))

        if (title != null) {
            BottomSheetHeader(
                text = title(),
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        if (subtitle != null) {
            BottomSheetSubtitle(text = subtitle(), modifier = Modifier.padding(bottom = if (hasSubtitlePadding) 16.dp else 0.dp))
        }

        content()
    }
}