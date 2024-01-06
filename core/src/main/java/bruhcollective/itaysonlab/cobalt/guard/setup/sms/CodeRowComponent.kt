package bruhcollective.itaysonlab.cobalt.guard.setup.sms

import com.arkivanov.decompose.value.Value

interface CodeRowComponent {
    val codeLength: Int

    val inactive: Value<Boolean>
    val error: Value<Boolean>

    fun getEntryCode(index: Int): Value<String>
    fun setEntryCode(index: Int, char: String)
    fun onEntryFinish()

    fun setInactive(value: Boolean)
    fun setError(value: Boolean)
    fun clear()
}