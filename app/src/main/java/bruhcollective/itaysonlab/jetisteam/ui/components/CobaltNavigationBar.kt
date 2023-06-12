package bruhcollective.itaysonlab.jetisteam.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.Message
import androidx.compose.material.icons.sharp.VideoLibrary
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
fun CobaltNavigationBar() {
    var selectedItem by remember {
        mutableIntStateOf(0)
    }

    val items = remember {
        listOf(
            BottomBarItem.Icon({ Icons.Sharp.Home }, 0, "", { selectedItem = 0 }),
            BottomBarItem.Icon({ Icons.Sharp.VideoLibrary }, 0, "", { selectedItem = 1 }),
            BottomBarItem.Icon({ Icons.Sharp.Message }, 0, "", { selectedItem = 2 }),
        )
    }

    val offsetDp by animateDpAsState(
        targetValue = if (FloatingNavigationBarHolder.shouldNavigationBarBeVisible) 0.dp else 150.dp,
        label = "",
        animationSpec = tween()
    )

    FloatingBottomBar(
        expanded = false,
        selectedItem = selectedItem,
        items = items,
        expandedContent = {},
        modifier = Modifier.offset {
            IntOffset(x = 0, y = offsetDp.toPx().toInt())
        }
    )
}