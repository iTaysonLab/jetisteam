package bruhcollective.itaysonlab.jetisteam.models

@JvmInline
value class SteamID (val steamId: Long) {
    val accountId get() = steamId and 0xFFFFFFFF
    val accountInstance get() = steamId ushr 32 and 0xFFFFF
    val accountType get() = steamId ushr 52 and 0xF
    val accountUniverse get() = steamId ushr 56 and 0xFF

    fun equalsWith(other: SteamID) = other.steamId == steamId
}