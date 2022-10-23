package bruhcollective.itaysonlab.jetisteam.uikit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun RoundedPage(
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier
            .clip(MaterialTheme.shapes.extraLarge.copy(bottomStart = CornerSize(0.dp), bottomEnd = CornerSize(0.dp)))
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
    ) {
        content()
    }
}