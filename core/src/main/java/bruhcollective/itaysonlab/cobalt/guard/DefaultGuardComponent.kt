package bruhcollective.itaysonlab.cobalt.guard

import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.DefaultGuardIncomingSessionComponent
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.DefaultGuardRecoveryCodeSheetComponent
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.DefaultGuardRemoveSheetComponent
import bruhcollective.itaysonlab.cobalt.guard.instance.DefaultGuardInstanceComponent
import bruhcollective.itaysonlab.cobalt.guard.qr.DefaultGuardQrScannerComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.alert.DefaultGuardAlreadyExistsAlertComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.onboarding.DefaultGuardOnboardingComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.recovery.SetupGuardRecoveryCodeComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.sms.DefaultGuardEnterSmsComponent
import bruhcollective.itaysonlab.ksteam.guard.models.ActiveSession
import bruhcollective.itaysonlab.ksteam.guard.models.GuardStructure
import bruhcollective.itaysonlab.ksteam.guard.models.MobileConfirmationItem
import bruhcollective.itaysonlab.ksteam.guard.models.SgCreationResult
import bruhcollective.itaysonlab.ksteam.models.SteamId
import bruhcollective.itaysonlab.ksteam.models.toSteamId
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DefaultGuardComponent(
    componentContext: ComponentContext,
    private val onOnboardingSmsSent: (SteamId, SgCreationResult.AwaitingConfirmation) -> Unit,
    private val onConfirmationClicked: (SteamId, MobileConfirmationItem) -> Unit,
    private val onSessionClicked: (SteamId, ActiveSession) -> Unit,
) : GuardComponent, ComponentContext by componentContext, KoinComponent, BackHandlerOwner {
    private val steamClient: SteamClient by inject()
    private val slotNavigation = SlotNavigation<SlotConfig>()

    override val slot: Value<ChildSlot<*, GuardComponent.Child>> = childSlot(
        source = slotNavigation,
        childFactory = ::createSlotChild,
        initialConfiguration = ::createInitialConfiguration,
        serializer = SlotConfig.serializer()
    )

    override fun notifySessionsUpdated() {
        (slot.value.child?.instance as? GuardComponent.Child.Instance)?.component?.notifySessionsRefresh()
    }

    override fun notifyConfirmationsUpdated() {
        (slot.value.child?.instance as? GuardComponent.Child.Instance)?.component?.notifyConfirmationsRefresh()
    }

    private fun createSlotChild(
        config: SlotConfig,
        componentContext: ComponentContext
    ): GuardComponent.Child {
        return when (config) {
            SlotConfig.Onboarding -> {
                GuardComponent.Child.Onboarding(
                    component = DefaultGuardOnboardingComponent(
                        componentContext = componentContext,
                        onSmsSent = onOnboardingSmsSent
                    )
                )
            }

            is SlotConfig.Instance -> {
                GuardComponent.Child.Instance(
                    component = DefaultGuardInstanceComponent(
                        componentContext = componentContext,
                        steamId = config.steamId.toSteamId(),
                        onGuardDeletionDone = {
                            slotNavigation.activate(SlotConfig.Onboarding)
                        },
                        onConfirmationClicked = { confirmation ->
                            onConfirmationClicked(config.steamId.toSteamId(), confirmation)
                        },
                        onSessionClicked = { session ->
                            onSessionClicked(config.steamId.toSteamId(), session)
                        }
                    )
                )
            }
        }
    }

    private fun createInitialConfiguration(): SlotConfig {
        return steamClient.ksteam.guard.instanceForCurrentUser()?.let { instance ->
            SlotConfig.Instance(instance.steamId.id)
        } ?: SlotConfig.Onboarding
    }

    @Serializable
    private sealed interface SlotConfig {
        @Serializable
        @SerialName("onboarding")
        data object Onboarding : SlotConfig

        @Serializable
        @SerialName("instance")
        data class Instance(
            val steamId: ULong
        ) : SlotConfig
    }
}