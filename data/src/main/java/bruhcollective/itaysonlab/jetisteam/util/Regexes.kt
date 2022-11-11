package bruhcollective.itaysonlab.jetisteam.util

object Regexes {
    @Suppress("RegExpRedundantEscape")
    private val QR_AUTH_LINK = "https:\\/\\/s\\.team\\/q\\/(.+?)\\/(.+)".toRegex()

    fun extractDataFromQr(value: String): QrData? {
        val data = QR_AUTH_LINK.matchEntire(value) ?: return null
        return QrData((data.groups[1]?.value?.toInt() ?: return null) to (java.lang.Long.parseUnsignedLong(data.groups[2]?.value ?: return null)))
    }

    fun qrMatches(value: String) = value matches QR_AUTH_LINK

    //

    @JvmInline
    value class QrData (private val packed: Pair<Int, Long>) {
        val version get() = packed.first
        val sessionId get() = packed.second
    }
}