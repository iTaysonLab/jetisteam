package bruhcollective.itaysonlab.microapp.gamepage.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.BlurTransformation

@Composable
internal fun GamePageHeader(
    background: String,
    backgroundBlurred: Boolean,
    logoUrl: String,
    useTextLogo: Boolean,
    name: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(background)
                .transformations(
                    if (backgroundBlurred) listOf(
                        BlurTransformation(LocalContext.current, 20f, 4f)
                    ) else emptyList()
                )
                .build(), contentDescription = null, modifier = Modifier
                .height(300.dp)
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

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (useTextLogo) {
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 21.sp,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                AsyncImage(
                    model = logoUrl,
                    contentDescription = null,
                    modifier = Modifier.heightIn(min = 0.dp, max = 100.dp),
                    contentScale = ContentScale.FillHeight
                )
            }
        }
    }
}