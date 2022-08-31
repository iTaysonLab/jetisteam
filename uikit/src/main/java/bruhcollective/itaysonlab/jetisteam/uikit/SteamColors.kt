package bruhcollective.itaysonlab.jetisteam.uikit

import androidx.compose.ui.graphics.Color

object SteamColors {
    private val registeredThemes = mapOf(
        "Default" to ColorTheme(
            gradientBackground = Color(red = 34, green = 35, blue = 48, alpha = alpha(0.93f)),
            colorShowcaseHeader = Color(red = 43, green = 45, blue = 68, alpha = alpha(0.93f)),
            gradientShowcaseHeaderLeft = Color(red = 115, green = 173, blue = 184, alpha = alpha(0.247f)),
            btnBackground = Color(43, 52, 68),
            btnOutline = Color(93, 102, 118)
        ),

        "Cosmic" to ColorTheme(
            gradientBackground = Color(46, 13, 36, alpha = alpha(0.93f)),
            colorShowcaseHeader = Color(57, 24, 61, alpha = alpha(0.93f)),
            gradientShowcaseHeaderLeft = Color(20, 60, 68, alpha = alpha(0.93f)),
            btnBackground = Color(90, 40, 92),
            btnOutline = Color(140, 90, 142)
        ),

        "Summer" to ColorTheme(
            gradientBackground = Color(51, 27, 16, alpha = alpha(0.93f)),
            colorShowcaseHeader = Color(70, 53, 31, alpha = alpha(0.93f)),
            gradientShowcaseHeaderLeft = Color(33, 78, 76, alpha = alpha(0.93f)),
            btnBackground = Color(95, 76, 39),
            btnOutline = Color(145, 126, 89)
        ),

        "Midnight" to ColorTheme(
            gradientBackground = Color(10, 14, 32, alpha = alpha(0.93f)),
            colorShowcaseHeader = Color(34, 32, 61, alpha = alpha(0.93f)),
            gradientShowcaseHeaderLeft = Color(20, 33, 68, alpha = alpha(0.93f)),
            btnBackground = Color(38, 36, 68),
            btnOutline = Color(88, 86, 108)
        ),

        "DarkMode" to ColorTheme(
            gradientBackground = Color(24, 24, 24, alpha = alpha(0.93f)),
            colorShowcaseHeader = Color(0, 0, 0, alpha = alpha(0.5f)),
            gradientShowcaseHeaderLeft = Color(0, 0, 0, alpha = alpha(0.5f)),
            btnBackground = Color(40, 40, 40),
            btnOutline = Color(80, 80, 80)
        ),

        "Steel" to ColorTheme(
            gradientBackground = Color(41, 46, 51, alpha = alpha(0.93f)),
            colorShowcaseHeader = Color(55, 62, 76, alpha = alpha(0.93f)),
            gradientShowcaseHeaderLeft = Color(68, 83, 93, alpha = alpha(0.93f)),
            btnBackground = Color(66, 76, 92),
            btnOutline = Color(106, 126, 142)
        ),

        "SteamGreen" to ColorTheme(
            gradientBackground = Color(53, 58, 39),
            colorShowcaseHeader = Color(62, 69, 44),
            gradientShowcaseHeaderLeft = Color(83, 92, 60),
            btnBackground = Color(73, 82, 53),
            btnOutline = Color(92, 111, 45)
        ),

        "MutedBlue" to ColorTheme(
            gradientBackground = Color(0x2127338c),
            colorShowcaseHeader = Color(30, 44, 66, alpha(0.54f)),
            gradientShowcaseHeaderLeft = Color(69, 79, 108, alpha(0.75f)),
            btnBackground = Color(44, 55, 74),
            btnOutline = Color(86, 91, 108)
        ),

        "PinkTeal" to ColorTheme(
            gradientBackground = Color(0, 255, 255, alpha(0.15f)),
            colorShowcaseHeader = Color(42, 117, 122),
            gradientShowcaseHeaderLeft = Color(42, 117, 122),
            btnBackground = Color(42, 117, 122),
            btnOutline = Color(55, 162, 169)
        ),

        "MutedRed" to ColorTheme(
            gradientBackground = Color(96, 17, 2, alpha = alpha(0.30f)),
            colorShowcaseHeader = Color(49, 9, 9, alpha = alpha(0.81f)),
            gradientShowcaseHeaderLeft = Color(182, 27, 23, alpha = alpha(0.35f)),
            btnBackground = Color(58, 13, 10),
            btnOutline = Color(108, 90, 86)
        ),

        "BlueRedTheme" to ColorTheme(
            gradientBackground = Color(10, 25, 32, alpha = alpha(0.30f)),
            colorShowcaseHeader = Color(64, 23, 23, alpha = alpha(0.5f)),
            gradientShowcaseHeaderLeft = Color(27, 113, 132, alpha = alpha(0.93f)),
            btnBackground = Color(45, 93, 111),
            btnOutline = Color(86, 105, 108)
        ),

        "GoldBurgundyTheme" to ColorTheme(
            gradientBackground = Color(39, 27, 16, alpha = alpha(0.50f)),
            colorShowcaseHeader = Color(53, 28, 22, alpha = alpha(0.76f)),
            gradientShowcaseHeaderLeft = Color(120, 92, 20, alpha = alpha(0.69f)),
            btnBackground = Color(57, 38, 25),
            btnOutline = Color(108, 90, 86)
        ),

        "VibrantBlueTheme" to ColorTheme(
            gradientBackground = Color(0x0203048A),
            colorShowcaseHeader = Color(19, 57, 95),
            gradientShowcaseHeaderLeft = Color(39, 106, 141),
            btnBackground = Color(25, 70, 107),
            btnOutline = Color(34, 88, 133)
        ),

        "GoldenProfileDebutTheme" to ColorTheme(
            gradientBackground = Color(61, 47, 20, alpha(0.6f)),
            colorShowcaseHeader = Color(95, 72, 33, alpha(0.82f)),
            gradientShowcaseHeaderLeft = Color(155, 122, 54, alpha(0.9f)),
            btnBackground = Color(125, 98, 44),
            btnOutline = Color(175, 148, 94)
        ),

        "GoldTheme" to ColorTheme(
            gradientBackground = Color(116, 93, 44, alpha(0.64f)),
            colorShowcaseHeader = Color(185, 145, 76, alpha(0.76f)),
            gradientShowcaseHeaderLeft = Color(120, 95, 31, alpha(0.69f)),
            btnBackground = Color(145, 116, 66),
            btnOutline = Color(108, 90, 86)
        ),

        "BurntOrangeTheme" to ColorTheme(
            gradientBackground = Color(47, 24, 6, alpha(0.65f)),
            colorShowcaseHeader = Color(48, 12, 5, alpha(0.88f)),
            gradientShowcaseHeaderLeft = Color(168, 99, 5, alpha(0.67f)),
            btnBackground = Color(50, 15, 7),
            btnOutline = Color(86, 30, 17)
        ),

        "FlatGreyTheme" to ColorTheme(
            gradientBackground = Color(59, 62, 65),
            colorShowcaseHeader = Color(105, 110, 113),
            gradientShowcaseHeaderLeft = Color(105, 110, 113),
            btnBackground = Color(105, 110, 113),
            btnOutline = Color(105, 110, 113)
        ),

        "PurpleTheme" to ColorTheme(
            gradientBackground = Color(0x20024380),
            colorShowcaseHeader = Color(0xFF4c2f69),
            gradientShowcaseHeaderLeft = Color(0xFF29113f),
            btnBackground = Color(0xFF40245c),
            btnOutline = Color(0xFF603689)
        ),
    )

    fun getColorTheme(id: String) = registeredThemes[id] ?: default()
    fun default() = registeredThemes["Default"]!!

    class ColorTheme(
        val gradientBackground: Color,
        val colorShowcaseHeader: Color,
        val gradientShowcaseHeaderLeft: Color,
        val btnBackground: Color,
        val btnOutline: Color,
    )

    private fun alpha(prcnt: Float): Int = (prcnt * 255f).toInt()
}

/*

body.WinterProfile2020Theme {
	--gradient-right: #7770A988;
    --gradient-left: #34526F88;
    --gradient-background: #34526F88;
	--gradient-background-right: rgba(25, 20, 65, 0.333);
	--gradient-background-left: rgba(3, 7, 12, 0.333);
	--color-showcase-header: #07817f;
    --gradient-showcase-header-left: #34526F;
	--btn-background: #34526F;
    --btn-background-hover: rgb(54, 104, 151);
	--btn-outline: #34526F;
}

body.GoldenWeekProfile2021Theme {
    --gradient-right: #389bb0aa;
    --gradient-left: #2c5e83aa;
    --gradient-background: #134061aa;
    --gradient-background-right: #389bb066;
    --gradient-background-left: #2c5e8333;
    --color-showcase-header: #389bb0;
    --gradient-showcase-header-left: #2c5e83;
    --btn-background: #2c5e83;
    --btn-background-hover: #1b6daa;
    --btn-outline: #2c5e83;
}

body.Summer2021Theme {
    --gradient-right: #344f68;
    --gradient-left: #ee3b57b8;
    --gradient-background: #000000cc;
    --gradient-background-right: #c1ad4c52;
    --gradient-background-left: #2e6ea2a3;
    --color-showcase-header: #b03849a1;
    --gradient-showcase-header-left: #2e6986;
    --btn-background: #309186;
    --btn-background-hover: #3aa89c;
    --btn-outline: #2c5e83;
}

/* Halloween 2021 */
body.GreenSlimeTheme {
	--gradient-right: rgb(254 249 156 / 30%);
    --gradient-left: rgb(98 145 152 / 34%);
    --gradient-background: rgb(35 58 26 / 58%);
    --gradient-background-right: rgb(0 0 0);
    --gradient-background-left: rgb(0 0 0);
    --color-showcase-header: rgb(27 38 18);
    --gradient-showcase-header-left: rgb(113 141 46);
    --btn-background: rgb(33 45 20);
    --btn-background-hover: rgb(80 101 35);
    --btn-outline: rgb(86 109 37);
}

body.GhostTheme {
	--gradient-right: rgb(82 64 83 / 52%);
    --gradient-left: rgb(3 3 4 / 46%);
    --gradient-background: rgb(93 123 137 / 32%);
    --gradient-background-right: rgb(72 31 46);
    --gradient-background-left: rgb(0 0 0);
    --color-showcase-header: rgb(69 32 46);
    --gradient-showcase-header-left: rgb(93 123 136);
    --btn-background: rgb(93 122 135);
    --btn-background-hover: rgb(112 151 169);
    --btn-outline: rgb(126 126 126 / 27%);
}

body.ColorNightmareTheme {
	--gradient-right: rgb(58 183 180 / 51%);
    --gradient-left: rgb(243 106 9 / 25%);
    --gradient-background: rgb(0 0 0 / 50%);
    --gradient-background-right: rgb(0 0 0);
    --gradient-background-left: rgb(9 243 217 / 33%);
    --color-showcase-header: rgb(57 144 152 / 61%);
    --gradient-showcase-header-left: rgb(198 96 50);
    --btn-background: rgb(45 114 123);
    --btn-background-hover: rgb(196 96 50);
    --btn-outline: rgb(196 96 50);
}

body.MurugiahTheme {
	--gradient-right: rgb(84 75 158);
    --gradient-left: rgb(84 75 158);
    --gradient-background: rgb(84 75 158);
    --gradient-background-right: rgb(84 75 158);
    --gradient-background-left: rgb(84 75 158);
    --color-showcase-header: rgb(11 160 232);
    --gradient-showcase-header-left: rgb(11 160 232);
    --btn-background: rgb(225 69 40);
    --btn-background-hover: rgb(145 181 69);
    --btn-outline: rgb(225 69 40);
}

body.Winter2021Theme {
	--gradient-right: rgb(87 136 137 / 14%);
    --gradient-left: rgb(157 204 199 / 35%);
    --gradient-background: rgb(22 77 81 / 88%);
    --gradient-background-right: rgb(95 165 155 / 15%);
    --gradient-background-left: rgb(9 243 217 / 33%);
    --color-showcase-header: rgb(184 99 69);
    --gradient-showcase-header-left: rgb(207 132 36);
    --btn-background: rgb(184 99 69);
    --btn-background-hover: rgb(206 131 36);
    --btn-outline: rgb(196 96 50);
}

body.Lunar2022Theme {
	--gradient-right: rgb(232 63 12 / 78%);
    --gradient-left: rgb(239 65 12);
    --gradient-background: rgb(58 44 122 / 83%);
    --gradient-background-right: rgb(135 25 2 / 65%);
    --gradient-background-left: rgb(211 72 31 / 60%);
    --color-showcase-header: rgb(86 62 173);
    --gradient-showcase-header-left: rgb(234 101 69);
    --btn-background: rgb(86 62 173);
    --btn-background-hover: rgb(146 78 131);
    --btn-outline: rgb(173 85 113);
}

body.SteamDeckTheme {
	--gradient-right: transparent;
    --gradient-left: transparent;
    --gradient-background: transparent;
    --gradient-background-right: transparent;
    --gradient-background-left: transparent;
    --color-showcase-header: rgb(149 1 169 / 19%);
    --gradient-showcase-header-left: rgb(87 89 123 / 0%);
    --btn-background: rgb(29 10 25 / 10%);
    --btn-background-hover: rgb(175 52 149 / 21%);
    --btn-outline: rgb(29 10 25 / 0%);
}

body.Steam3000Theme {
--gradient-right: rgba(0, 0, 0, 0);
--gradient-left: rgba(0, 0, 0, 0);
--gradient-background: rgba(0, 0, 0, 0);
--gradient-background-right: rgba(0, 0, 0, 0);
--gradient-background-left: rgba(0, 0, 0, 0);
--color-showcase-header: rgb(231 196 52);
--gradient-showcase-header-left: rgb(239 0 255);
--btn-background: rgba(0, 0, 0, 0);
--btn-background-hover: rgb(46, 46, 46);
--btn-outline: rgba(0, 0, 0, 0);
}
 */