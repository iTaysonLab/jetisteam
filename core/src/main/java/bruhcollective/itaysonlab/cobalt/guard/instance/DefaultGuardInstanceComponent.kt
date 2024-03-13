package bruhcollective.itaysonlab.cobalt.guard.instance

import bruhcollective.itaysonlab.cobalt.core.CobaltDispatchers
import bruhcollective.itaysonlab.cobalt.guard.qr.DefaultGuardQrScannerComponent
import bruhcollective.itaysonlab.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.handlers.guard
import bruhcollective.itaysonlab.ksteam.models.SteamId
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.withLifecycle
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class DefaultGuardInstanceComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val steamId: SteamId
): GuardInstanceComponent, ComponentContext by componentContext, KoinComponent {
    private val qrSlotNavigation = SlotNavigation<QrSlotConfig>()

    private val store = instanceKeeper.getStore {
        TestGuardInstanceStore(
            storeFactory = storeFactory,
            steamId = steamId,
            mainContext = get<CobaltDispatchers>().main,
            steamClient = get<SteamClient>(),
            codeFlow = get<SteamClient>().guard.instanceFor(steamId)?.code?.withLifecycle(lifecycle) ?: error("GuardInstanceStore cannot be constructed for a non-SG holding user")
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

    override val qrSlot: Value<ChildSlot<*, GuardInstanceComponent.QrCodeScannerChild>> = childSlot(
        source = qrSlotNavigation,
        handleBackButton = true,
        childFactory = ::createQrSlot,
        serializer = QrSlotConfig.serializer()
    )

    override fun openQrScanner() {
        qrSlotNavigation.activate(QrSlotConfig.Present)
    }

    private fun createQrSlot(config: QrSlotConfig, componentContext: ComponentContext): GuardInstanceComponent.QrCodeScannerChild {
        return GuardInstanceComponent.QrCodeScannerChild(
            component = DefaultGuardQrScannerComponent(componentContext)
        )
    }

    @Serializable
    private sealed interface QrSlotConfig {
        @Serializable
        data object Present : QrSlotConfig
    }
}