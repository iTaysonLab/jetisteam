package bruhcollective.itaysonlab.cobalt.navigation

import bruhcollective.itaysonlab.cobalt.guard.GuardComponent
import bruhcollective.itaysonlab.cobalt.guard.confirmation.GuardConfirmationComponent
import bruhcollective.itaysonlab.cobalt.guard.session.GuardSessionDetailComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.recovery.GuardRecoveryCodeComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.sms.GuardEnterSmsComponent
import bruhcollective.itaysonlab.cobalt.library.LibraryComponent
import bruhcollective.itaysonlab.cobalt.news.discover.DiscoverComponent
import bruhcollective.itaysonlab.cobalt.profile.ProfileComponent

/**
 * A destination child, used in [DestinationComponent].
 *
 * Children should follow these rules:
 * - they CAN define their own alerts, slots or pagers
 * - they MUST NOT define their own stacks
 */
sealed interface DestinationChild {
    // region LIBRARY

    /**
     * The library root.
     *
     * Hosts a pager of screenshots, devices and games.
     */
    class LibraryRoot (
        val component: LibraryComponent
    ): DestinationChild

    // endregion

    // region GUARD

    /**
     * The guard root.
     *
     * Hosts a slot that can be setup onboarding or pager of code, sessions and confirmations.
     */
    class GuardRoot (
        val component: GuardComponent
    ): DestinationChild

    /**
     * The guard's session detail page.
     */
    class GuardSessionDetail (
        val component: GuardSessionDetailComponent
    ): DestinationChild

    /**
     * The guard's confirmation detail page.
     */
    class GuardConfirmationDetail (
        val component: GuardConfirmationComponent
    ): DestinationChild

    /**
     * The guard's setup page, where the user must enter the verification code.
     */
    class GuardSetupEnterCode (
        val component: GuardEnterSmsComponent
    ): DestinationChild

    /**
     * The guard's setup final page, where the user sees the recovery code.
     */
    class GuardSetupRecoveryCode (
        val component: GuardRecoveryCodeComponent
    ): DestinationChild

    // endregion

    // region PROFILE

    /**
     * The profile page.
     */
    class Profile (
        val component: ProfileComponent
    ): DestinationChild

    // endregion

    // NEWS

    /**
     * The newsfeed page.
     */
    class Newsfeed (
        val component: DiscoverComponent
    ): DestinationChild

    // endregion
}