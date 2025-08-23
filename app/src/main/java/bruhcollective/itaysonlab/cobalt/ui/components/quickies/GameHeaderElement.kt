package bruhcollective.itaysonlab.cobalt.ui.components.quickies

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest

private val GameHeaderHeight = 240.dp

/**
 * Game header element, which is a confined-size block with background, foreground (usually a logo), drag handler and bottom decoration
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GameHeaderElement(
    backgroundImageUrl: String,
    foregroundImageUrl: String,
    foregroundImageAlt: String,
    modifier: Modifier = Modifier,
    additionalContent: (@Composable BoxScope.() -> Unit)? = null,
    additionalContentHeader: Dp = 0.dp,
) {
    Box(modifier = modifier.height(GameHeaderHeight)) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(backgroundImageUrl)
                .build(), contentDescription = null, modifier = Modifier
                .height(GameHeaderHeight)
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.linearGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.8f), Color.Transparent),
                            start = Offset(x = size.width / 2f, y = 0f),
                            end = Offset(x = size.width / 2f, y = size.height),
                        )
                    )
                }, contentScale = ContentScale.Crop
        )

        Box(Modifier.align(Alignment.Center).fillMaxSize().padding(
            top = 48.dp,
            bottom = if (additionalContent != null) additionalContentHeader else 48.dp
        )) {
            if (foregroundImageUrl == "") {
                Text(
                    text = foregroundImageAlt,
                    color = Color.White,
                    fontSize = 21.sp,
                    modifier = Modifier.align(Alignment.Center).padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                AsyncImage(
                    model = foregroundImageUrl,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center).heightIn(min = 0.dp, max = 100.dp),
                    contentScale = ContentScale.FillHeight
                )
            }
        }

        BottomSheetDefaults.DragHandle(modifier = Modifier.align(Alignment.TopCenter))

        additionalContent?.invoke(this)

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clip(
                    MaterialTheme.shapes.extraLarge.copy(
                        bottomStart = CornerSize(0.dp),
                        bottomEnd = CornerSize(0.dp)
                    )
                )
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .fillMaxWidth()
                .height(16.dp)
        )
    }
}