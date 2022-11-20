package bruhcollective.itaysonlab.jetisteam.uikit.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        StateButtonContent(inLoadingState, MaterialTheme.colorScheme.onPrimaryContainer, content)
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
        StateButtonContent(inLoadingState, MaterialTheme.colorScheme.onSecondaryContainer, content)
    }
}

@Composable
fun StateTextButton(
    onClick: () -> Unit,
    inLoadingState: Boolean,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    content: @Composable () -> Unit,
) {
    TextButton(onClick = {
        if (!inLoadingState) onClick()
    }, modifier = modifier, shape = shape) {
        StateButtonContent(inLoadingState, MaterialTheme.colorScheme.primary, content)
    }
}

@Composable
private fun StateButtonContent(
    inLoadingState: Boolean,
    containerColor: Color,
    content: @Composable () -> Unit,
) {
    Box(Modifier.animateContentSize()) {
        if (inLoadingState) {
            ResizableCircularIndicator(
                modifier = Modifier,
                color = containerColor,
                indicatorSize = 24.dp,
                strokeWidth = 2.dp
            )
        } else {
            content()
        }
    }
}