package bruhcollective.itaysonlab.cobalt.guard.bottom_sheet

import bruhcollective.itaysonlab.ksteam.ExtendedSteamClient
import bruhcollective.itaysonlab.ksteam.models.SteamId
import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class DefaultGuardRecoveryCodeSheetComponent (
    steamId: SteamId,
    componentContext: ComponentContext,
    private val onDismiss: () -> Unit
): GuardRecoveryCodeSheetComponent, ComponentContext by componentContext, KoinComponent {
    private val guard = get<ExtendedSteamClient>().guard.instanceFor(steamId)

    override val username = guard?.username.orEmpty()
    override val recoveryCode = guard?.revocationCode.orEmpty()

    override fun dismiss() = onDismiss()
}