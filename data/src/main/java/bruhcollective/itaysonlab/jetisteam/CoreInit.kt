package bruhcollective.itaysonlab.jetisteam

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

object CoreInit {
    fun init() {
        Security.removeProvider("BC")
        Security.addProvider(BouncyCastleProvider())
    }
}