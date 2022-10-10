package bruhcollective.itaysonlab.jetisteam.uikit.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheetHandle(
    modifier: Modifier = Modifier
) {
    Divider(
        modifier = modifier.width(32.dp).padding(vertical = 14.dp).clip(CircleShape),
        thickness = 4.dp,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.4f)
    )
}