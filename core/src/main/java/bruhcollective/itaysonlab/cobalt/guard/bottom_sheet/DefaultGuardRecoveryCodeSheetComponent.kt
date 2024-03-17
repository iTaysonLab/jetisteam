package bruhcollective.itaysonlab.cobalt.guard.bottom_sheet

import com.arkivanov.decompose.ComponentContext

internal class DefaultGuardRecoveryCodeSheetComponent (
    override val recoveryCode: String,
    override val username: String,
    private val onDismiss: () -> Unit,
    componentContext: ComponentContext
): GuardRecoveryCodeSheetComponent, ComponentContext by componentContext {
    override fun dismiss() = onDismiss()
}