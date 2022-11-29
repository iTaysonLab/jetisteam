package bruhcollective.itaysonlab.jetisteam.util

import android.content.Context
import androidx.annotation.StringRes

sealed class FormattedResourceString {
    abstract fun get(ctx: Context): String

    class ResourceId (
        @StringRes private val base: Int,
        private vararg val args: Any
    ): FormattedResourceString() {
        override fun get(ctx: Context) = ctx.getString(base, *args)
    }

    class FixedString (
        private val data: String
    ): FormattedResourceString() {
        override fun get(ctx: Context) = data
    }
}