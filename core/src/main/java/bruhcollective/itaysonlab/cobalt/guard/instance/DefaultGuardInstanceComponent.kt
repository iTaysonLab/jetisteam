@file:UseSerializers(serializerClasses = [ActiveSessionSerializer::class])

package bruhcollective.itaysonlab.cobalt.guard.instance

import bruhcollective.itaysonlab.cobalt.core.CobaltDispatchers
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.DefaultGuardRecoveryCodeSheetComponent
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.DefaultGuardRemoveSheetComponent
import bruhcollective.itaysonlab.cobalt.guard.qr.DefaultGuardQrScannerComponent
import bruhcollective.itaysonlab.cobalt.guard.session.DefaultGuardSessionDetailComponent
import bruhcollective.itaysonlab.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.guard.GuardInstance
import bruhcollective.itaysonlab.ksteam.guard.models.ActiveSession
import bruhcollective.itaysonlab.ksteam.guard.models.MobileConfirmationItem
import bruhcollective.itaysonlab.ksteam.handlers.guard
import bruhcollective.itaysonlab.ksteam.models.SteamId
import bruhcollective.itaysonlab.ksteam.models.toSteamId
import bruhcollective.itaysonlab.ksteam.serialization.ActiveSessionSerializer
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.withLifecycle
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class DefaultGuardInstanceComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val steamId: SteamId,
    private val onGuardRemovedSuccessfully: () -> Unit,
): GuardInstanceComponent, ComponentContext by componentContext, KoinComponent {
    private val fsAlertNavigation = SlotNavigation<FsAlertSlotConfig>()
    private val alertNavigation = SlotNavigation<AlertSlotConfig>()

    private val store = instanceKeeper.getStore {
        val instance = get<SteamClient>().guard.instanceFor(steamId) ?: error("GuardInstanceStore cannot be constructed for a non-SG holding user")
        val codeFlow = instance.code.withLifecycle(lifecycle)

        GuardInstanceStore(
            storeFactory = storeFactory,
            mainContext = get<CobaltDispatchers>().main,
            steamClient = get<SteamClient>(),
            codeFlow = codeFlow,
            initialState = GuardInstanceState(steamId = steamId, username = instance.username)
        )
    }

    init {
        lifecycle.doOnResume {
            store.accept(GuardInstanceIntent.LoadMobileConfirmations)
            store.accept(GuardInstanceIntent.LoadActiveSessions)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<GuardInstanceState> = store.stateFlow

    override val fullscreenAlertSlot: Value<ChildSlot<*, GuardInstanceComponent.FullscreenAlertChild>> = childSlot(
        source = fsAlertNavigation,
        handleBackButton = true,
        childFactory = ::createFsAlertSlot,
        serializer = FsAlertSlotConfig.serializer(),
        key = "GuardInstance-FullscreenAlert"
    )

    override val alertSlot: Value<ChildSlot<*, GuardInstanceComponent.AlertChild>> = childSlot(
        source = alertNavigation,
        handleBackButton = true,
        childFactory = ::createAlertSlot,
        serializer = AlertSlotConfig.serializer(),
        key = "GuardInstance-Alert"
    )

    override fun openQrScanner() {
        fsAlertNavigation.activate(FsAlertSlotConfig.QrScanner)
    }

    override fun openSessionDetail(session: ActiveSession) {
        fsAlertNavigation.activate(FsAlertSlotConfig.SessionDetail(session))
    }

    override fun openRecoveryCodeSheet() {
        alertNavigation.activate(AlertSlotConfig.RecoveryCode(accountName = state.value.username, code = state.value.revocationCode))
    }

    override fun openDeleteSheet() {
        alertNavigation.activate(AlertSlotConfig.RemoveGuard(steamId = steamId.id))
    }

    private fun createFsAlertSlot(config: FsAlertSlotConfig, componentContext: ComponentContext): GuardInstanceComponent.FullscreenAlertChild {
        return when (config) {
            FsAlertSlotConfig.QrScanner -> {
                GuardInstanceComponent.FullscreenAlertChild.QrCodeScanner(
                    component = DefaultGuardQrScannerComponent(componentContext)
                )
            }

            is FsAlertSlotConfig.SessionDetail -> {
                GuardInstanceComponent.FullscreenAlertChild.SessionDetail(
                    component = DefaultGuardSessionDetailComponent(config.session, onDismiss = {
                        fsAlertNavigation.dismiss()
                    }, componentContext)
                )
            }

            is FsAlertSlotConfig.MobileConfirmationDetail -> TODO()
        }
    }

    private fun createAlertSlot(config: AlertSlotConfig, componentContext: ComponentContext): GuardInstanceComponent.AlertChild {
        return when (config) {
            is AlertSlotConfig.RecoveryCode -> {
                GuardInstanceComponent.AlertChild.RecoveryCode(
                    component = DefaultGuardRecoveryCodeSheetComponent(
                        recoveryCode = config.code,
                        username = config.accountName,
                        componentContext = componentContext,
                        onDismiss = alertNavigation::dismiss
                    )
                )
            }

            is AlertSlotConfig.RemoveGuard -> {
                GuardInstanceComponent.AlertChild.DeleteGuard(
                    component = DefaultGuardRemoveSheetComponent(
                        steamId = config.steamId.toSteamId(),
                        componentContext = componentContext,
                        onDismiss = alertNavigation::dismiss,
                        onGuardRemovedSuccessfully = onGuardRemovedSuccessfully
                    )
                )
            }
        }
    }

    @Serializable
    private sealed interface FsAlertSlotConfig {
        @Serializable
        data class MobileConfirmationDetail (
            val item: MobileConfirmationItem
        ): FsAlertSlotConfig

        @Serializable
        data class SessionDetail (
            val session: ActiveSession
        ): FsAlertSlotConfig

        @Serializable
        data object QrScanner : FsAlertSlotConfig
    }

    @Serializable
    private sealed interface AlertSlotConfig {
        @Serializable
        data class RecoveryCode (
            val code: String,
            val accountName: String
        ): AlertSlotConfig

        @Serializable
        data class RemoveGuard (
            val steamId: ULong
        ) : AlertSlotConfig
    }
}