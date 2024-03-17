package bruhcollective.itaysonlab.jetisteam.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

@Composable
fun BottomSheetHeader(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        text,
        fontSize = 22.sp,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun BottomSheetSubtitle(
    modifier: Modifier = Modifier,
    text: AnnotatedString
) {
    Text(
        text,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth()
    )
}