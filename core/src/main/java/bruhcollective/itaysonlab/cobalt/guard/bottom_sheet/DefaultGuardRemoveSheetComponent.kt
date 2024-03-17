package bruhcollective.itaysonlab.cobalt.guard.bottom_sheet

import bruhcollective.itaysonlab.cobalt.core.decompose.componentCoroutineScope
import bruhcollective.itaysonlab.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.handlers.guard
import bruhcollective.itaysonlab.ksteam.models.SteamId
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class DefaultGuardRemoveSheetComponent (
    private val steamId: SteamId,
    private val onDismiss: () -> Unit,
    private val onGuardRemovedSuccessfully: () -> Unit,
    componentContext: ComponentContext
): GuardRemoveSheetComponent, KoinComponent, ComponentContext by componentContext {
    private val componentScope = componentCoroutineScope()

    override val username = get<SteamClient>().guard.instanceFor(steamId)?.username.orEmpty()
    override val isRemovalInProgress = MutableValue(false)
    override fun dismiss() = onDismiss()

    override fun confirmDeletion() {
        componentScope.launch {
            isRemovalInProgress.value = true

            get<SteamClient>().guard.delete(steamId = steamId, unsafe = false)
            onGuardRemovedSuccessfully()

            isRemovalInProgress.value = false
        }
    }
}