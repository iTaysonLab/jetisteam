package bruhcollective.itaysonlab.microapp.notifications.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Article
import androidx.compose.material.icons.rounded.CardGiftcard
import androidx.compose.material.icons.rounded.Checklist
import androidx.compose.material.icons.rounded.Inventory
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bruhcollective.itaysonlab.jetisteam.util.DateUtil
import bruhcollective.itaysonlab.ksteam.models.notifications.Notification
import bruhcollective.itaysonlab.ksteam.models.persona.Persona
import bruhcollective.itaysonlab.microapp.notifications.R
import coil.compose.AsyncImage

@Composable
internal fun NotificationRenderer(
    notification: Notification,
    onClick: () -> Unit,
) {
    when (notification) {
        is Notification.FriendRequest -> FriendRequestNotification(notification = notification, onClick = onClick)
        is Notification.Gift -> GiftNotification(notification = notification, onClick = onClick)
        is Notification.Item -> ItemNotification(notification = notification, onClick = onClick)
        is Notification.Promotion -> PromoNotification(notification = notification, onClick = onClick)
        is Notification.WishlistSale -> WishlistSaleNotification(notification = notification, onClick = onClick)
        is Notification.Unknown -> {
            Text("Unknown notification: ${notification.rawJsonData}")
        }
        else -> {
            Text("Unknown notification: $notification")
        }
    }
}

@Composable
internal fun Notification(
    timestamp: Int,
    imageUrl: String,
    iconVector: ImageVector,
    iconType: String,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    val formattedDate = remember(timestamp) {
        DateUtil.formatDateTimeToLocale(timestamp * 1000L)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .size(64.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)),
                    error = ColorPainter(MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)),
                )
            } else {
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .size(64.dp)
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Notifications,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = iconVector, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text(text = iconType, fontSize = 13.sp)
                    Text(text = formattedDate, modifier = Modifier.alpha(0.7f), fontSize = 13.sp)
                }

                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(text = description, fontSize = 13.sp, lineHeight = 18.sp)
            }
        }
    }
}

// Types

@Composable
private fun FriendRequestNotification(
    notification: Notification.FriendRequest,
    onClick: () -> Unit
) {
    val persona = notification.requestor.collectAsStateWithLifecycle(initialValue = Persona.Unknown).value

    Notification(
        timestamp = notification.timestamp,
        imageUrl = persona.avatar.medium,
        iconVector = Icons.Rounded.PersonAdd,
        iconType = stringResource(id = R.string.notifications_type_friend),
        title = persona.name,
        description = stringResource(id = R.string.notifications_type_friend_desc_awaiting),
        onClick = onClick
    )
}

@Composable
private fun GiftNotification(
    notification: Notification.Gift,
    onClick: () -> Unit
) {
    val persona = notification.gifter.collectAsStateWithLifecycle(initialValue = Persona.Unknown).value

    Notification(
        timestamp = notification.timestamp,
        imageUrl = persona.avatar.medium,
        iconVector = Icons.Rounded.CardGiftcard,
        iconType = stringResource(id = R.string.notifications_type_gift),
        title = persona.name,
        description = stringResource(id = R.string.notifications_type_gift_desc),
        onClick = onClick
    )
}

@Composable
private fun ItemNotification(
    notification: Notification.Item,
    onClick: () -> Unit
) {
    // TODO
    Notification(
        timestamp = notification.timestamp,
        imageUrl = "",
        iconVector = Icons.Rounded.Inventory,
        iconType = stringResource(id = R.string.notifications_type_item),
        title = stringResource(id = R.string.notifications_type_item_title),
        description = "Unknown",
        onClick = onClick
    )
}

@Composable
private fun PromoNotification(
    notification: Notification.Promotion,
    onClick: () -> Unit
) {
    Notification(
        timestamp = notification.timestamp,
        imageUrl = notification.iconUrl,
        iconVector = Icons.Rounded.Article,
        iconType = stringResource(id = R.string.notifications_type_promo),
        title = notification.title,
        description = notification.description,
        onClick = onClick
    )
}

@Composable
private fun WishlistSaleNotification(
    notification: Notification.WishlistSale,
    onClick: () -> Unit
) {
    Notification(
        timestamp = notification.timestamp,
        imageUrl = "", // TODO
        iconVector = Icons.Rounded.Checklist,
        iconType = stringResource(id = R.string.notifications_type_wishlist),
        title = if (notification.isMultipleItemsOnSale) {
            stringResource(id = R.string.notifications_type_wishlist_multiple_title)
        } else {
            notification.appSummary?.name.orEmpty()
        },
        description = if (notification.isMultipleItemsOnSale) {
            stringResource(id = R.string.notifications_type_wishlist_multiple_desc)
        } else {
            stringResource(id = R.string.notifications_type_wishlist_desc, "TODO")
        },
        onClick = onClick
    )
}