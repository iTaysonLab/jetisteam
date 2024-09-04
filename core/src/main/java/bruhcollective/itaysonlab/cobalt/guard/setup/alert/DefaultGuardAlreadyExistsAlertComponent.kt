package bruhcollective.itaysonlab.cobalt.guard.setup.alert

import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.guard.models.SgCreationResult
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class DefaultGuardAlreadyExistsAlertComponent (
    componentContext: ComponentContext,
    private val onConfirm: (SgCreationResult.AwaitingConfirmation) -> Unit,
    private val onCancel: () -> Unit
): GuardAlreadyExistsAlertComponent, CoroutineScope by componentContext.coroutineScope(), ComponentContext by componentContext, KoinComponent {
    private val steamClient by inject<SteamClient>()

    override val isConfirmingReplacement = MutableValue(false)

    override fun dismiss() = onCancel.invoke()

    override fun confirm() {
        launch {
            isConfirmingReplacement.value = false

            when (val result = steamClient.ksteam.guard.initializeSgMoving()) {
                is SgCreationResult.AwaitingConfirmation -> {
                    onConfirm(result)
                }

                is SgCreationResult.Error -> {
                    // TODO
                }

                SgCreationResult.AlreadyHasGuard -> Unit // ignored
            }

            isConfirmingReplacement.value = true
        }
    }
}