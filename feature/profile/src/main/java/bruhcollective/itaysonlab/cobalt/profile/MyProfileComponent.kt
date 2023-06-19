package bruhcollective.itaysonlab.cobalt.profile

import bruhcollective.itaysonlab.cobalt.core.decompose.componentCoroutineScope
import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.handlers.persona
import bruhcollective.itaysonlab.ksteam.handlers.profile
import bruhcollective.itaysonlab.ksteam.models.persona.Persona
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileEquipment
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MyProfileComponent (
    componentContext: ComponentContext
): ProfileComponent, ComponentContext by componentContext, KoinComponent {
    private val scope = componentCoroutineScope()
    private val steam: SteamClient by inject()

    override val persona: StateFlow<Persona> = steam.ksteam.persona.currentLivePersona().stateIn(scope, SharingStarted.Lazily, Persona.Unknown)
    override val personaEquipment: StateFlow<ProfileEquipment> = steam.ksteam.profile.getMyEquipment()

    override fun dispatchComponentLoad() {

    }
}