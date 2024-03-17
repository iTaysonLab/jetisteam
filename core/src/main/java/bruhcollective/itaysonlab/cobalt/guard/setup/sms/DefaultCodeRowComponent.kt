package bruhcollective.itaysonlab.cobalt.guard.setup.sms

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

internal class DefaultCodeRowComponent(
    override val codeLength: Int,
    private val onEntryFinish: (String) -> Unit
): CodeRowComponent {
    private val entries = Array(codeLength) { MutableValue("") }

    override val inactive = MutableValue(false)
    override val error = MutableValue(false)

    override fun getEntryCode(index: Int): Value<String> {
        return entries[index]
    }

    override fun setEntryCode(index: Int, char: String) {
        entries[index].value = char
    }

    override fun onEntryFinish() {
        onEntryFinish(entries.joinToString(separator = "") { it.value })
    }

    override fun setInactive(value: Boolean) {
        inactive.value = value
    }

    override fun setError(value: Boolean) {
        error.value = value
    }

    override fun clear() {
        for (entry in entries) {
            entry.value = ""
        }
    }
}