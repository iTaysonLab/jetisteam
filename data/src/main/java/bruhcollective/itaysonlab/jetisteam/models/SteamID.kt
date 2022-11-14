package bruhcollective.itaysonlab.jetisteam.models

@JvmInline
value class SteamID (val steamId: Long) {
    val accountId get() = steamId and 0xFFFFFFFF
    val accountInstance get() = steamId ushr 32 and 0xFFFFF
    val accountType get() = steamId ushr 52 and 0xF
    val accountUniverse get() = steamId ushr 56 and 0xFF

    fun equalsWith(other: SteamID) = other.steamId == steamId
}

fun steamIdFromAccountId(id: Long, instance: Short = 0, type: Short = 1, universe: Short = 1): SteamID {
    return 0L
        .setMask(0, 0xFFFFFFFF, id)
        .setMask(32, 0xFFFFF, instance.toLong())
        .setMask(52, 0xF, type.toLong())
        .setMask(56, 0xFF, universe.toLong())
        .let { SteamID(it) }
}

private fun Long.setMask(bitoffset: Int, valuemask: Long, value: Long): Long {
    return this and (valuemask shl bitoffset).inv() or (value and valuemask shl bitoffset)
}