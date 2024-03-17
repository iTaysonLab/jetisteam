package bruhcollective.itaysonlab.jetisteam.guard.instance

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetisteam.R
import bruhcollective.itaysonlab.jetisteam.guard.GuardUtils
import bruhcollective.itaysonlab.jetisteam.ui.components.FullscreenError
import bruhcollective.itaysonlab.jetisteam.ui.components.FullscreenLoading
import bruhcollective.itaysonlab.jetisteam.ui.components.FullscreenPlaceholder
import bruhcollective.itaysonlab.ksteam.guard.models.ConfirmationListState
import bruhcollective.itaysonlab.ksteam.guard.models.MobileConfirmationItem
import coil.compose.AsyncImage

@Composable
internal fun GuardCodeAndConfirmationsPage(
    confirmationState: ConfirmationListState,
    modifier: Modifier = Modifier
) {
    when (confirmationState) {
        is ConfirmationListState.Loading -> {
            FullscreenLoading()
        }

        is ConfirmationListState.NetworkError -> {
            FullscreenError(onReload = { /*TODO*/ }, exception = confirmationState.e)
        }

        is ConfirmationListState.Error -> {
            FullscreenPlaceholder(
                title = confirmationState.message,
                text = confirmationState.detail
            )
        }

        is ConfirmationListState.Success -> {
            if (confirmationState.conf.isNotEmpty()) {
                LazyColumn(
                    modifier = modifier,
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(confirmationState.conf) {
                        ConfirmationCard(confirmation = it, onClick = {
                            // onConfirmationClicked(it)
                        })
                    }
                }
            } else {
                FullscreenPlaceholder(
                    title = stringResource(id = R.string.guard_confirmations_empty),
                    text = stringResource(id = R.string.guard_confirmations_empty_desc),
                )
            }
        }
    }
}

@Composable
private fun ConfirmationCard(
    confirmation: MobileConfirmationItem,
    onClick: () -> Unit
) {
    val formattedDate = remember(confirmation.creationTime) {
        GuardUtils.formatDateTimeToLocale(confirmation.creationTime * 1000L)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(16.dp)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = confirmation.icon,
                contentDescription = null,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .size(64.dp),
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp))
            )

            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = confirmation.typeName, fontSize = 13.sp)
                    Text(text = formattedDate, modifier = Modifier.alpha(0.7f), fontSize = 13.sp)
                }

                Text(
                    text = confirmation.headline,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(text = remember(confirmation.summary) {
                    confirmation.summary.joinToString(separator = "\n")
                }, fontSize = 13.sp, lineHeight = 18.sp)
            }
        }
    }
}