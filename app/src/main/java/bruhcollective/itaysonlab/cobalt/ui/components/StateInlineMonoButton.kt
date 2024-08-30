package bruhcollective.itaysonlab.cobalt.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.ui.font.robotoMonoFontFamily
import soup.compose.material.motion.animation.materialSharedAxisY
import soup.compose.material.motion.animation.rememberSlideDistance

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StateInlineMonoButton(
    icon: @Composable () -> Unit,
    title: String,
    onClick: () -> Unit,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    val slideDistance = rememberSlideDistance()

    TextButton(
        onClick = onClick,
        shape = RectangleShape,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        colors = colors,
        enabled = enabled
    ) {
        icon()

        Spacer(modifier = Modifier.width(16.dp))

        Text(text = remember { title.uppercase() }, fontFamily = robotoMonoFontFamily)

        Spacer(modifier = Modifier.weight(1f))

        AnimatedContent(targetState = isLoading, transitionSpec = {
            materialSharedAxisY(forward = true, slideDistance = slideDistance)
        }, label = "") { showSpinner ->
            if (showSpinner) {
                ResizableCircularIndicator(
                    indicatorSize = 24.dp,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.alpha(0.5f)
                )
            }
        }
    }
}