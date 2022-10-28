package bruhcollective.itaysonlab.microapp.core.ext

import androidx.lifecycle.SavedStateHandle
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import okio.ByteString.Companion.EMPTY
import okio.ByteString.Companion.decodeBase64

fun SavedStateHandle.getString(key: String) = get<String>(key).orEmpty()
fun SavedStateHandle.getLongFromString(key: String) = (get<String>(key) ?: "0").toLong()
fun SavedStateHandle.getBooleanFromString(key: String) = (get<String>(key) ?: "false").toBoolean()
fun SavedStateHandle.getSteamId(key: String) = SteamID(getLongFromString(key))
fun SavedStateHandle.getBase64(key: String) = getString(key).decodeBase64() ?: EMPTY