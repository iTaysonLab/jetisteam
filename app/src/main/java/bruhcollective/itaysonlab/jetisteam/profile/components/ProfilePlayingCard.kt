package bruhcollective.itaysonlab.jetisteam.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.cobalt.profile.components.status.StatusCardComponent
import bruhcollective.itaysonlab.jetisteam.R
import bruhcollective.itaysonlab.jetisteam.ui.font.robotoMonoFontFamily
import bruhcollective.itaysonlab.jetisteam.ui.font.rubikFontFamily
import bruhcollective.itaysonlab.jetisteam.ui.theme.backgroundEmphasis
import bruhcollective.itaysonlab.ksteam.models.persona.Persona
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState

@Composable
fun ProfilePlayingCard(
    component: StatusCardComponent
) {
    val ingame by component.status.subscribeAsState()

    val ingameTitle = when (ingame) {
        is Persona.IngameStatus.NonSteam -> stringResource(id = R.string.profile_playing_non_steam)
        is Persona.IngameStatus.Steam -> stringResource(id = R.string.profile_playing_steam)
        Persona.IngameStatus.None -> "Status"
    }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = remember(ingameTitle) { ingameTitle.uppercase() },
            fontFamily = robotoMonoFontFamily,
            style = MaterialTheme.typography.labelMedium
        )

        when (val ig = ingame) {
            is Persona.IngameStatus.NonSteam -> {
                NonSteamContent(ig)
            }

            is Persona.IngameStatus.Steam -> {
                SteamContent(component, ig)
            }

            Persona.IngameStatus.None -> {
                StatusContent()
            }
        }
    }
}

@Composable
private fun NonSteamContent(
    ingame: Persona.IngameStatus.NonSteam
) {
    Text(
        text = ingame.name,
        style = MaterialTheme.typography.titleLarge,
        fontFamily = rubikFontFamily,
        fontSize = 20.sp
    )
}

@Composable
private fun SteamContent(
    component: StatusCardComponent,
    ingame: Persona.IngameStatus.Steam
) {
    val steamAppSummary by component.appInformation.subscribeAsState()

    Text(
        text = steamAppSummary.toString(),
        style = MaterialTheme.typography.titleLarge,
        fontFamily = rubikFontFamily,
        fontSize = 20.sp
    )

    if (ingame.richPresence.isNotEmpty()) {
        Text(
            text = ingame.richPresence.entries.joinToString(),
            style = MaterialTheme.typography.titleLarge,
            fontFamily = rubikFontFamily,
            fontSize = 20.sp
        )
    }
}

@Composable
private fun StatusContent(

) {
    Text(
        text = "TODO: Online status",
        style = MaterialTheme.typography.titleLarge,
        fontFamily = rubikFontFamily,
        fontSize = 20.sp
    )
}