package bruhcollective.itaysonlab.microapp.profile.ext

import androidx.annotation.PluralsRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.pluralStringResource
import bruhcollective.itaysonlab.microapp.profile.R
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@ExperimentalComposeUiApi
@Composable
fun Long.toLastSeenDate(): String {
    val date = LocalDateTime.ofInstant(Instant.ofEpochSecond(this), ZoneId.systemDefault())
    val currentDate = LocalDateTime.now()
    val timeDiff = Duration.between(date, currentDate)

    return with(timeDiff){
        when {
            (toDays() / 365) > 0 -> pluralStringResourceVarg(
                id = R.plurals.years,
                count = (toDays() / 365).toInt()
            )
            (toDays() / 30) > 0 -> pluralStringResourceVarg(
                id = R.plurals.months,
                count = (toDays() / 30).toInt()
            )
            toDays() > 0 -> pluralStringResourceVarg(
                id = R.plurals.days,
                count = toDays().toInt()
            )
            toHours() > 0 -> pluralStringResourceVarg(
                id = R.plurals.hours,
                count = toHours().toInt()
            ) + " " + pluralStringResourceVarg(
                id = R.plurals.minutes,
                count = (toMinutes() % 60).toInt()
            )
            toMinutes() > 0 -> pluralStringResourceVarg(
                id = R.plurals.minutes,
                count = toMinutes().toInt()
            )
            else -> pluralStringResourceVarg(
                id = R.plurals.minutes,
                1
            )
        }
    }
}

@ExperimentalComposeUiApi
@Composable
private fun pluralStringResourceVarg(@PluralsRes id: Int, count: Int) = pluralStringResource(id, count, count)