package bruhcollective.itaysonlab.cobalt.navigation

import bruhcollective.itaysonlab.cobalt.guard.DefaultGuardComponent
import bruhcollective.itaysonlab.cobalt.guard.confirmation.DefaultGuardConfirmationComponent
import bruhcollective.itaysonlab.cobalt.guard.session.DefaultGuardSessionDetailComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.recovery.SetupGuardRecoveryCodeComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.sms.DefaultGuardEnterSmsComponent
import bruhcollective.itaysonlab.cobalt.library.DefaultLibraryComponent
import bruhcollective.itaysonlab.cobalt.news.DefaultNewsfeedComponent
import bruhcollective.itaysonlab.cobalt.news.DefaultWrappedNewsfeedComponent
import bruhcollective.itaysonlab.cobalt.news.models.NewsfeedType
import bruhcollective.itaysonlab.cobalt.profile.MyProfileComponent
import bruhcollective.itaysonlab.ksteam.models.toSteamId
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.items
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popToFirst
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value

internal class DefaultDestinationComponent (
    initialDestinationRoute: DestinationRoute,
    componentContext: ComponentContext
): DestinationComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<DestinationRoute>()
    
    override val stack: Value<ChildStack<*, DestinationChild>> = childStack(
        source = navigation,
        initialStack = { listOf(initialDestinationRoute) },
        childFactory = ::createDestinationChild,
        serializer = DestinationRoute.serializer(),
        key = "DestinationChildStack",
        handleBackButton = true
    )

    override fun onBackPressed() {
        navigation.pop()
    }

    override fun onResetStackPressed(): Boolean {
        return if (stack.items.size > 1) {
            navigation.popToFirst()
            true
        } else {
            false
        }
    }

    private fun createDestinationChild(configuration: DestinationRoute, componentContext: ComponentContext): DestinationChild {
        return when (configuration) {
            // LIBRARY
            DestinationRoute.Library -> createLibraryChild(componentContext)
            
            // GUARD
            DestinationRoute.Guard -> createGuardChild(componentContext)
            is DestinationRoute.GuardConfirmationDetail -> createGuardConfirmationChild(configuration, componentContext)
            is DestinationRoute.GuardSessionDetail -> createGuardSessionChild(configuration, componentContext)
            is DestinationRoute.GuardSetupEnterCode -> createGuardEnterCodeChild(configuration, componentContext)
            is DestinationRoute.GuardSetupRecoveryCode -> createGuardRecoveryCodeChild(configuration, componentContext)
            
            // PROFILE
            DestinationRoute.MyProfile -> createMyProfileChild(componentContext)
            
            // NEWS
            DestinationRoute.WrappedNewsfeed -> createWrappedNewsfeedChild(componentContext)
            is DestinationRoute.Newsfeed -> createNewsfeedChild(configuration, componentContext)
        }
    }
    
    private fun createLibraryChild(componentContext: ComponentContext): DestinationChild.LibraryRoot {
        return DestinationChild.LibraryRoot(
            component = DefaultLibraryComponent(
                componentContext = componentContext
            )
        )
    }
    
    private fun createGuardChild(componentContext: ComponentContext): DestinationChild.GuardRoot {
        return DestinationChild.GuardRoot(
            component = DefaultGuardComponent(
                componentContext = componentContext,
                onOnboardingSmsSent = { steamId, msg ->
                    navigation.pushNew(DestinationRoute.GuardSetupEnterCode(
                        steamId = steamId.id,
                        hint = msg.hint,
                        isMoving = msg.moving,
                        structure = msg.guardConfiguration
                    ))
                }, onSessionClicked = { steamId, activeSession ->
                    navigation.pushNew(DestinationRoute.GuardSessionDetail(
                        steamId = steamId.id,
                        session = activeSession
                    ))
                }, onConfirmationClicked = { steamId, mobileConfirmationItem ->
                    navigation.pushNew(DestinationRoute.GuardConfirmationDetail(
                        steamId = steamId.id,
                        confirmation = mobileConfirmationItem
                    ))
                }
            )
        )
    }

    private fun createGuardSessionChild(
        configuration: DestinationRoute.GuardSessionDetail, 
        componentContext: ComponentContext
    ): DestinationChild.GuardSessionDetail {
        return DestinationChild.GuardSessionDetail(
            component = DefaultGuardSessionDetailComponent(
                componentContext = componentContext,
                session = configuration.session,
                onDismiss = navigation::pop,
                onSessionRemoved = {
                    navigation.pop()

                    stack.items
                        .map { it.instance }
                        .filterIsInstance<DestinationChild.GuardRoot>()
                        .forEach { it.component.notifySessionsUpdated() }
                }
            )
        )
    }

    private fun createGuardConfirmationChild(
        configuration: DestinationRoute.GuardConfirmationDetail,
        componentContext: ComponentContext
    ): DestinationChild.GuardConfirmationDetail {
        return DestinationChild.GuardConfirmationDetail(
            component = DefaultGuardConfirmationComponent(
                componentContext = componentContext,
                steamId = configuration.steamId.toSteamId(),
                item = configuration.confirmation,
                onDismiss = navigation::pop,
                onActionTaken = {
                    stack.items
                        .map { it.instance }
                        .filterIsInstance<DestinationChild.GuardRoot>()
                        .forEach { it.component.notifyConfirmationsUpdated() }
                }
            )
        )
    }

    private fun createGuardEnterCodeChild(
        configuration: DestinationRoute.GuardSetupEnterCode,
        componentContext: ComponentContext
    ): DestinationChild.GuardSetupEnterCode {
        return DestinationChild.GuardSetupEnterCode(
            component = DefaultGuardEnterSmsComponent(
                componentContext = componentContext,
                isMovingGuard = configuration.isMoving,
                phoneNumberHint = configuration.hint,
                guardStructure = configuration.structure,
                onExitClicked = navigation::pop,
                onGuardAdded = { code ->
                    navigation.replaceAll(
                        DestinationRoute.Guard,
                        DestinationRoute.GuardSetupRecoveryCode(configuration.steamId, code)
                    ) {
                        stack.items
                            .map { it.instance }
                            .filterIsInstance<DestinationChild.GuardRoot>()
                            .forEach { it.component.notifySlotUpdate() }
                    }
                }
            )
        )
    }

    private fun createGuardRecoveryCodeChild(
        configuration: DestinationRoute.GuardSetupRecoveryCode,
        componentContext: ComponentContext
    ): DestinationChild.GuardSetupRecoveryCode {
        return DestinationChild.GuardSetupRecoveryCode(
            component = SetupGuardRecoveryCodeComponent(
                componentContext = componentContext,
                steamId = configuration.steamId.toSteamId(),
                code = configuration.code,
                onExitClicked = navigation::pop
            )
        )
    }

    private fun createMyProfileChild(
        componentContext: ComponentContext
    ): DestinationChild.Profile {
        return DestinationChild.Profile(
            component = MyProfileComponent(
                componentContext = componentContext
            )
        )
    }

    private fun createWrappedNewsfeedChild(
        componentContext: ComponentContext
    ): DestinationChild.WrappedNewsfeed {
        return DestinationChild.WrappedNewsfeed(
            component = DefaultWrappedNewsfeedComponent(
                componentContext = componentContext,
                onUpcomingCardClicked = {
                    navigation.pushNew(DestinationRoute.Newsfeed(NewsfeedType.Upcoming))
                }
            )
        )
    }

    private fun createNewsfeedChild(
        configuration: DestinationRoute.Newsfeed,
        componentContext: ComponentContext,
    ): DestinationChild.Newsfeed {
        return DestinationChild.Newsfeed(
            component = DefaultNewsfeedComponent(
                type = configuration.type,
                componentContext = componentContext
            )
        )
    }
}