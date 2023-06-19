package bruhcollective.itaysonlab.jetisteam.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.Message
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material.icons.sharp.Store
import androidx.compose.material.icons.sharp.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.jetisteam.navigation.CobaltContainerComponent
import bruhcollective.itaysonlab.jetisteam.ui.theme.backgroundEmphasis
import kotlinx.collections.immutable.ImmutableList

@Composable
fun CobaltNavigationBar(
    items: ImmutableList<CobaltContainerComponent.NavigationItem>,
    selectedItem: CobaltContainerComponent.NavigationItem,
    onSelected: (CobaltContainerComponent.NavigationItem) -> Unit
) {
    val offsetDp by animateDpAsState(
        targetValue = if (FloatingNavigationBarHolder.shouldNavigationBarBeVisible) 0.dp else 150.dp,
        label = "",
        animationSpec = tween()
    )

    Column(Modifier.background(MaterialTheme.colorScheme.background).navigationBarsPadding()) {
        CobaltDivider(padding = 0.dp)

        Row(
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedItem == item

                val colorFg by animateColorAsState(targetValue = if (isSelected) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                }, label = "")

                val colorBg by animateColorAsState(targetValue = if (isSelected) {
                    MaterialTheme.colorScheme.backgroundEmphasis
                } else {
                    MaterialTheme.colorScheme.background
                }, label = "")

                Box(
                    modifier = Modifier
                        .background(colorBg)
                        .weight(1f)
                        .clickable {
                            onSelected(item)
                        }
                    , contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (item) {
                            CobaltContainerComponent.NavigationItem.Home -> Icons.Sharp.Home
                            CobaltContainerComponent.NavigationItem.MyProfile -> Icons.Sharp.Person
                        },
                        contentDescription = null,
                        modifier = Modifier.padding(16.dp),
                        tint = colorFg
                    )
                }

                if (index != items.lastIndex) {
                    CobaltVerticalDivider()
                }
            }
        }

        CobaltDivider(padding = 0.dp)
    }

    /*
    FloatingBottomBar(
        expanded = false,
        selectedItem = selectedItem,
        items = items,
        expandedContent = {},
        modifier = Modifier.offset {
            IntOffset(x = 0, y = offsetDp.toPx().toInt())
        }
    )*/
}