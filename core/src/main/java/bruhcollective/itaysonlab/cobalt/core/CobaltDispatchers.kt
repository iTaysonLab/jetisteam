package bruhcollective.itaysonlab.cobalt.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlin.coroutines.CoroutineContext

interface CobaltDispatchers {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
}