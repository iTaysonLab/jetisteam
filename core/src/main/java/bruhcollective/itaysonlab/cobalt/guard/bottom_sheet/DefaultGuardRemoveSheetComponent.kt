package bruhcollective.itaysonlab.cobalt.guard.bottom_sheet

import bruhcollective.itaysonlab.ksteam.ExtendedSteamClient
import bruhcollective.itaysonlab.ksteam.models.SteamId
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class DefaultGuardRemoveSheetComponent (
    private val steamId: SteamId,
    private val onDismiss: () -> Unit,
    private val onGuardRemovedSuccessfully: () -> Unit,
    componentContext: ComponentContext
): GuardRemoveSheetComponent, KoinComponent, ComponentContext by componentContext, CoroutineScope by componentContext.coroutineScope() {
    override val username = get<ExtendedSteamClient>().guard.instanceFor(steamId)?.username.orEmpty()
    override val isRemovalInProgress = MutableValue(false)
    override fun dismiss() = onDismiss()

    override fun confirmDeletion() {
        launch {
            isRemovalInProgress.value = true

            get<ExtendedSteamClient>().guard.delete(steamId = steamId, unsafe = false)
            onGuardRemovedSuccessfully()

            isRemovalInProgress.value = false
        }
    }
}