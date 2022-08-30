package bruhcollective.itaysonlab.jetisteam.util

object SteamBbToMarkdown {
    fun bbcodeToMarkdown(src: String): String {
        return src
            .replace("\r", "")
            .replaceTagWith("h2") { "# $it" }
            .replaceTagWith("img") { "![image]($it)" }
    }

    private fun String.replaceTagWith(tag: String, func: (String) -> String) = replace(
        "\\[$tag](.+?)\\[/$tag]".toRegex()
    ) { result -> func(result.groupValues[1]) }
}