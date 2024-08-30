package bruhcollective.itaysonlab.cobalt.profile.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.ui.font.robotoMonoFontFamily
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileWidget

@Composable
fun ProfileWidgetPortal(
    widget: ProfileWidget
) {
    when (widget) {
        is ProfileWidget.FavoriteGame -> {
            FavoriteGameProfileWidget(widget)
        }

        is ProfileWidget.GameCollector -> {
            DefaultPwCard(
                title = stringResource(id = R.string.profile_widget_game_collector)
            ) {
                GameCollectorProfileWidget(widget)
            }
        }

        is ProfileWidget.Unknown -> {
            DefaultPwCard(
                title = stringResource(id = R.string.profile_widget_unknown)
            ) {
                Text(text = widget.toString())
            }
        }
    }
}

@Composable
fun DefaultPwCard(
    title: String,
    inner: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = remember(title) { title.uppercase() },
            fontFamily = robotoMonoFontFamily,
            style = MaterialTheme.typography.labelMedium
        )

        inner()
    }
}