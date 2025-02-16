package bruhcollective.itaysonlab.cobalt.profile.components.status

import bruhcollective.itaysonlab.cobalt.core.ksteam.NullableSteamApplication
import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.models.persona.Persona
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class DefaultStatusCardComponent (
    componentContext: ComponentContext
): StatusCardComponent, ComponentContext by componentContext, KoinComponent {
    private val scope = coroutineScope()
    private val steam by inject<SteamClient>()

    override val status = MutableValue<Persona.Status>(Persona.Status.Offline)
    override val appInformation = MutableValue(NullableSteamApplication(null))

    override fun onPersonaUpdated(persona: Persona) {
        status.value = persona.status

        if (persona.status is Persona.Status.InGame) {
            scope.launch {
                // appInformation.value = steam.ksteam.store.getNetworkApp((persona.status as? Persona.Status.InGame)?.appId?.value ?: return@launch)
            }
        } else {
            // appInformation.value = AppSummary(0, "", "")
        }
    }
}