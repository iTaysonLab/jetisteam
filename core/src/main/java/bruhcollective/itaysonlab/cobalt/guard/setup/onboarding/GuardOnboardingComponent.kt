package bruhcollective.itaysonlab.cobalt.guard.setup.onboarding

import com.arkivanov.decompose.value.Value

interface GuardOnboardingComponent {
    val isTryingToStartSetup: Value<Boolean>

    fun onSetupClicked()
}