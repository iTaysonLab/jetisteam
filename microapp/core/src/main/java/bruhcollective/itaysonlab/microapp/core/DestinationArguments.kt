package bruhcollective.itaysonlab.microapp.core

@JvmInline
value class DestNode(val url: String) {
    internal companion object {
        val REGEX = "\\{(.+?)\\}".toRegex()
    }
}

fun DestNode.map(arguments: Map<String, String>): String {
    return url.replace(DestNode.REGEX) { result ->
        arguments[result.groupValues[1]]!!
    }
}