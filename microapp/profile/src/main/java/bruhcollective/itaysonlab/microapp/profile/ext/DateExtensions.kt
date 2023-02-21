package bruhcollective.itaysonlab.microapp.profile.ext

import androidx.annotation.PluralsRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.pluralStringResource
import bruhcollective.itaysonlab.microapp.profile.R
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil

@ExperimentalComposeUiApi
@Composable
fun Long.toLastSeenDate(): String {
    val diff = Instant.fromEpochSeconds(this).periodUntil(
        other = Clock.System.now(),
        timeZone = TimeZone.currentSystemDefault()
    )

    return when {
        diff.years > 0 -> pluralStringResourceVarg(
            id = R.plurals.years,
            count = diff.years
        )

        diff.months > 0 -> pluralStringResourceVarg(
            id = R.plurals.months,
            count = diff.months
        )

        diff.days > 0 -> pluralStringResourceVarg(
            id = R.plurals.days,
            count = diff.days
        )

        diff.hours > 0 -> pluralStringResourceVarg(
            id = R.plurals.hours,
            count = diff.hours
        ) + " " + pluralStringResourceVarg(
            id = R.plurals.minutes,
            count = diff.minutes
        )

        diff.minutes > 0 -> pluralStringResourceVarg(
            id = R.plurals.minutes,
            count = diff.minutes
        )

        else -> pluralStringResourceVarg(
            id = R.plurals.minutes,
            1
        )
    }
}

@ExperimentalComposeUiApi
@Composable
private fun pluralStringResourceVarg(@PluralsRes id: Int, count: Int) =
    pluralStringResource(id, count, count)