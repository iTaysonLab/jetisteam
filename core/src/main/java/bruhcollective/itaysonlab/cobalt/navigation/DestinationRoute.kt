package bruhcollective.itaysonlab.cobalt.navigation

import bruhcollective.itaysonlab.ksteam.guard.models.ActiveSession
import bruhcollective.itaysonlab.ksteam.guard.models.GuardStructure
import bruhcollective.itaysonlab.ksteam.guard.models.MobileConfirmationItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Actual configuration from which [DestinationChild] is created.
 *
 * Think of it as actual navigation routes.
 */
@Serializable
sealed interface DestinationRoute {
    // region LIBRARY

    @Serializable
    @SerialName("library")
    data object Library : DestinationRoute

    // endregion

    // region GUARD

    @Serializable
    @SerialName("guard")
    data object Guard : DestinationRoute

    @Serializable
    @SerialName("guard_session")
    data class GuardSessionDetail (
        val steamId: ULong,
        val session: ActiveSession
    ) : DestinationRoute

    @Serializable
    @SerialName("guard_confirmation")
    data class GuardConfirmationDetail (
        val steamId: ULong,
        val confirmation: MobileConfirmationItem
    ) : DestinationRoute

    @Serializable
    @SerialName("guard_setup_enter_code")
    data class GuardSetupEnterCode (
        val steamId: ULong,
        val hint: String,
        val isMoving: Boolean,
        val structure: GuardStructure?
    ) : DestinationRoute

    @Serializable
    @SerialName("guard_setup_recovery_code")
    data class GuardSetupRecoveryCode (
        val steamId: ULong,
        val code: String,
    ) : DestinationRoute

    // endregion

    // region PROFILE

    @Serializable
    @SerialName("my_profile")
    data object MyProfile : DestinationRoute

    // endregion

    // region NEWS

    @Serializable
    @SerialName("newsfeed")
    data object Newsfeed : DestinationRoute

    // endregion
}