package bruhcollective.itaysonlab.cobalt.ui.components

import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

@Composable
internal fun M3ECardListItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingContent: (@Composable () -> Unit)? = null,
    headlineContent: @Composable () -> Unit,
    supportingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    overlineContent: (@Composable () -> Unit)? = null,
    shape: Shape = MaterialTheme.shapes.extraSmall,
    colors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
) {
    Card(
        modifier = modifier,
        colors = colors,
        shape = shape,
        onClick = onClick
    ) {
        ListItem(
            leadingContent = leadingContent,
            headlineContent = headlineContent,
            supportingContent = supportingContent,
            trailingContent = trailingContent,
            overlineContent = overlineContent,
            colors = ListItemDefaults.colors(
                // leadingIconColor = MaterialTheme.colorScheme.primary,
                containerColor = Color.Transparent
            )
        )
    }
}

internal object M3ECardListItemShapes {
    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @get:Composable @get:ReadOnlyComposable val top get() = MaterialTheme.shapes.largeIncreased.copy(bottomStart = MaterialTheme.shapes.extraSmall.bottomStart, bottomEnd = MaterialTheme.shapes.extraSmall.bottomEnd)

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @get:Composable @get:ReadOnlyComposable val bottom get() = MaterialTheme.shapes.largeIncreased.copy(topStart = MaterialTheme.shapes.extraSmall.topStart, topEnd = MaterialTheme.shapes.extraSmall.topEnd)

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    @ReadOnlyComposable
    fun shape(top: Boolean, bottom: Boolean): Shape {
        return when {
            top && bottom -> MaterialTheme.shapes.largeIncreased
            top -> M3ECardListItemShapes.top
            bottom -> M3ECardListItemShapes.bottom
            else -> MaterialTheme.shapes.extraSmall
        }
    }
}