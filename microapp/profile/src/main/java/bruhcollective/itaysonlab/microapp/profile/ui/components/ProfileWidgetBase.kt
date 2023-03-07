package bruhcollective.itaysonlab.microapp.profile.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.jetisteam.uikit.LocalSteamTheme
import bruhcollective.itaysonlab.jetisteam.uikit.partialShapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileWidgetBase(
    firstItem: Boolean,
    lastItem: Boolean,
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = when {
        firstItem -> MaterialTheme.partialShapes.largeTopShape
        lastItem -> MaterialTheme.partialShapes.largeBottomShape
        else -> RectangleShape
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = LocalSteamTheme.current.colorShowcaseHeader),
        content = content
    )

    if (lastItem.not()) {
        Divider(color = LocalSteamTheme.current.btnBackground, modifier = Modifier.padding(horizontal = 16.dp))
    }
}