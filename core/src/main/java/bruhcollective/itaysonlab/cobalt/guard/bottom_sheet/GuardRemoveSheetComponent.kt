package bruhcollective.itaysonlab.cobalt.guard.bottom_sheet

import com.arkivanov.decompose.value.Value

interface GuardRemoveSheetComponent {
    val isRemovalInProgress: Value<Boolean>
    val username: String

    fun confirmDeletion()
    fun dismiss()
}