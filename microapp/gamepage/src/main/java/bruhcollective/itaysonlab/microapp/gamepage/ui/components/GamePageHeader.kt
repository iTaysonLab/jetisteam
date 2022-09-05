package bruhcollective.itaysonlab.microapp.gamepage.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetisteam.usecases.GetGamePage
import coil.compose.AsyncImage
import bruhcollective.itaysonlab.microapp.gamepage.R

@Composable
internal fun GamePageHeader(
    backgroundUrl: String?,
    gameTitle: String,
    reviews: GetGamePage.ReviewsInfo,
    tags: List<Pair<String, Int>>
) {
    val surfaceColor = MaterialTheme.colorScheme.surface

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {

        AsyncImage(
            model = backgroundUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()
                    drawRect(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0f to Color.Transparent,
                                1f to surfaceColor,
                            )
                        )
                    )
                },
            contentScale = ContentScale.Crop
        )

        Column(Modifier.padding(bottom = 8.dp)) {
            Spacer(modifier = Modifier.height(150.dp))

            Text(
                text = gameTitle,
                fontSize = 28.sp,
                lineHeight = 31.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                item {
                    Button(
                        onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(32.dp),
                            contentColor = Color.White
                        ), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Icon(imageVector = Icons.Rounded.Star, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = reviews.scoreLabel)
                            Text(text = stringResource(id = R.string.gamepage_review_coef, "${reviews.positivePercent}%", reviews.reviews), fontWeight = FontWeight.Normal)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }

                items(tags) { tag ->
                    Button(
                        onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
                            contentColor = Color.White
                        ), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(text = tag.first)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}