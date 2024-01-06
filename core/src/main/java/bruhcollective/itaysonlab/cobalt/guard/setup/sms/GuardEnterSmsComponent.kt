package bruhcollective.itaysonlab.cobalt.guard.setup.sms

interface GuardEnterSmsComponent {
    val codeRow: CodeRowComponent

    val phoneNumberHint: String
    val isMovingGuard: Boolean

    fun onExitClicked()
    fun onSubmitCodeClicked(code: String)
}