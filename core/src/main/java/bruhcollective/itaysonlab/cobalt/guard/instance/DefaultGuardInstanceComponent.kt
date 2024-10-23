package bruhcollective.itaysonlab.cobalt.guard.instance

import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.DefaultGuardIncomingSessionComponent
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.DefaultGuardRecoveryCodeSheetComponent
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.DefaultGuardRemoveSheetComponent
import bruhcollective.itaysonlab.cobalt.guard.instance.code.DefaultGuardCodeComponent
import bruhcollective.itaysonlab.cobalt.guard.instance.confirmations.DefaultGuardConfirmationsComponent
import bruhcollective.itaysonlab.cobalt.guard.instance.sessions.DefaultGuardSessionsComponent
import bruhcollective.itaysonlab.cobalt.guard.qr.DefaultGuardQrScannerComponent
import bruhcollective.itaysonlab.ksteam.ExtendedSteamClient
import bruhcollective.itaysonlab.ksteam.guard.models.ActiveSession
import bruhcollective.itaysonlab.ksteam.guard.models.MobileConfirmationItem
import bruhcollective.itaysonlab.ksteam.models.SteamId
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.router.pages.Pages
import com.arkivanov.decompose.router.pages.PagesNavigation
import com.arkivanov.decompose.router.pages.childPages
import com.arkivanov.decompose.router.pages.select
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.coroutines.withLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class DefaultGuardInstanceComponent(
    componentContext: ComponentContext,
    private val steamId: SteamId,
    private val onGuardDeletionDone: () -> Unit,
    private val onSessionClicked: (ActiveSession) -> Unit,
    private val onConfirmationClicked: (MobileConfirmationItem) -> Unit
): GuardInstanceComponent, ComponentContext by componentContext, KoinComponent, CoroutineScope by componentContext.coroutineScope() {
    private val pagesNavigation = PagesNavigation<PageConfig>()
    private val alertNavigation = SlotNavigation<AlertConfig>()

    override val pages: Value<ChildPages<*, GuardInstanceComponent.PageChild>> = childPages(
        source = pagesNavigation,
        serializer = PageConfig.serializer(),
        childFactory = ::createPageChild,
        initialPages = { Pages(items = listOf(PageConfig.Code, PageConfig.Confirmations, PageConfig.Sessions), selectedIndex = 0) }
    )

    override val alertSlot: Value<ChildSlot<*, GuardInstanceComponent.AlertChild>> = childSlot(
        source = alertNavigation,
        handleBackButton = true,
        serializer = AlertConfig.serializer(),
        childFactory = ::createAlertChild
    )

    override fun selectPage(index: Int) {
        pagesNavigation.select(index)
    }

    init {
        launch {
            get<ExtendedSteamClient>().guardManagement.createIncomingSessionWatcher()
                .withLifecycle(lifecycle, minActiveState = Lifecycle.State.RESUMED)
                .filterNotNull()
                .distinctUntilChanged()
                .collect { id ->
                    alertNavigation.activate(AlertConfig.IncomingSession(id))
                }
        }
    }

    override fun openQrScanner() {
        alertNavigation.activate(AlertConfig.QrScanner)
    }

    private fun createPageChild(
        config: PageConfig,
        componentContext: ComponentContext
    ): GuardInstanceComponent.PageChild {
        return when (config) {
            PageConfig.Code -> {
                GuardInstanceComponent.PageChild.Code(
                    component = DefaultGuardCodeComponent(
                        componentContext = componentContext,
                        steamId = steamId,
                        onRecoveryButtonClicked = {
                            alertNavigation.activate(AlertConfig.RecoveryCode)
                        }, onDeleteButtonClicked = {
                            alertNavigation.activate(AlertConfig.RemoveGuard)
                        }
                    )
                )
            }

            PageConfig.Confirmations -> {
                GuardInstanceComponent.PageChild.Confirmations(
                    component = DefaultGuardConfirmationsComponent(
                        componentContext = componentContext,
                        steamId = steamId,
                        onConfirmationClicked = onConfirmationClicked
                    )
                )
            }

            PageConfig.Sessions -> {
                GuardInstanceComponent.PageChild.Sessions(
                    component = DefaultGuardSessionsComponent(
                        componentContext = componentContext,
                        steamId = steamId,
                        onSessionClicked = onSessionClicked
                    )
                )
            }
        }
    }

    private fun createAlertChild(
        config: AlertConfig,
        componentContext: ComponentContext
    ): GuardInstanceComponent.AlertChild {
        return when (config) {
            is AlertConfig.RecoveryCode -> {
                GuardInstanceComponent.AlertChild.RecoveryCode(
                    component = DefaultGuardRecoveryCodeSheetComponent(
                        componentContext = componentContext,
                        steamId = steamId,
                        onDismiss = alertNavigation::dismiss,
                    )
                )
            }

            is AlertConfig.RemoveGuard -> {
                GuardInstanceComponent.AlertChild.DeleteGuard(
                    component = DefaultGuardRemoveSheetComponent(
                        componentContext = componentContext,
                        steamId = steamId,
                        onDismiss = alertNavigation::dismiss,
                        onGuardRemovedSuccessfully = {
                            alertNavigation.dismiss()
                            onGuardDeletionDone()
                        }
                    )
                )
            }

            is AlertConfig.IncomingSession -> {
                GuardInstanceComponent.AlertChild.IncomingSession(
                    component = DefaultGuardIncomingSessionComponent(
                        componentContext = componentContext,
                        steamId = steamId,
                        sessionId = config.sessionId,
                        onDismiss = alertNavigation::dismiss,
                        onConfirmation = {
                            alertNavigation.dismiss()
                            notifySessionsRefresh()
                        }
                    )
                )
            }

            is AlertConfig.QrScanner -> {
                GuardInstanceComponent.AlertChild.QrCodeScanner(
                    component = DefaultGuardQrScannerComponent(
                        componentContext = componentContext,
                        steamId = steamId,
                        onDismiss = alertNavigation::dismiss,
                    )
                )
            }
        }
    }

    override fun notifySessionsRefresh() {
        pages.value.items
            .map { it.instance }
            .filterIsInstance<GuardInstanceComponent.PageChild.Sessions>()
            .forEach { it.component.onRefresh() }
    }

    override fun notifyConfirmationsRefresh() {
        pages.value.items
            .map { it.instance }
            .filterIsInstance<GuardInstanceComponent.PageChild.Confirmations>()
            .forEach { it.component.onRefresh() }
    }

    @Serializable
    private sealed interface PageConfig {
        @Serializable
        data object Code: PageConfig

        @Serializable
        data object Confirmations: PageConfig

        @Serializable
        data object Sessions: PageConfig
    }

    @Serializable
    private sealed interface AlertConfig {
        @Serializable
        data object RecoveryCode : AlertConfig

        @Serializable
        data object RemoveGuard : AlertConfig

        @Serializable
        data class IncomingSession(
            val sessionId: Long
        ) : AlertConfig

        @Serializable
        data object QrScanner : AlertConfig
    }
}