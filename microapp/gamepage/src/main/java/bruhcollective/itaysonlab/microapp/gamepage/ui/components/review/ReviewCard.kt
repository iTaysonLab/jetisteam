package bruhcollective.itaysonlab.microapp.gamepage.ui.components.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EmojiEmotions
import androidx.compose.material.icons.rounded.ThumbDown
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetisteam.ext.roundUpTo
import bruhcollective.itaysonlab.jetisteam.models.Player
import bruhcollective.itaysonlab.jetisteam.models.Review
import bruhcollective.itaysonlab.jetisteam.uikit.components.ExpandableRichText
import bruhcollective.itaysonlab.jetisteam.util.DateUtil
import bruhcollective.itaysonlab.jetisteam.util.SteamBbToMarkdown
import bruhcollective.itaysonlab.microapp.gamepage.R
import coil.compose.AsyncImage
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.material3.Material3RichText
import com.halilibo.richtext.ui.string.RichTextStringStyle

@Composable
fun ReviewCard(
    review: Review,
    reviewer: Player,
    modifier: Modifier = Modifier
) {
    val reviewColor = if (review.positiveReview) {
        ReviewCardColors.ReviewGradientPositiveIcon
    } else {
        ReviewCardColors.ReviewGradientNegativeIcon
    }

    val compositedColor = reviewColor.copy(alpha = 0.25f)
        .compositeOver(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
    val compositedColorLight = reviewColor.copy(alpha = 0.5f)
        .compositeOver(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
    val compositedColorOverLight = reviewColor.copy(alpha = 0.85f)
        .compositeOver(MaterialTheme.colorScheme.surfaceColorAtElevation(16.dp))

    val reviewModel = rememberSaveable(review.review) {
        SteamBbToMarkdown.bbcodeToMarkdown(review.review)
    }

    val headerSubtitle = remember(review) {
        if (review.author.reviewPlaytime == review.author.totalPlaytime) {
            HeaderSubtitle(
                R.string.gamepage_reviews_header to arrayOf(
                    (review.author.totalPlaytime / 60f).roundUpTo(1).toString()
                )
            )
        } else {
            HeaderSubtitle(
                R.string.gamepage_reviews_header_diff to arrayOf(
                    (review.author.totalPlaytime / 60f).roundUpTo(1).toString(),
                    (review.author.reviewPlaytime / 60f).roundUpTo(1).toString()
                )
            )
        }
    }

    Card(
        modifier, colors = CardDefaults.cardColors(
            containerColor = compositedColor
        )
    ) {
        Column(
            Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .background(reviewColor.copy(alpha = 0.35f))
                        .padding(12.dp)
                ) {
                    Icon(
                        imageVector = if (review.positiveReview) {
                            Icons.Rounded.ThumbUp
                        } else {
                            Icons.Rounded.ThumbDown
                        }, contentDescription = null, tint = reviewColor
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = stringResource(
                            id = if (review.positiveReview) {
                                R.string.gamepage_reviews_recommend
                            } else {
                                R.string.gamepage_reviews_not_recommend
                            }
                        ), color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = stringResource(headerSubtitle.resId, *headerSubtitle.args),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (reviewModel.length > 240) {
                ExpandableRichText(
                    markdown = reviewModel,
                    textColor = reviewColor,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    backgroundColor = compositedColor
                )

                Spacer(modifier = Modifier.height(8.dp))
            } else {
                Material3RichText(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    style = RichTextStyle(
                        stringStyle = RichTextStringStyle(
                            linkStyle = SpanStyle(
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    )
                ) {
                    Markdown(
                        content = reviewModel
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            Column(
                modifier = Modifier
                    .background(compositedColorLight)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = reviewer.avatarmedium,
                        contentDescription = null,
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .size(42.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = reviewer.personaname,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 15.sp
                        )

                        Text(
                            text = remember(review.createdAt) {
                                DateUtil.formatDateTimeToLocale(review.createdAt * 1000)
                            },
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                if (review.markedAsHelpful != 0 || review.markedAsFunny != 0) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (review.markedAsHelpful != 0) {
                            ReviewChip(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.ThumbUp,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }, content = {
                                    Text(
                                        text = review.markedAsHelpful.toString(),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }, background = compositedColorOverLight
                            )
                        }

                        if (review.markedAsFunny != 0) {
                            ReviewChip(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.EmojiEmotions,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }, content = {
                                    Text(
                                        text = review.markedAsFunny.toString(),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }, background = compositedColorOverLight
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewChip(
    background: Color,
    icon: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = background
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            icon()
            content()
        }
    }
}

private object ReviewCardColors {
    val ReviewGradientPositiveOnDark = Color(26, 50, 70)
    val ReviewGradientNegativeOnDark = Color(80, 36, 34)

    val ReviewGradientPositiveIcon = Color(116, 178, 224)
    val ReviewGradientNegativeIcon = Color(208, 104, 101)
}

@JvmInline
private value class HeaderSubtitle(
    private val packed: Pair<Int, Array<Any>>
) {
    val resId get() = packed.first
    val args get() = packed.second
}