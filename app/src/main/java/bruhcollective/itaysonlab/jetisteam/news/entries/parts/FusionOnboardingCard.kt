package bruhcollective.itaysonlab.jetisteam.news.entries.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun FusionOnboardingCard(

) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Sharp.Home,
            contentDescription = null,
            modifier = Modifier.size(36.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )

        Text(text = "Welcome to the new Home", style = MaterialTheme.typography.headlineSmall)

        Text(
            text = "Your game news, combined with your friends activity.\n\nIf you don\'t like, you can revert to separate feeds in Settings.",
            style = MaterialTheme.typography.bodyMedium
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onSurface,
                contentColor = MaterialTheme.colorScheme.inverseOnSurface,
            ), shape = RectangleShape, modifier = Modifier.widthIn(min = 0.dp), contentPadding = PaddingValues(horizontal = 16.dp)) {
                Text(text = "Got it".uppercase())
            }

            FilledTonalButton(onClick = { /*TODO*/ }, colors = ButtonDefaults.filledTonalButtonColors(

            ), shape = RectangleShape, modifier = Modifier.widthIn(min = 0.dp), contentPadding = PaddingValues(horizontal = 16.dp)) {
                Text(text = "Learn more".uppercase())
            }
        }
    }
}