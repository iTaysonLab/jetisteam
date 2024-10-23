package bruhcollective.itaysonlab.cobalt.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Edit
import androidx.compose.material.icons.sharp.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun ProfileActionsStrip(

) {
    LazyRow(
        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        item {
            TextButton(
                onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ), shape = RectangleShape, contentPadding = PaddingValues(16.dp)
            ) {
                Icon(imageVector = Icons.Sharp.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Edit profile".uppercase()
                )
            }
        }

        item {
            TextButton(
                onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ), shape = RectangleShape, contentPadding = PaddingValues(16.dp)
            ) {
                Icon(imageVector = Icons.Sharp.Share, contentDescription = null)
            }
        }

        item {

        }
    }
}