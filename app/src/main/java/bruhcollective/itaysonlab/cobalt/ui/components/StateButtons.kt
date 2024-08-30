package bruhcollective.itaysonlab.cobalt.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun StateButton(
    onClick: () -> Unit,
    inLoadingState: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable () -> Unit,
) {
    Button(onClick = {
        if (!inLoadingState) onClick()
    }, modifier = modifier, shape = shape, contentPadding = contentPadding, enabled = enabled) {
        StateButtonContent(inLoadingState, content)
    }
}

@Composable
fun StateTonalButton(
    onClick: () -> Unit,
    inLoadingState: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable () -> Unit,
) {
    FilledTonalButton(onClick = {
        if (!inLoadingState) onClick()
    }, modifier = modifier, shape = shape, contentPadding = contentPadding, enabled = enabled) {
        StateButtonContent(inLoadingState, content)
    }
}

@Composable
fun StateTextButton(
    onClick: () -> Unit,
    inLoadingState: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    content: @Composable () -> Unit,
) {
    TextButton(onClick = {
        if (!inLoadingState) onClick()
    }, modifier = modifier, shape = shape, enabled = enabled) {
        StateButtonContent(inLoadingState, content)
    }
}

@Composable
fun StateButtonContent(
    inLoadingState: Boolean,
    content: @Composable () -> Unit,
) {
    Box(Modifier.animateContentSize()) {
        if (inLoadingState) {
            ResizableCircularIndicator(
                modifier = Modifier,
                color = LocalContentColor.current,
                indicatorSize = 19.dp,
                strokeWidth = 2.dp
            )
        } else {
            content()
        }
    }
}