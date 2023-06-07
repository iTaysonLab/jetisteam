package bruhcollective.itaysonlab.jetisteam.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.Message
import androidx.compose.material.icons.sharp.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CobaltNavigationBar() {
    var selectedItem by remember {
        mutableIntStateOf(0)
    }

    val items = remember {
        listOf(
            Icons.Sharp.Home, Icons.Sharp.VideoLibrary, Icons.Sharp.Message
        )
    }

    Column(
        Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        CobaltDivider(padding = 0.dp)

        IndicatorBehindScrollableTabRow(
            selectedTabIndex = selectedItem,
            containerColor = MaterialTheme.colorScheme.background,
            indicator = { tabPositions ->
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedItem])
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.onSurface)
                )
            },
            edgePadding = 0.dp,
            modifier = Modifier.fillMaxWidth(),
            paddingBetweenTabs = 0.dp
        ) {
            items.forEachIndexed { index, item ->
                NoRippleTab(
                    selected = selectedItem == index,
                    onClick = { selectedItem = index },
                    selectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ) {
                    Row {
                        Icon(
                            imageVector = item, contentDescription = null, modifier = Modifier
                                .padding(vertical = 20.dp, horizontal = 20.dp)
                        )

                        CobaltVerticalDivider()
                    }
                }
            }
        }

        CobaltDivider(padding = 0.dp)
    }
}