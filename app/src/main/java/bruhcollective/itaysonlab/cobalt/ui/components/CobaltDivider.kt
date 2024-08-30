package bruhcollective.itaysonlab.cobalt.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CobaltDivider(
    modifier: Modifier = Modifier,
    padding: Dp = 16.dp,
) {
    HorizontalDivider(
        modifier = modifier.padding(horizontal = padding),
        color = MaterialTheme.colorScheme.surfaceVariant
    )
}

@Composable
fun CobaltVerticalDivider(
    modifier: Modifier = Modifier,
    targetThickness: Dp = 1.dp
) {
    Box(
        modifier
            .fillMaxHeight()
            .width(targetThickness)
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
    )
}