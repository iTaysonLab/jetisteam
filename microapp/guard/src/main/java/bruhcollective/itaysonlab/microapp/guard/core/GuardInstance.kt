package bruhcollective.itaysonlab.microapp.guard.core

import androidx.annotation.FloatRange
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.proto.GuardData
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import okio.ByteString
import okio.ByteString.Companion.toByteString
import okio.buffer
import okio.sink
import okio.use
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.time.Clock
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and
import kotlin.math.min
import kotlin.time.Duration.Companion.seconds

class GuardInstance(
    private val clock: Clock,
    private val configuration: GuardData
) {
    companion object {
        private const val AlgorithmTotp = "HmacSHA1"
        private const val AlgorithmConfirmation = "HmacSHA256"

        private const val Digits = 5
        private val Period = 30.seconds.inWholeMilliseconds
        private val Alphabet = "23456789BCDFGHJKMNPQRTVWXY".toCharArray()
    }

    val code = flow {
        do {
            emit(generateCode())
            delay(1000L)
        } while (currentCoroutineContext().isActive)
    }

    val revocationCode get() = configuration.revocation_code
    val username get() = configuration.account_name
    val configurationEncoded get() = GuardData.ADAPTER.encode(configuration)
    val steamId get() = SteamID(configuration.steam_id)

    private val secretKey = SecretKeySpec(configuration.shared_secret.toByteArray(), AlgorithmTotp)
    private val secretKeyIdentity = SecretKeySpec(configuration.identity_secret.toByteArray(), AlgorithmTotp)

    private val digest = Mac.getInstance(AlgorithmTotp).also { it.init(secretKey) }
    private val digestIdentity = Mac.getInstance(AlgorithmTotp).also { it.init(secretKeyIdentity) }

    private fun generateCode(): CodeModel {
        val currentTime = clock.millis()

        val progress = ((Period - ((currentTime) % Period)) / Period.toFloat()).coerceIn(0f..1f)
        val localDigest = digest.doFinal(ByteBuffer.allocate(8).putLong(currentTime / Period).array())

        val offset = (localDigest.last() and 0xf).toInt()
        val code = localDigest.copyOfRange(offset, offset + 4)
        code[0] = (0x7f and code[0].toInt()).toByte()

        return CodeModel(Triple(buildString {
            var remainingCodeInt = ByteBuffer.wrap(code).int
            repeat(Digits) {
                append(Alphabet[remainingCodeInt % Alphabet.size])
                remainingCodeInt /= 26
            }
        }, progress, currentTime))
    }

    fun generateCodeWithTime(): StaticAuthCode {
        return generateCode().let { StaticAuthCode(it.code to it.generatedAt) }
    }

    private fun digestSha256(msg: ByteArray): ByteArray {
        val localKey = SecretKeySpec(configuration.shared_secret.toByteArray(), AlgorithmConfirmation)
        val localDigest = Mac.getInstance(AlgorithmConfirmation).also { it.init(localKey) }
        return localDigest.doFinal(msg)
    }

    fun sgCreateSignature(version: Int, clientId: Long): ByteString {
        return ByteArrayOutputStream(2 + 8 + 8).apply {
            sink().buffer().use { sink ->
                sink.writeShortLe(version)
                sink.writeLongLe(clientId)
                sink.writeLongLe(steamId.steamId)
            }
        }.toByteArray().let(this::digestSha256).toByteString()
    }

    fun sgCreateRevokeSignature(tokenId: Long): ByteString {
        return ByteArrayOutputStream(20).apply {
            sink().buffer().use { sink ->
                sink.writeLong(tokenId)
            }
        }.toByteArray().let(this::digestSha256).toByteString()
    }

    suspend fun confirmationTicket(guardClockNormalizer: GuardClockNormalizer, tag: String): ConfirmationTicket {
        val currentTime = guardClockNormalizer.normalize(clock.millis())

        val base64Ticket = ByteArrayOutputStream(min(tag.length, 32) + 8).apply {
            sink().buffer().use { sink ->
                sink.writeLong(currentTime)
                sink.writeUtf8(tag)
            }
        }.toByteArray().let { arr ->
            digestIdentity.doFinal(arr)
        }.toByteString().base64()

        return ConfirmationTicket(base64Ticket to currentTime)
    }

    @JvmInline
    value class CodeModel(private val packed: Triple<String, Float, Long>) {
        companion object {
            val DefaultInstance = CodeModel(Triple("", 0f, 0L))
        }

        val code: String get() = packed.first

        @get:FloatRange(from = 0.0, to = 1.0)
        val progressRemaining: Float get() = packed.second

        val generatedAt: Long get() = packed.third
    }

    @JvmInline
    value class ConfirmationTicket(private val packed: Pair<String, Long>) {
        val b64EncodedSignature: String get() = packed.first
        val generationTime: Long get() = packed.second
    }

    @JvmInline
    value class StaticAuthCode(private val packed: Pair<String, Long>) {
        val _proto get() = packed
        val codeString: String get() = packed.first
        val generationTime: Long get() = packed.second
    }
}