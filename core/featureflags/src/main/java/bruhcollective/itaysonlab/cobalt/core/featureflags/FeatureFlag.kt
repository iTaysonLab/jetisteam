package bruhcollective.itaysonlab.cobalt.core.featureflags

abstract class FeatureFlag (
    val id: String,
    val title: String,
    val description: String
) {
    abstract fun available(): Boolean
}