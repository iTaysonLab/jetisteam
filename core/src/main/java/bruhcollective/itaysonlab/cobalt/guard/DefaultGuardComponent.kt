package bruhcollective.itaysonlab.cobalt.guard

import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.DefaultGuardIncomingSessionComponent
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.DefaultGuardRecoveryCodeSheetComponent
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.DefaultGuardRemoveSheetComponent
import bruhcollective.itaysonlab.cobalt.guard.confirmation.DefaultGuardConfirmationComponent
import bruhcollective.itaysonlab.cobalt.guard.instance.DefaultGuardInstanceComponent
import bruhcollective.itaysonlab.cobalt.guard.qr.DefaultGuardQrScannerComponent
import bruhcollective.itaysonlab.cobalt.guard.session.DefaultGuardSessionDetailComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.alert.DefaultGuardAlreadyExistsAlertComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.onboarding.DefaultGuardOnboardingComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.recovery.SetupGuardRecoveryCodeComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.sms.DefaultGuardEnterSmsComponent
import bruhcollective.itaysonlab.ksteam.guard.models.ActiveSession
import bruhcollective.itaysonlab.ksteam.guard.models.GuardStructure
import bruhcollective.itaysonlab.ksteam.guard.models.MobileConfirmationItem
import bruhcollective.itaysonlab.ksteam.models.toSteamId
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DefaultGuardComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory
) : GuardComponent, ComponentContext by componentContext, KoinComponent, BackHandlerOwner {
    private val steamClient: SteamClient by inject()

    private val alertNavigation = SlotNavigation<AlertConfig>()
    private val childNavigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, GuardComponent.Child>> = childStack(
        source = childNavigation,
        childFactory = ::createChild,
        initialConfiguration = createInitialConfiguration(),
        serializer = Config.serializer(),
        handleBackButton = true
    )

    override val alertSlot: Value<ChildSlot<*, GuardComponent.AlertChild>> = childSlot(
        source = alertNavigation,
        childFactory = ::createAlert,
        serializer = AlertConfig.serializer()
    )

    private fun createChild(
        config: Config,
        componentContext: ComponentContext
    ): GuardComponent.Child {
        return when (config) {
            Config.SetupOnboarding -> {
                GuardComponent.Child.SetupOnboarding(
                    component = DefaultGuardOnboardingComponent(
                        componentContext = componentContext,
                        onSmsSent = { msg ->
                            childNavigation.push(
                                Config.SetupEnterSmsCode(
                                    steamId = steamClient.currentSteamId.id,
                                    hint = msg.hint,
                                    isMoving = msg.moving,
                                    structure = msg.guardConfiguration
                                )
                            )
                        }, onDuplicateRequest = {
                            alertNavigation.activate(AlertConfig.GuardAlreadyExists(steamId = steamClient.currentSteamId.id))
                        }
                    )
                )
            }

            is Config.SetupEnterSmsCode -> {
                GuardComponent.Child.SetupEnterSmsCode(
                    component = DefaultGuardEnterSmsComponent(
                        onExitClicked = ::onBackPressed, onGuardAdded = { revocationCode ->
                            childNavigation.replaceAll(
                                Config.Instance(steamId = config.steamId),
                                Config.SetupRecoveryCode(steamId = config.steamId, code = revocationCode)
                            )
                        },
                        isMovingGuard = config.isMoving,
                        phoneNumberHint = config.hint,
                        guardStructure = config.structure,
                        componentContext = componentContext
                    )
                )
            }

            is Config.SetupRecoveryCode -> {
                GuardComponent.Child.SetupRecoveryCode(
                    component = SetupGuardRecoveryCodeComponent(
                        steamId = config.steamId.toSteamId(),
                        code = config.code,
                        onExitClicked = childNavigation::pop,
                        componentContext = componentContext
                    )
                )
            }

            is Config.Instance -> {
                GuardComponent.Child.Instance(
                    component = DefaultGuardInstanceComponent(
                        componentContext = componentContext,
                        storeFactory = storeFactory,
                        steamId = config.steamId.toSteamId(),
                        onQrScannerClicked = {
                            alertNavigation.activate(AlertConfig.QrScanner(steamId = config.steamId))
                        }, onDeleteClicked = {
                            alertNavigation.activate(AlertConfig.RemoveGuard(steamId = config.steamId))
                        }, onConfirmationClicked = { confirmation ->
                            childNavigation.push(Config.ConfirmationDetail(steamId = config.steamId, confirmation = confirmation))
                        }, onRecoveryCodeClicked = { code ->
                            alertNavigation.activate(AlertConfig.RecoveryCode(steamId = config.steamId, code = code))
                        }, onSessionClicked = { session ->
                            childNavigation.push(Config.SessionDetail(steamId = config.steamId, session = session))
                        }, onIncomingSessionAppeared = { id ->
                            alertNavigation.activate(AlertConfig.IncomingSession(steamId = config.steamId, sessionId = id))
                        }
                    )
                )
            }

            is Config.ConfirmationDetail -> {
                GuardComponent.Child.MobileConfirmationDetail(
                    component = DefaultGuardConfirmationComponent(
                        componentContext = componentContext,
                        item = config.confirmation,
                        steamId = config.steamId.toSteamId(),
                        onDismiss = ::onBackPressed,
                        onActionTaken = {
                            childNavigation.pop {
                                (stack.active.instance as? GuardComponent.Child.Instance)?.component?.notifyConfirmationDecided(config.confirmation.id)
                            }
                        }
                    )
                )
            }

            is Config.SessionDetail -> {
                GuardComponent.Child.ActiveSessionDetail(
                    component = DefaultGuardSessionDetailComponent(
                        session = config.session,
                        onDismiss = ::onBackPressed,
                        componentContext = componentContext,
                        onSessionRemoved = {
                            childNavigation.pop {
                                (stack.active.instance as? GuardComponent.Child.Instance)?.component?.notifySessionRevoked(config.session.id)
                            }
                        },
                    )
                )
            }
        }
    }

    private fun createAlert(
        config: AlertConfig,
        componentContext: ComponentContext
    ): GuardComponent.AlertChild {
        return when (config) {
            is AlertConfig.GuardAlreadyExists -> {
                GuardComponent.AlertChild.SetupOverrideExisting(
                    DefaultGuardAlreadyExistsAlertComponent(componentContext, onConfirm = { msg ->
                        alertNavigation.dismiss()
                        childNavigation.pushNew(
                            Config.SetupEnterSmsCode(
                                steamId = config.steamId,
                                hint = msg.hint,
                                isMoving = msg.moving,
                                structure = null
                            )
                        )
                    }, onCancel = alertNavigation::dismiss)
                )
            }

            is AlertConfig.RecoveryCode -> {
                GuardComponent.AlertChild.InstanceRecoveryCode(
                    component = DefaultGuardRecoveryCodeSheetComponent(
                        componentContext = componentContext,
                        steamId = config.steamId.toSteamId(),
                        onDismiss = alertNavigation::dismiss,
                    )
                )
            }

            is AlertConfig.RemoveGuard -> {
                GuardComponent.AlertChild.InstanceDelete(
                    component = DefaultGuardRemoveSheetComponent(
                        componentContext = componentContext,
                        steamId = config.steamId.toSteamId(),
                        onDismiss = alertNavigation::dismiss,
                        onGuardRemovedSuccessfully = {
                            alertNavigation.dismiss()
                            childNavigation.replaceAll(Config.SetupOnboarding)
                        }
                    )
                )
            }

            is AlertConfig.IncomingSession -> {
                GuardComponent.AlertChild.IncomingSession(
                    component = DefaultGuardIncomingSessionComponent(
                        componentContext = componentContext,
                        steamId = config.steamId.toSteamId(),
                        sessionId = config.sessionId,
                        onDismiss = alertNavigation::dismiss,
                    )
                )
            }

            is AlertConfig.QrScanner -> {
                GuardComponent.AlertChild.QrCodeScanner(
                    component = DefaultGuardQrScannerComponent(
                        componentContext = componentContext,
                        steamId = config.steamId.toSteamId(),
                        onDismiss = alertNavigation::dismiss,
                    )
                )
            }
        }
    }

    private fun createInitialConfiguration(): Config {
        return steamClient.ksteam.guard.instanceForCurrentUser()?.let { instance ->
            Config.Instance(instance.steamId.id)
        } ?: Config.SetupOnboarding
    }

    override fun onBackPressed() {
        childNavigation.pop()
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object SetupOnboarding : Config

        @Serializable
        data class SetupEnterSmsCode(
            val steamId: ULong,
            val hint: String,
            val isMoving: Boolean,
            val structure: GuardStructure?
        ) : Config

        @Serializable
        data class SetupRecoveryCode(
            val steamId: ULong,
            val code: String,
        ) : Config

        @Serializable
        data class Instance(
            val steamId: ULong
        ) : Config

        @Serializable
        data class SessionDetail(
            val steamId: ULong,
            val session: ActiveSession
        ) : Config

        @Serializable
        data class ConfirmationDetail(
            val steamId: ULong,
            val confirmation: MobileConfirmationItem
        ) : Config
    }

    @Serializable
    private sealed interface AlertConfig {
        @Serializable
        data class GuardAlreadyExists(
            val steamId: ULong
        ) : AlertConfig

        @Serializable
        data class RecoveryCode(
            val steamId: ULong,
            val code: String
        ) : AlertConfig

        @Serializable
        data class RemoveGuard(
            val steamId: ULong
        ) : AlertConfig

        @Serializable
        data class QrScanner(
            val steamId: ULong
        ) : AlertConfig

        @Serializable
        data class IncomingSession(
            val steamId: ULong,
            val sessionId: Long
        ) : AlertConfig
    }
}