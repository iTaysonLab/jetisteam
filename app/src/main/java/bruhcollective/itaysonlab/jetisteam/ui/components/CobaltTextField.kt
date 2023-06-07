package bruhcollective.itaysonlab.jetisteam.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetisteam.ui.font.robotoMonoFontFamily

@Composable
fun CobaltTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingContent: (@Composable () -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }

    val textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
    val focused by interactionSource.collectIsFocusedAsState()

    val labelAlpha by animateFloatAsState(targetValue = if (focused) 1f else 0.5f, label = "CobaltTextField: label alpha")

    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle,
        singleLine = singleLine,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        interactionSource = interactionSource,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        decorationBox = { innerTextField ->
            CobaltTextFieldContent(label, placeholder, value, textStyle, labelAlpha, innerTextField)
        }
    )
}

@Composable
private fun CobaltTextFieldContent(
    label: String,
    placeholder: String,
    value: String,
    textStyle: TextStyle,
    labelAlpha: Float,
    innerTextField: @Composable () -> Unit
) {
    Column {
        Text(
            text = remember { label.uppercase() },
            fontFamily = robotoMonoFontFamily,
            style = textStyle.copy(fontSize = 12.sp),
            modifier = Modifier.alpha(labelAlpha)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box {
            if (value.isEmpty()) {
                Text(text = placeholder, style = textStyle, modifier = Modifier.alpha(0.5f))
            }

            innerTextField()
        }
    }
}