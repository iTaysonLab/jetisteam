package bruhcollective.itaysonlab.jetisteam.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.profile.components.header.ProfileHeaderComponent
import bruhcollective.itaysonlab.jetisteam.ui.components.CobaltDivider
import bruhcollective.itaysonlab.jetisteam.ui.font.robotoMonoFontFamily
import bruhcollective.itaysonlab.ksteam.models.persona.Persona
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileEquipment
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import me.onebone.toolbar.CollapsingToolbarScaffoldState
import me.onebone.toolbar.CollapsingToolbarScope
import me.onebone.toolbar.ExperimentalToolbarApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalToolbarApi::class)
@Composable
fun CollapsingToolbarScope.ProfileHeader(
    collapsingScaffold: CollapsingToolbarScaffoldState,
    component: ProfileHeaderComponent,
    topPadding: Dp
) {
    val personaTitle by component.title.subscribeAsState()

    LaunchedEffect(personaTitle) {
        if (collapsingScaffold.toolbarState.progress == 1f) {
            // Fix when toolbar is resizing
            collapsingScaffold.toolbarState.expand(duration = 0)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .parallax(ratio = 1f)
    ) {
        Box(Modifier.alpha(collapsingScaffold.toolbarState.progress)) {
            ProfileHeaderExpandedContent(component, topPadding)
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
                Text(text = remember(personaTitle) { personaTitle.uppercase() }, fontFamily = robotoMonoFontFamily)
            },
            windowInsets = WindowInsets(top = topPadding),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        CobaltDivider(padding = 0.dp)
    }
}

@Composable
fun BoxScope.ProfileHeaderExpandedContent(
    component: ProfileHeaderComponent,
    topPadding: Dp
) {
    val personaAvatar by component.staticAvatarUrl.subscribeAsState()
    val personaBackground by component.staticBackgroundUrl.subscribeAsState()
    val personaTitle by component.title.subscribeAsState()

    val surfaceColor = MaterialTheme.colorScheme.surface

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(personaBackground)
            .build(), contentDescription = null, modifier = Modifier
            .fillMaxSize()
            .drawWithContent {
                drawContent()
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            surfaceColor.copy(alpha = 0.1f),
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
                .height(96.dp)
                .padding(top = topPadding)
        )

        AsyncImage(
            model = personaAvatar,
            contentDescription = null,
            modifier = Modifier
                .size(72.dp)
        )

        Text(
            text = personaTitle,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}