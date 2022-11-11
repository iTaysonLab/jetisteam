package bruhcollective.itaysonlab.jetisteam.data

import bruhcollective.itaysonlab.jetisteam.util.Regexes
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Test

class RegexTest {
    @Test
    fun steamSignIn_parsesCorrectly() {
        val data = Regexes.extractDataFromQr("https://s.team/q/1/1234567890")
        assertNotNull(data)
        assertEquals(data!!.version, 1)
        assertEquals(data.sessionId, 1234567890L)
    }
}