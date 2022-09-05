package bruhcollective.itaysonlab.jetisteam.rpc

inline fun <T> T.modifyIf(predicate: Boolean, ifTrue: T.() -> T) = let {
    if (predicate) ifTrue() else it
}