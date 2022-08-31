package bruhcollective.itaysonlab.jetisteam.util

object SteamBbToMarkdown {
    fun bbcodeToMarkdown(src: String): String {
        return src
            .replace("\r", "")
            .replace("[*]", "* ")
            .replace("[list]", "")
            .replace("[/list]", "")
            .replaceTagWith("h2") { "# $it" }
            .replaceTagWith("img") { "![image]($it)" }
            .replaceTagWith("b") { "**$it**" }
            .replace("\\[url=(.+?)](.+?)\\[/url]".toRegex()) { result ->
                "[${result.groupValues[2]}](${result.groupValues[1]})"
            }
    }

    private fun String.replaceTagWith(tag: String, func: (String) -> String) = replace(
        "\\[$tag](.+?)\\[/$tag]".toRegex()
    ) { result -> func(result.groupValues[1]) }
}