package bruhcollective.itaysonlab.jetisteam.uikit.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.jetisteam.uikit.R
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.material3.Material3RichText
import com.halilibo.richtext.ui.string.RichTextStringStyle

@Composable
fun ExpandableRichText(
    modifier: Modifier = Modifier,
    markdown: String,
    textColor: Color,
    backgroundColor: Color,
) {
    val isExpanded = rememberSaveable {
        mutableStateOf(false)
    }

    val spinDegree by animateFloatAsState(targetValue = if (isExpanded.value) 180f else 0f)
    val spinFloat by animateFloatAsState(targetValue = if (isExpanded.value) 0f else 1f)

    Column(modifier) {
        Material3RichText(Modifier.animateContentSize().drawWithContent {
            drawContent()
            drawRect(Brush.verticalGradient(colors = listOf(
                Color.Transparent, backgroundColor
            )), alpha = spinFloat)
        }.fillMaxWidth().let {
            if (isExpanded.value) it else it.heightIn(max = 200.dp)
        }, style = RichTextStyle(stringStyle = RichTextStringStyle(
            linkStyle = SpanStyle(
                color = MaterialTheme.colorScheme.primary
            )
        ))) {
            Markdown(content = markdown)
        }

        TextButton(
            onClick = { isExpanded.value = !isExpanded.value },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.textButtonColors(contentColor = textColor)
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowDownward,
                contentDescription = null,
                modifier = Modifier.rotate(
                    degrees = spinDegree
                )
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = stringResource(
                    id = if (isExpanded.value) {
                        R.string.text_shorten
                    } else {
                        R.string.text_expand
                    }
                )
            )
        }
    }
}