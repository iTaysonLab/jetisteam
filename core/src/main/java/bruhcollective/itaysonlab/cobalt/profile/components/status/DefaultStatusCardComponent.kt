package bruhcollective.itaysonlab.cobalt.profile.components.status

import bruhcollective.itaysonlab.cobalt.core.decompose.componentCoroutineScope
import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.models.apps.AppSummary
import bruhcollective.itaysonlab.ksteam.models.persona.Persona
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class DefaultStatusCardComponent (
    componentContext: ComponentContext
): StatusCardComponent, ComponentContext by componentContext, KoinComponent {
    private val scope = componentCoroutineScope()
    private val steam by inject<SteamClient>()

    override val status = MutableValue<Persona.Status>(Persona.Status.Offline)
    override val appInformation = MutableValue(AppSummary(0, "", ""))

    override fun onPersonaUpdated(persona: Persona) {
        status.value = persona.status

        if (persona.status is Persona.Status.InGame) {
            scope.launch {
                appInformation.value = steam.ksteam.store.getNetworkApp((persona.status as? Persona.Status.InGame)?.appId?.value ?: return@launch)
            }
        } else {
            appInformation.value = AppSummary(0, "", "")
        }
    }
}