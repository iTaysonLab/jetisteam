package bruhcollective.itaysonlab.jetisteam.ext

import steam.common.CMsgIPAddress

val CMsgIPAddress.ipString: String get() {
    return buildString {
        if (v4 != null) {
            append((v4 shr 24) and 255)
            append(".")
            append((v4 shr 16) and 255)
            append(".")
            append((v4 shr 8) and 255)
            append(".")
            append((v4 shr 0) and 255)
        } else if (v6 != null) {
            append("IPv6 not supported for parsing yet")
        }
    }
}