package bruhcollective.itaysonlab.jetisteam.models

enum class Language(val vdfName: String) {
    English("english"),
    German("german"),
    French("french"),
    Italian("italian"),
    Korean("koreana"),
    Spanish("spanish"),
    SimpleChinese("schinese"),
    TraditionalChinese("tchinese"),
    Russian("russian"),
    Thai("thai"),
    Japanese("japanese"),
    Portuguese("portuguese"),
    Polish("polish"),
    Danish("danish"),
    Dutch("dutch"),
    Finnish("finnish"),
    Norwegian("norwegian"),
    Swedish("swedish"),
    Hungarian("hungarian"),
    Czech("czech"),
    Romanian("romanian"),
    Turkish("turkish"),
    Arabic("arabic"),
    Brazilian("brazilian"),
    Bulgarian("bulgarian"),
    Greek("greek"),
    Ukrainian("ukrainian"),
    Latam("latam"),
    Vietnamese("vietnamese"),
    ScSimpleChinese("sc_schinese");

    companion object {
        val elanguageMap = values().associateBy { (it.ordinal + 1) }
    }
}