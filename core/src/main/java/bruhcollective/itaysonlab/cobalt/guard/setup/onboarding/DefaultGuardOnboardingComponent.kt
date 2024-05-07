package bruhcollective.itaysonlab.cobalt.guard.setup.onboarding

import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.guard.models.SgCreationResult
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class DefaultGuardOnboardingComponent (
    componentContext: ComponentContext,
    private val onSmsSent: (SgCreationResult.SmsSent) -> Unit,
    private val onDuplicateRequest: () -> Unit
): GuardOnboardingComponent, ComponentContext by componentContext, CoroutineScope by componentContext.coroutineScope(), KoinComponent {
    private val steam by inject<SteamClient>()

    override val isTryingToStartSetup = MutableValue(false)

    override fun onSetupClicked() {
        launch {
            isTryingToStartSetup.value = true

            // TODO: get rid of sgAddFlow in kSteam
            when (val result = steam.ksteam.guard.initializeSgCreation()) {
                SgCreationResult.AlreadyHasGuard -> {
                    onDuplicateRequest()
                }

                is SgCreationResult.SmsSent -> {
                    onSmsSent(result)
                }

                is SgCreationResult.Error -> {
                    // TODO
                }
            }

            isTryingToStartSetup.value = false
        }
    }
}