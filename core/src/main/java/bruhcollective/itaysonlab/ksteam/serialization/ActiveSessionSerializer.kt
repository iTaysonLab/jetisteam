package bruhcollective.itaysonlab.ksteam.serialization

import bruhcollective.itaysonlab.ksteam.guard.models.ActiveSession
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import okio.ByteString.Companion.decodeBase64
import steam.webui.authentication.CAuthentication_RefreshToken_Enumerate_Response_RefreshTokenDescription

class ActiveSessionSerializer: KSerializer<ActiveSession> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ActiveSessionProtoWrapper") {
        element<String>("proto_b64")
    }

    override fun deserialize(decoder: Decoder): ActiveSession {
        return decoder.decodeStructure(descriptor) {
            var b64: String? = null

            loop@ while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    DECODE_DONE -> break@loop
                    0 -> b64 = decodeStringElement(descriptor, 0)
                    else -> throw SerializationException("Unexpected index $index")
                }
            }

            ActiveSession(
                CAuthentication_RefreshToken_Enumerate_Response_RefreshTokenDescription.ADAPTER.decode(
                    requireNotNull(requireNotNull(b64).decodeBase64())
                )
            )
        }
    }

    override fun serialize(encoder: Encoder, value: ActiveSession) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.protoBytes().base64())
        }
    }
}