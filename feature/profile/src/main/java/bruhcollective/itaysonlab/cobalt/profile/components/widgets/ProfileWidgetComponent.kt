package bruhcollective.itaysonlab.cobalt.profile.components.widgets

import androidx.annotation.StringRes
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileWidget

interface ProfileWidgetComponent {
    @get:StringRes val typeStringRes: Int
    val widget: ProfileWidget
}