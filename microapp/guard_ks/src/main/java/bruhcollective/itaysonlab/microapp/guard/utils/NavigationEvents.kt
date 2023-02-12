package bruhcollective.itaysonlab.microapp.guard.utils

import androidx.compose.runtime.Stable
import bruhcollective.itaysonlab.microapp.core.navigation.extensions.results.NavigationResult
import kotlinx.parcelize.Parcelize

@Stable
@Parcelize
class ConfirmationDetailResult(
    val id: String
): NavigationResult

@Stable
@Parcelize
class ConfirmedNewSession(
    val id: Long,
    val allowed: Boolean
): NavigationResult

@Stable
@Parcelize
class RevokedActiveSession(
    val id: Long
): NavigationResult