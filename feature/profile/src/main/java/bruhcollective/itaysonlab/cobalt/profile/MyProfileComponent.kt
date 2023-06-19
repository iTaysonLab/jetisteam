package bruhcollective.itaysonlab.cobalt.profile

import bruhcollective.itaysonlab.cobalt.core.decompose.componentCoroutineScope
import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.cobalt.profile.components.header.DefaultProfileHeaderComponent
import bruhcollective.itaysonlab.cobalt.profile.components.header.ProfileHeaderComponent
import bruhcollective.itaysonlab.cobalt.profile.components.status.DefaultStatusCardComponent
import bruhcollective.itaysonlab.cobalt.profile.components.status.StatusCardComponent
import bruhcollective.itaysonlab.ksteam.handlers.persona
import bruhcollective.itaysonlab.ksteam.handlers.profile
import bruhcollective.itaysonlab.ksteam.models.persona.Persona
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileWidget
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MyProfileComponent (
    componentContext: ComponentContext
): ProfileComponent, ComponentContext by componentContext, KoinComponent {
    override val state = MutableValue<ProfileComponent.State>(ProfileComponent.State.Loading)

    private val scope = componentCoroutineScope()
    private val steam by inject<SteamClient>()

    override val headerComponent: ProfileHeaderComponent = DefaultProfileHeaderComponent(componentContext = childContext(key = "MPC:Header"))
    override val statusCardComponent: StatusCardComponent = DefaultStatusCardComponent(componentContext = childContext(key = "MPC:StatusCard"))
    override val widgets = MutableValue<ImmutableList<ProfileWidget>>(persistentListOf())

    override fun dispatchComponentLoad() {
        if (state.value != ProfileComponent.State.Loading) return
        launchPersonaObserver()
        launchPersonaEquipmentObserver()
        requestPersonaCustomization()
    }

    private fun launchPersonaObserver() {
        val personaFlow = steam.ksteam.persona.currentLivePersona().stateIn(scope, SharingStarted.Lazily, Persona.Unknown)

        personaFlow.onEach { persona ->
            if (persona != Persona.Unknown) {
                state.value = ProfileComponent.State.Ready
            }

            headerComponent.onPersonaUpdated(persona)
            statusCardComponent.onPersonaUpdated(persona)
        }.launchIn(scope)
    }

    private fun launchPersonaEquipmentObserver() {
        val personaEquipFlow = steam.ksteam.profile.getMyEquipment()

        personaEquipFlow.onEach { equipment ->
            headerComponent.onPersonaEquipmentUpdated(equipment)
        }.launchIn(scope)
    }

    private fun requestPersonaCustomization() {
        println("[requestPersonaCustomization]")

        scope.launch {
            println("[requestPersonaCustomization] scope")

            val customization = withContext(Dispatchers.Default) {
                steam.ksteam.profile.getCustomization(
                    steamId = steam.currentSteamId,
                    includeInactive = false
                )
            }

            println("[requestPersonaCustomization] success")

            widgets.value = customization.profileWidgets.toPersistentList()
        }
    }
}