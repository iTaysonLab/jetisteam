package bruhcollective.itaysonlab.jetisteam.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Edit
import androidx.compose.material.icons.sharp.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.profile.ProfileComponent
import bruhcollective.itaysonlab.jetisteam.profile.components.ProfileActionsStrip
import bruhcollective.itaysonlab.jetisteam.profile.components.ProfileHeader
import bruhcollective.itaysonlab.jetisteam.profile.components.ProfilePlayingCard
import bruhcollective.itaysonlab.jetisteam.ui.components.CobaltDivider
import bruhcollective.itaysonlab.jetisteam.ui.font.robotoMonoFontFamily
import bruhcollective.itaysonlab.jetisteam.ui.font.rubikFontFamily
import bruhcollective.itaysonlab.jetisteam.ui.theme.backgroundEmphasis
import bruhcollective.itaysonlab.ksteam.models.persona.Persona
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ExperimentalToolbarApi
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalToolbarApi::class)
@Composable
fun ProfileScreen(
    topPadding: Dp,
    component: ProfileComponent
) {
    val persona by component.persona.collectAsState()
    val personaEquipment by component.personaEquipment.collectAsState()

    val collapsingScaffold = rememberCollapsingToolbarScaffoldState()

    LaunchedEffect(persona) {
        collapsingScaffold.toolbarState.expand(duration = 0)
    }

    CollapsingToolbarScaffold(
        toolbar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .parallax(ratio = 1f)
            ) {
                Box(Modifier.alpha(collapsingScaffold.toolbarState.progress)) {
                    ProfileHeader(
                        persona = persona,
                        equipment = personaEquipment,
                        topPadding = topPadding
                    )
                }

                CobaltDivider(padding = 0.dp, modifier = Modifier.align(Alignment.BottomCenter))
            }

            Column(
                modifier = Modifier
                    .pin()
                    .alpha(1f - collapsingScaffold.toolbarState.progress)
            ) {
                TopAppBar(
                    title = {
                        Text(text = persona.name.uppercase(), fontFamily = robotoMonoFontFamily)
                    },
                    windowInsets = WindowInsets(top = topPadding),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )

                CobaltDivider(padding = 0.dp)
            }
        },
        state = collapsingScaffold,
        modifier = Modifier.fillMaxSize(),
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed
    ) {
        LazyColumn(
            Modifier.fillMaxSize()
        ) {
            item {
                ProfileActionsStrip()
                CobaltDivider(padding = 0.dp)
            }

            item {
                ProfilePlayingCard(persona.onlineStatus, persona.ingame)
                CobaltDivider(padding = 0.dp)
            }
        }
    }
}