package bruhcollective.itaysonlab.jetisteam.models

@JvmInline
value class SteamID (val steamId: Long) {
    val accountId get() = steamId and 0xFFFFFFFF
    val accountInstance get() = steamId ushr 32 and 0xFFFFF
    val accountType get() = steamId ushr 52 and 0xF
    val accountUniverse get() = steamId ushr 56 and 0xFF

    fun equalsWith(other: SteamID) = other.steamId == steamId
}

enum class SteamInstance (val apiRepresentation: Int) {
    Other(0),
    SteamUserDesktop(1),
    SteamUserConsole(2),
    SteamUserWeb(4),
}

enum class SteamAccountType (val apiRepresentation: Int) {
    Normal(1),
    Clan(7)
}

enum class SteamUniverse (val apiRepresentation: Int) {
    Production(1)
}

fun steamIdFromAccountId(
    id: Long,
    instance: SteamInstance = SteamInstance.SteamUserDesktop,
    type: SteamAccountType = SteamAccountType.Normal,
    universe: SteamUniverse = SteamUniverse.Production
): SteamID {
    return 0L
        .setMask(0, 0xFFFFFFFF, id)
        .setMask(32, 0xFFFFF, instance.apiRepresentation.toLong())
        .setMask(52, 0xF, type.apiRepresentation.toLong())
        .setMask(56, 0xFF, universe.apiRepresentation.toLong())
        .let { SteamID(it) }
}

private fun Long.setMask(bitoffset: Int, valuemask: Long, value: Long): Long {
    return this and (valuemask shl bitoffset).inv() or (value and valuemask shl bitoffset)
}