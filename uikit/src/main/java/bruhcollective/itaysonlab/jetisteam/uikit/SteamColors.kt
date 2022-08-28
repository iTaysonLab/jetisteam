package bruhcollective.itaysonlab.jetisteam.uikit

import androidx.compose.ui.graphics.Color

object SteamColors {
    private val registeredThemes = mapOf(
        "Default" to ColorTheme(
            gradientRight = Color(red = 109, green = 38, blue = 44, alpha = alpha(0.301f)),
            gradientLeft = Color(red = 50, green = 255, blue = 193, alpha = alpha(0.103f)),
            gradientBackground = Color(red = 34, green = 35, blue = 48, alpha = alpha(0.93f)),
            gradientBackgroundRight = Color.Transparent,
            gradientBackgroundLeft = Color(red = 50, green = 255, blue = 193, alpha = alpha(0.103f)),
            colorShowcaseHeader = Color(red = 43, green = 45, blue = 68, alpha = alpha(0.93f)),
            gradientShowcaseHeaderLeft = Color(red = 115, green = 173, blue = 184, alpha = alpha(0.247f)),
            btnBackground = Color(43, 52, 68),
            btnOutline = Color(93, 102, 118)
        ),

        "Cosmic" to ColorTheme(
            gradientRight = Color(248, 70, 180, alpha = alpha(0.301f)),
            gradientLeft = Color(9, 243, 99, alpha = alpha(0.247f)),
            gradientBackground = Color(46, 13, 36, alpha = alpha(0.93f)),
            gradientBackgroundRight = Color.Transparent,
            gradientBackgroundLeft = Color(239, 243, 9, alpha = alpha(0.13f)),
            colorShowcaseHeader = Color(57, 24, 61, alpha = alpha(0.93f)),
            gradientShowcaseHeaderLeft = Color(20, 60, 68, alpha = alpha(0.93f)),
            btnBackground = Color(90, 40, 92),
            btnOutline = Color(140, 90, 142)
        ),

        "Summer" to ColorTheme(
            gradientRight = Color(252, 197, 16, alpha = alpha(0.301f)),
            gradientLeft = Color(9, 243, 153, alpha = alpha(0.247f)),
            gradientBackground = Color(51, 27, 16, alpha = alpha(0.93f)),
            gradientBackgroundRight = Color.Transparent,
            gradientBackgroundLeft = Color(48, 243, 9, alpha = alpha(0.13f)),
            colorShowcaseHeader = Color(70, 53, 31, alpha = alpha(0.93f)),
            gradientShowcaseHeaderLeft = Color(33, 78, 76, alpha = alpha(0.93f)),
            btnBackground = Color(95, 76, 39),
            btnOutline = Color(145, 126, 89)
        ),

        "Midnight" to ColorTheme(
            gradientRight = Color(51, 54, 253, alpha = alpha(0.233f)),
            gradientLeft = Color(12, 85, 241, alpha = alpha(0.37f)),
            gradientBackground = Color(10, 14, 32, alpha = alpha(0.93f)),
            gradientBackgroundRight = Color.Transparent,
            gradientBackgroundLeft = Color(12, 85, 241, alpha = alpha(0.13f)),
            colorShowcaseHeader = Color(34, 32, 61, alpha = alpha(0.93f)),
            gradientShowcaseHeaderLeft = Color(20, 33, 68, alpha = alpha(0.93f)),
            btnBackground = Color(38, 36, 68),
            btnOutline = Color(88, 86, 108)
        ),

        "DarkMode" to ColorTheme(
            gradientRight = Color(49, 49, 49, alpha = alpha(0.233f)),
            gradientLeft = Color(51, 51, 51, alpha = alpha(0.37f)),
            gradientBackground = Color(24, 24, 24, alpha = alpha(0.93f)),
            gradientBackgroundRight = Color.Transparent,
            gradientBackgroundLeft = Color(32, 32, 32, alpha = alpha(0.13f)),
            colorShowcaseHeader = Color(0, 0, 0, alpha = alpha(0.5f)),
            gradientShowcaseHeaderLeft = Color(0, 0, 0, alpha = alpha(0.5f)),
            btnBackground = Color(40, 40, 40),
            btnOutline = Color(80, 80, 80)
        ),

        "Steel" to ColorTheme(
            gradientRight = Color(70, 106, 128, alpha = alpha(0.233f)),
            gradientLeft = Color(86, 120, 134, alpha = alpha(0.37f)),
            gradientBackground = Color(41, 46, 51, alpha = alpha(0.93f)),
            gradientBackgroundRight = Color.Transparent,
            gradientBackgroundLeft = Color.Transparent,
            colorShowcaseHeader = Color(55, 62, 76, alpha = alpha(0.93f)),
            gradientShowcaseHeaderLeft = Color(68, 83, 93, alpha = alpha(0.93f)),
            btnBackground = Color(66, 76, 92),
            btnOutline = Color(106, 126, 142)
        ),

        "SteamGreen" to ColorTheme(
            gradientRight = Color(0xFF393e2b),
            gradientLeft = Color(71, 78, 52),
            gradientBackground = Color(53, 58, 39),
            gradientBackgroundRight = Color.Transparent,
            gradientBackgroundLeft = Color.Transparent,
            colorShowcaseHeader = Color(62, 69, 44),
            gradientShowcaseHeaderLeft = Color(83, 92, 60),
            btnBackground = Color(73, 82, 53),
            btnOutline = Color(92, 111, 45)
        ),

        "MutedBlue" to ColorTheme(
            gradientRight = Color(0xFF737d96),
            gradientLeft = Color(115, 124, 147),
            gradientBackground = Color(0x2127338c),
            gradientBackgroundRight = Color.Transparent,
            gradientBackgroundLeft = Color(20, 26, 49, alpha(0.13f)),
            colorShowcaseHeader = Color(30, 44, 66, alpha(0.54f)),
            gradientShowcaseHeaderLeft = Color(69, 79, 108, alpha(0.75f)),
            btnBackground = Color(44, 55, 74),
            btnOutline = Color(86, 91, 108)
        ),

        "PinkTeal" to ColorTheme(
            gradientRight = Color(139, 29, 68),
            gradientLeft = Color(137, 30, 67),
            gradientBackground = Color(0, 255, 255, alpha(0.15f)),
            gradientBackgroundRight = Color.Transparent,
            gradientBackgroundLeft = Color.Transparent,
            colorShowcaseHeader = Color(42, 117, 122),
            gradientShowcaseHeaderLeft = Color(42, 117, 122),
            btnBackground = Color(42, 117, 122),
            btnOutline = Color(55, 162, 169)
        ),

        "MutedRed" to ColorTheme(
            gradientRight = Color(142, 40, 40, alpha = alpha(0.23f)),
            gradientLeft = Color(163, 53, 53, alpha = alpha(0.37f)),
            gradientBackground = Color(96, 17, 2, alpha = alpha(0.30f)),
            gradientBackgroundRight = Color.Transparent,
            gradientBackgroundLeft = Color(241, 12, 12, alpha = alpha(0.13f)),
            colorShowcaseHeader = Color(49, 9, 9, alpha = alpha(0.81f)),
            gradientShowcaseHeaderLeft = Color(182, 27, 23, alpha = alpha(0.35f)),
            btnBackground = Color(58, 13, 10),
            btnOutline = Color(108, 90, 86)
        ),

        // TODO: RE8, CSGO, Civ7, Events, more...
    )

    fun getColorTheme(id: String) = registeredThemes[id] ?: default()
    fun default() = registeredThemes["Default"]!!

    class ColorTheme(
        val gradientRight: Color,
        val gradientLeft: Color,
        val gradientBackground: Color,
        val gradientBackgroundRight: Color,
        val gradientBackgroundLeft: Color,
        val colorShowcaseHeader: Color,
        val gradientShowcaseHeaderLeft: Color,
        val btnBackground: Color,
        val btnOutline: Color,
    )

    private fun alpha(prcnt: Float): Int = (prcnt * 255f).toInt()
}

/*

/* -- BlueRedTheme -- (CSGO) */
body.BlueRedTheme {
	--gradient-right: rgb(253 56 51 / 23%);
    --gradient-left: rgb(12 230 241 / 37%);
    --gradient-background: rgb(10 25 32 / 30%);
    --gradient-background-right: rgb(253 51 51 / 15%);
    --gradient-background-left: rgb(12 241 241 / 13%);
    --color-showcase-header: rgb(64 23 23 / 50%);
    --gradient-showcase-header-left: rgb(27 113 132 / 93%);
    --btn-background: rgb(45 93 111);
    --btn-background-hover: rgb(36 121 130);
    --btn-outline: rgb(86 105 108);
}

/* -- GoldBurgundyTheme -- (Resident Evil Village - Castle) */
body.GoldBurgundyTheme {
	--gradient-right: rgb(253 219 51 / 23%);
    --gradient-left: rgb(97 75 37);
    --gradient-background: rgb(39 27 16 / 50%);
    --gradient-background-right: rgb(0 0 0 / 35%);
    --gradient-background-left: rgb(0 0 0 / 35%);
    --color-showcase-header: rgb(53 28 22 / 76%);
    --gradient-showcase-header-left: rgb(120 92 20 / 69%);
    --btn-background: rgb(57 38 25);
    --btn-background-hover: rgb(77 52 36);
    --btn-outline: rgb(108, 90, 86);
}

/* -- VibrantBlueTheme -- (Civilization VI) */
body.VibrantBlueTheme {
	--gradient-right: #008ada8a;
    --gradient-left: rgb(47 137 188);
    --gradient-background: #0203048a;
    --gradient-background-right: rgb(253 51 51 / 0%);
    --gradient-background-left: rgb(12 140 241 / 17%);
    --color-showcase-header: rgb(19 57 95);
    --gradient-showcase-header-left: rgb(39 106 141);
    --btn-background: rgb(25 70 107);
    --btn-background-hover: rgb(35 87 130);
    --btn-outline: rgb(34 88 133);
}

body.GoldenProfileDebutTheme {
    --gradient-right: rgba(156, 66, 17, 0.18);
    --gradient-left: rgba(213, 172, 81, 0.62);
	--gradient-background: rgba(61, 47, 20, 0.6);
    --gradient-background-right: rgba(0, 0, 0, 0);
    --gradient-background-left: rgba(243, 200, 9, 0.13);
    --color-showcase-header: rgba(95, 72, 33, 0.82);
    --gradient-showcase-header-left: rgba(155, 122, 54, 0.9);
    --btn-background: rgb(125, 98, 44);
    --btn-background-hover: rgb(135, 108, 54);
    --btn-outline: rgb(175, 148, 94);
}

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

/* -- NEW THEMES 6.23.21 -- */

/* (Evil Genius 2) */
body.GoldTheme {
    --gradient-right: rgb(197 164 79 / 73%);
    --gradient-left: rgb(128 94 36);
    --gradient-background: rgb(116 93 44 / 64%);
    --gradient-background-right: rgb(0 0 0 / 35%);
    --gradient-background-left: rgb(0 0 0 / 35%);
    --color-showcase-header: rgb(185 145 76 / 76%);
    --gradient-showcase-header-left: rgb(120 95 31 / 69%);
    --btn-background: rgb(145 116 66);
    --btn-background-hover: rgb(169 132 68);
	--btn-outline: rgb(108, 90, 86);
}

/* (Dying Light) */
body.BurntOrangeTheme {
    --gradient-right: rgb(136 66 8 / 66%);
    --gradient-left: rgb(251 250 251 / 48%);
    --gradient-background: rgb(47 24 6 / 65%);
    --gradient-background-right: rgb(19 8 2);
    --gradient-background-left: rgb(8 2 5 / 0%);
    --color-showcase-header: rgb(48 12 5 / 88%);
    --gradient-showcase-header-left: rgb(168 99 5 / 67%);
    --btn-background: rgb(50 15 7);
    --btn-background-hover: rgb(89 31 17);
    --btn-outline: rgb(86 30 17);
}

/* (Loop Hero) */
body.FlatGreyTheme {
	--gradient-right: #3b3e4100;
    --gradient-left: rgb(59 62 65 / 0%);
    --gradient-background: rgb(59 62 65);
    --gradient-background-right: rgb(59 62 65 / 0%);
    --gradient-background-left: rgb(59 62 65 / 0%);
    --color-showcase-header: rgb(105 110 113);
    --gradient-showcase-header-left: rgb(105 110 113);
    --btn-background: rgb(105 110 113);
    --btn-background-hover: rgb(118 122 124);
    --btn-outline: rgb(105 110 113);
}

/* (Outriders - Main) */
body.PurpleTheme {
	--gradient-right: #3c2c4c;
    --gradient-left: #52298094;
    --gradient-background: #20024380;
    --gradient-background-right: #0f021f;
    --gradient-background-left: #150329;
    --color-showcase-header: #4c2f69;
    --gradient-showcase-header-left: #29113f;
    --btn-background: #40245c;
    --btn-background-hover: #5f3c80;
    --btn-outline: #603689;
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