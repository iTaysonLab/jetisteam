package bruhcollective.itaysonlab.cobalt.ui.components

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

@Composable
fun InlineMonoButton(
    icon: @Composable () -> Unit,
    title: String,
    onClick: () -> Unit,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    showChevron: Boolean = false
) {
    TextButton(
        onClick = onClick,
        shape = RectangleShape,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        colors = colors
    ) {
        icon()

        Spacer(modifier = Modifier.width(16.dp))

        Text(text = remember { title.uppercase() }, fontFamily = robotoMonoFontFamily)

        Spacer(modifier = Modifier.weight(1f))

        if (showChevron) {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.alpha(0.5f)
            )
        }
    }
}