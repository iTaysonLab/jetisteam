package bruhcollective.itaysonlab.microapp.core.ext

import androidx.lifecycle.SavedStateHandle
import bruhcollective.itaysonlab.ksteam.models.SteamId
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments
import com.squareup.wire.Message
import okio.ByteString.Companion.EMPTY
import okio.ByteString.Companion.decodeBase64

fun SavedStateHandle.getString(key: String) = get<String>(key).orEmpty()
fun SavedStateHandle.getLongFromString(key: String) = (get<String>(key) ?: "0").toLong()
fun SavedStateHandle.getBooleanFromString(key: String) = (get<String>(key) ?: "false").toBoolean()
fun SavedStateHandle.getSteamId() = SteamId(get<Long>(CommonArguments.SteamId.name)?.toULong() ?: error("No SteamID specific in SavedState"))

fun SavedStateHandle.getBase64(key: String) = getString(key).decodeBase64() ?: EMPTY

fun Message<*, *>.asBase64() = encodeByteString().base64Url()