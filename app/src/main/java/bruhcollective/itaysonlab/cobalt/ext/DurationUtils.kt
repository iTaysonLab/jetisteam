package bruhcollective.itaysonlab.cobalt.ext

import android.content.Context
import bruhcollective.itaysonlab.cobalt.R
import kotlin.time.Duration

object DurationUtils {
    fun formatDuration(context: Context, duration: Duration): String = buildString {
        val ds = context.getString(R.string.dur_day)
        val hs = context.getString(R.string.dur_hour)
        val ms = context.getString(R.string.dur_minute)
        val ss = context.getString(R.string.dur_second)

        duration.toComponents { d, h, m, s, _ ->
            if (d > 0) append(d).append(ds).append(' ')
            if (h > 0) append(h).append(hs).append(' ')
            if (m > 0) append(m).append(ms).append(' ')
            if (s > 0) append(s).append(ss).append(' ')
        }
    }.trimEnd()
}