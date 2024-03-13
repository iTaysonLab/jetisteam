package bruhcollective.itaysonlab.cobalt.profile

import bruhcollective.itaysonlab.cobalt.core.decompose.ViewModel
import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.cobalt.profile.components.header.DefaultProfileHeaderComponent
import bruhcollective.itaysonlab.cobalt.profile.components.header.ProfileHeaderComponent
import bruhcollective.itaysonlab.cobalt.profile.components.status.DefaultStatusCardComponent
import bruhcollective.itaysonlab.cobalt.profile.components.status.StatusCardComponent
import bruhcollective.itaysonlab.cobalt.profile.components.widgets.DefaultProfileWidgetsComponent
import bruhcollective.itaysonlab.cobalt.profile.components.widgets.ProfileWidgetsComponent
import bruhcollective.itaysonlab.ksteam.handlers.persona
import bruhcollective.itaysonlab.ksteam.handlers.profile
import bruhcollective.itaysonlab.ksteam.models.SteamId
import bruhcollective.itaysonlab.ksteam.models.persona.Persona
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileCustomization
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileEquipment
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.ObserveLifecycleMode
import com.arkivanov.decompose.value.subscribe
import com.arkivanov.essenty.instancekeeper.getOrCreate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MyProfileComponent (
    componentContext: ComponentContext
): ProfileComponent, ComponentContext by componentContext {
    private val viewModel: ProfileViewModel = instanceKeeper.getOrCreate {
        ProfileViewModel()
    }

    override val state get() = viewModel.state
    override val headerComponent: ProfileHeaderComponent = DefaultProfileHeaderComponent(componentContext = childContext(key = "MPC:Header"))
    override val statusCardComponent: StatusCardComponent = DefaultStatusCardComponent(componentContext = childContext(key = "MPC:StatusCard"))
    override val widgetsComponent: ProfileWidgetsComponent = DefaultProfileWidgetsComponent(componentContext = childContext(key = "MPC:Widgets"))

    init {
        viewModel.persona.subscribe(lifecycle, mode = ObserveLifecycleMode.RESUME_PAUSE) { persona ->
            headerComponent.onPersonaUpdated(persona)
            statusCardComponent.onPersonaUpdated(persona)
        }

        viewModel.personaEquipment.subscribe(lifecycle, mode = ObserveLifecycleMode.RESUME_PAUSE) { equipment ->
            headerComponent.onPersonaEquipmentUpdated(equipment)
        }

        viewModel.personaCustomization.subscribe(lifecycle, mode = ObserveLifecycleMode.RESUME_PAUSE) { customization ->
            widgetsComponent.onPersonaCustomizationChanged(customization)
        }
    }

    override fun dispatchComponentLoad() = viewModel.dispatchComponentLoad()

    private class ProfileViewModel: ViewModel(), KoinComponent {
        private val steam by inject<SteamClient>()
        private val stateMutex = Mutex()

        val state = MutableValue<ProfileComponent.State>(ProfileComponent.State.Idle)
        val persona = MutableValue(Persona.Unknown)
        val personaEquipment = MutableValue<ProfileEquipment>(ProfileEquipment.None)
        val personaCustomization = MutableValue<ProfileCustomization>(ProfileCustomization.None)

        fun dispatchComponentLoad() {
            viewModelScope.launch {
                stateMutex.withLock {
                    if (state.value != ProfileComponent.State.Idle) return@withLock
                    state.value = ProfileComponent.State.Loading
                    launchPersonaObserver()
                    launchPersonaEquipmentObserver()
                    launchPersonaCustomizationObserver()
                }
            }
        }

        private fun launchPersonaObserver() {
            steam.ksteam.persona.currentLivePersona().onEach { newPersona ->
                if (newPersona.id != SteamId.Empty) {
                    state.value = ProfileComponent.State.Ready
                }

                persona.value = newPersona
            }.launchIn(viewModelScope)
        }

        private fun launchPersonaEquipmentObserver() {
            steam.ksteam.profile.getMyEquipment().onEach { equipment ->
                personaEquipment.value = equipment
            }.launchIn(viewModelScope)
        }

        private fun launchPersonaCustomizationObserver() {
            steam.ksteam.persona.currentPersona.onEach { persona ->
                personaCustomization.value = withContext(Dispatchers.Default) {
                    steam.ksteam.profile.getCustomization(steamId = steam.currentSteamId)
                }
            }.launchIn(viewModelScope)
        }
    }
}