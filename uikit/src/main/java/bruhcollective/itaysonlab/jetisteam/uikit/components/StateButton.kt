package bruhcollective.itaysonlab.jetisteam.uikit.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StateButton(
    onClick: () -> Unit,
    inLoadingState: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Button(onClick = onClick, modifier = modifier) {
        Box(Modifier.animateContentSize()) {
            if (inLoadingState) {
                ResizableCircularIndicator(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.onPrimary,
                    indicatorSize = 24.dp
                )
            } else {
                content()
            }
        }
    }
}