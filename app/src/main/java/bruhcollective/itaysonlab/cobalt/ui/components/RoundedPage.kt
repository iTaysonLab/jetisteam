package bruhcollective.itaysonlab.cobalt.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RoundedPage(
    modifier: Modifier = Modifier,
    cornerSize: Dp = 28.dp,
    color: Color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier
            .clip(RoundedCornerShape(topStart = cornerSize, topEnd = cornerSize))
            .background(color)
    ) {
        content()
    }
}