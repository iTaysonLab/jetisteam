package bruhcollective.itaysonlab.jetisteam.uikit.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun StateButton(
    onClick: () -> Unit,
    inLoadingState: Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    content: @Composable () -> Unit,
) {
    Button(onClick = {
        if (!inLoadingState) onClick()
    }, modifier = modifier, shape = shape) {
        Box(Modifier.animateContentSize()) {
            if (inLoadingState) {
                ResizableCircularIndicator(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.onPrimary,
                    indicatorSize = 24.dp,
                    strokeWidth = 2.dp
                )
            } else {
                content()
            }
        }
    }
}

@Composable
fun StateTonalButton(
    onClick: () -> Unit,
    inLoadingState: Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    content: @Composable () -> Unit,
) {
    FilledTonalButton(onClick = {
        if (!inLoadingState) onClick()
    }, modifier = modifier, shape = shape) {
        Box(Modifier.animateContentSize()) {
            if (inLoadingState) {
                ResizableCircularIndicator(
                    modifier = Modifier,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    indicatorSize = 24.dp,
                    strokeWidth = 2.dp
                )
            } else {
                content()
            }
        }
    }
}