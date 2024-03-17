package bruhcollective.itaysonlab.cobalt.guard.bottom_sheet

interface GuardRecoveryCodeSheetComponent {
    val username: String
    val recoveryCode: String

    fun dismiss()
}