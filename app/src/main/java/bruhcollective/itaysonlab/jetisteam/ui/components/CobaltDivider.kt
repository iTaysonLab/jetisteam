package bruhcollective.itaysonlab.jetisteam.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CobaltDivider(
    padding: Dp = 16.dp,
    modifier: Modifier = Modifier
) {
    Divider(
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier.padding(horizontal = padding)
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