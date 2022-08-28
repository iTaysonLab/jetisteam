package bruhcollective.itaysonlab.microapp.notifications.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.jetisteam.usecases.GetNotifications
import coil.compose.AsyncImage

@Composable
fun Notification(
    notification: GetNotifications.Notification
) {
    Row(Modifier.padding(horizontal = 16.dp)) {
        AsyncImage(model = notification.icon, contentDescription = null, modifier = Modifier.size(72.dp))
        Column {
            Text(text = notification.title)
            Text(text = notification.description)
        }
    }
}