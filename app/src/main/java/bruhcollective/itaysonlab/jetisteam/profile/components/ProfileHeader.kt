package bruhcollective.itaysonlab.jetisteam.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.ksteam.models.persona.Persona
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileEquipment
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun BoxScope.ProfileHeader(
    persona: Persona,
    equipment: ProfileEquipment,
    topPadding: Dp
) {
    val surfaceColor = MaterialTheme.colorScheme.surface

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(equipment.background?.imageLarge)
            .build(), contentDescription = null, modifier = Modifier
            .fillMaxSize()
            .drawWithContent {
                drawContent()
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            surfaceColor.copy(alpha = 0.2f),
                            surfaceColor,
                        ),
                        start = Offset(x = size.width / 2f, y = 0f),
                        end = Offset(
                            x = size.width / 2f,
                            y = size.height
                        ),
                    )
                )
            }, contentScale = ContentScale.Crop
    )

    Column(
        Modifier
            .align(Alignment.BottomStart)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(
            modifier = Modifier
                .height(64.dp)
                .padding(top = topPadding)
        )

        AsyncImage(
            model = equipment.animatedAvatar?.movieMp4 ?: persona.avatar.full,
            contentDescription = null,
            modifier = Modifier
                .size(72.dp)
        )

        Text(
            text = persona.name,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}