package bruhcollective.itaysonlab.cobalt.guard.qr_code

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateRect
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.guard.qr.GuardQrScannerComponent

@Composable
internal fun QrExpandable(
    state: GuardQrScannerComponent.QrViewfinderState,
    scannedBox: Rect,
    modifier: Modifier,
    expandedContent: @Composable () -> Unit
) {
    val paths = remember {
        QrPaths()
    }

    BoxWithConstraints(modifier) {
        val density = LocalDensity.current

        val center = remember(constraints) {
            Rect(
                center = Offset(
                    x = constraints.maxWidth / 2f,
                    y = constraints.maxHeight / 2f
                ), radius = with(density) { QrConstants.DefaultRadius.toPx() }
            )
        }

        val expanded = remember(constraints, paths.expandableHeight) {
            val horizontalPaddingPx = with(density) { QrConstants.Padding.toPx() }
            val centerHeight = constraints.maxHeight / 2f
            val halfExHeight = paths.expandableHeight / 2f

            Rect(
                topLeft = Offset(
                    x = horizontalPaddingPx,
                    y = centerHeight - halfExHeight,
                ), bottomRight = Offset(
                    x = constraints.maxWidth - horizontalPaddingPx,
                    y = centerHeight + halfExHeight,
                )
            )
        }

        val stateTransition = updateTransition(targetState = state, label = "[QR] Global state transition")

        val stateRect by stateTransition.animateRect(label = "[QR] Corners location", transitionSpec = {
            spring(stiffness = QrConstants.SpringStiffness)
        }) { state ->
            when (state) {
                GuardQrScannerComponent.QrViewfinderState.NotDetected -> center
                GuardQrScannerComponent.QrViewfinderState.Preheat -> scannedBox
                GuardQrScannerComponent.QrViewfinderState.Detected -> expanded
            }
        }

        val stateProgress by stateTransition.animateFloat(label = "[QR] Corners alpha and content scale", transitionSpec = {
            spring(stiffness = QrConstants.SpringStiffness)
        }) { state ->
            when (state) {
                GuardQrScannerComponent.QrViewfinderState.NotDetected, GuardQrScannerComponent.QrViewfinderState.Preheat -> 0f
                GuardQrScannerComponent.QrViewfinderState.Detected -> 1f
            }
        }

        val stateColor by stateTransition.animateColor(label = "[QR] Corners color", transitionSpec = {
            spring(stiffness = QrConstants.SpringStiffness)
        }) { state ->
            when (state) {
                GuardQrScannerComponent.QrViewfinderState.NotDetected, GuardQrScannerComponent.QrViewfinderState.Detected -> Color.White
                GuardQrScannerComponent.QrViewfinderState.Preheat -> MaterialTheme.colorScheme.primary
            }
        }

        Box(modifier = Modifier.fillMaxSize().alpha(kotlin.math.min(stateProgress, 0.7f)).background(Color.Black))

        Canvas(modifier = Modifier.fillMaxSize()) {
            val cornerSize = QrConstants.CornerSize.toPx()

            val cornerStyle = Stroke(
                pathEffect = PathEffect.cornerPathEffect(radius = QrConstants.CornerRadius.toPx()), width = QrConstants.CornerWidth.toPx()
            )

            if (paths.redrawConstant != stateRect) {
                paths.recalculate(cornerSize, stateRect)
            }

            paths.onEach { path ->
                drawPath(
                    path = path, color = stateColor.copy(alpha = 1f - stateProgress), style = cornerStyle
                )
            }
        }

        Box(
            modifier = Modifier
                .scale(stateProgress)
                .alpha(stateProgress)
                .align(Alignment.Center)
                // .fillMaxWidth()
                .padding(QrConstants.Padding)
                .clip(RoundedCornerShape(QrConstants.CornerRadius))
                .background(MaterialTheme.colorScheme.surface)
                .onGloballyPositioned {
                    if (paths.expandableHeight != it.size.height) {
                        paths.expandableHeight = it.size.height
                    }
                }
        ) {
            expandedContent()
        }
    }
}

private object QrConstants {
    const val SpringStiffness = 500f // 750f

    val DefaultRadius = 75.dp
    val CornerRadius = 12.dp
    val Padding = 16.dp
    val CornerSize = 20.dp
    val CornerWidth = 2.5.dp
}

internal class QrPaths {
    var expandableHeight = 0
    var redrawConstant = Rect(0f, 0f, 0f, 0f)

    private val topLeft = Path()
    private val topRight = Path()
    private val bottomLeft = Path()
    private val bottomRight = Path()

    fun onEach(func: (Path) -> Unit) {
        func(topLeft)
        func(topRight)
        func(bottomLeft)
        func(bottomRight)
    }

    fun recalculate(cornerSize: Float, stateBox: Rect) {
        redrawConstant = stateBox

        topLeft.apply {
            reset()
            moveTo(stateBox.topLeft.x + cornerSize, stateBox.topLeft.y)
            lineTo(stateBox.topLeft.x, stateBox.topLeft.y)
            lineTo(stateBox.topLeft.x, stateBox.topLeft.y + cornerSize)
        }

        topRight.apply {
            reset()
            moveTo(stateBox.topRight.x - cornerSize, stateBox.topLeft.y)
            lineTo(stateBox.topRight.x, stateBox.topRight.y)
            lineTo(stateBox.topRight.x, stateBox.topLeft.y + cornerSize)
        }

        bottomLeft.apply {
            reset()
            moveTo(stateBox.bottomLeft.x + cornerSize, stateBox.bottomLeft.y)
            lineTo(stateBox.bottomLeft.x, stateBox.bottomLeft.y)
            lineTo(stateBox.bottomLeft.x, stateBox.bottomLeft.y - cornerSize)
        }

        bottomRight.apply {
            reset()
            moveTo(stateBox.bottomRight.x - cornerSize, stateBox.bottomRight.y)
            lineTo(stateBox.bottomRight.x, stateBox.bottomRight.y)
            lineTo(stateBox.bottomRight.x, stateBox.bottomRight.y - cornerSize)
        }
    }
}