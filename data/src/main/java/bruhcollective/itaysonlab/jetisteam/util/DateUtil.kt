package bruhcollective.itaysonlab.jetisteam.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object DateUtil {
    fun formatDateTimeToLocale(timestamp: Long): String {
        val dtf = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        val date = Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(TimeZone.UTC)
        return dtf.format(date.toJavaLocalDateTime())
    }
}