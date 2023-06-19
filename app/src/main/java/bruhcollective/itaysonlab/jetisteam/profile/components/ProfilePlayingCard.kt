package bruhcollective.itaysonlab.jetisteam.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetisteam.R
import bruhcollective.itaysonlab.jetisteam.ui.font.robotoMonoFontFamily
import bruhcollective.itaysonlab.jetisteam.ui.font.rubikFontFamily
import bruhcollective.itaysonlab.jetisteam.ui.theme.backgroundEmphasis
import bruhcollective.itaysonlab.ksteam.models.enums.EPersonaState
import bruhcollective.itaysonlab.ksteam.models.persona.Persona

@Composable
fun ProfilePlayingCard(
    onlineStatus: EPersonaState,
    ingame: Persona.IngameStatus
) {
    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.backgroundEmphasis).padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = when (ingame) {
                is Persona.IngameStatus.NonSteam -> stringResource(id = R.string.profile_playing_non_steam)
                is Persona.IngameStatus.Steam -> stringResource(id = R.string.profile_playing_steam)
                Persona.IngameStatus.None -> "Status"
            }.uppercase(),
            fontFamily = robotoMonoFontFamily,
            style = MaterialTheme.typography.labelMedium
        )

        when (ingame) {
            is Persona.IngameStatus.NonSteam -> {
                Text(
                    text = ingame.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = rubikFontFamily,
                    fontSize = 20.sp
                )
            }

            is Persona.IngameStatus.Steam -> {
                Text(
                    text = ingame.appId.toString(),
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

            Persona.IngameStatus.None -> {
                Text(
                    text = onlineStatus.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = rubikFontFamily,
                    fontSize = 20.sp
                )
            }
        }
    }
}