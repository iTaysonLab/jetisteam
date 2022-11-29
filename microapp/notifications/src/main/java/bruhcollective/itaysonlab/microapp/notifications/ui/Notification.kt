package bruhcollective.itaysonlab.microapp.notifications.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetisteam.usecases.GetNotifications
import bruhcollective.itaysonlab.microapp.notifications.R
import coil.compose.AsyncImage
import steam.steamnotification.SteamNotificationType

@Composable
fun Notification(
    notification: GetNotifications.Notification,
    onClick: () -> Unit
) {
    val typeHeader = remember(notification.type) {
        when (notification.type) {
            SteamNotificationType.Gift -> Icons.Rounded.CardGiftcard to R.string.notifications_type_gift
            SteamNotificationType.Comment -> Icons.Rounded.Comment to R.string.notifications_type_comment
            SteamNotificationType.Item -> Icons.Rounded.Inventory to R.string.notifications_type_item
            SteamNotificationType.FriendInvite -> Icons.Rounded.PersonAdd to R.string.notifications_type_friend
            SteamNotificationType.MajorSale -> Icons.Rounded.Discount to R.string.notifications_type_discount
            SteamNotificationType.PreloadAvailable -> Icons.Rounded.Download to R.string.notifications_type_preload
            SteamNotificationType.Wishlist -> Icons.Rounded.Checklist to R.string.notifications_type_wishlist
        }
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
            AsyncImage(
                model = notification.icon,
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
                    Icon(
                        imageVector = typeHeader.first,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Text(text = stringResource(id = typeHeader.second), fontSize = 13.sp)
                }

                Text(text = notification.title.get(LocalContext.current), color = MaterialTheme.colorScheme.onSurface)
                Text(text = notification.description.get(LocalContext.current), fontSize = 13.sp)
            }
        }
    }
}