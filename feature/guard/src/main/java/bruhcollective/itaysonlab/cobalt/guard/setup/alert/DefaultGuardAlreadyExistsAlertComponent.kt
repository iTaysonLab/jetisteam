package bruhcollective.itaysonlab.cobalt.guard.setup.alert

import com.arkivanov.decompose.ComponentContext

internal class DefaultGuardAlreadyExistsAlertComponent (
    componentContext: ComponentContext,
    private val onConfirm: () -> Unit,
    private val onCancel: () -> Unit
): GuardAlreadyExistsAlertComponent, ComponentContext by componentContext {
    override fun onConfirm() = onConfirm.invoke()
    override fun onCancel() = onCancel.invoke()
}