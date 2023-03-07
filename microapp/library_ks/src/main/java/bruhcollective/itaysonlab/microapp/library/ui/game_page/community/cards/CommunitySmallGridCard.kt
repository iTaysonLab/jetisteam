package bruhcollective.itaysonlab.microapp.library.ui.game_page.community.cards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun CommunitySmallGridCard(
    modifier: Modifier = Modifier,
    background: @Composable () -> Unit,
    icon: @Composable () -> Unit
) {
    CommunityCardBase(modifier) {
        Box(Modifier.fillMaxSize()) {
            background()

            Column(Modifier.fillMaxWidth().align(Alignment.TopCenter).padding(8.dp)) {
                icon()
            }
        }
    }
}