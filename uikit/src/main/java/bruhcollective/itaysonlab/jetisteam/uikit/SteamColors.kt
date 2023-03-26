package bruhcollective.itaysonlab.jetisteam.uikit

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.persistentMapOf

object SteamColors {
    private val registeredThemes = persistentMapOf(
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

        "WinterProfile2020Theme" to ColorTheme(
            gradientBackground = Color(0x34526F88),
            colorShowcaseHeader = Color(0xFF07817f),
            gradientShowcaseHeaderLeft = Color(0xFF34526F),
            btnBackground = Color(0xFF34526F),
            btnOutline = Color(0xFF34526F)
        ),

        "GoldenWeekProfile2021Theme" to ColorTheme(
            gradientBackground = Color(0x134061aa),
            colorShowcaseHeader = Color(0xFF389bb0),
            gradientShowcaseHeaderLeft = Color(0xFF2c5e83),
            btnBackground = Color(0xFF2c5e83),
            btnOutline = Color(0xFF2c5e83)
        ),

        "Summer2021Theme" to ColorTheme(
            gradientBackground = Color(0xFF344f68),
            colorShowcaseHeader = Color(0xb03849a1),
            gradientShowcaseHeaderLeft = Color(0xFF2e6986),
            btnBackground = Color(0xFF309186),
            btnOutline = Color(0xFF2c5e83)
        ),

        "GreenSlimeTheme" to ColorTheme(
            gradientBackground = Color(35, 58, 26, alpha(0.58f)),
            colorShowcaseHeader = Color(0xFF1b2612),
            gradientShowcaseHeaderLeft = Color(0xFF718d2e),
            btnBackground = Color(0xFF212d14),
            btnOutline = Color(0xFF566d25)
        ),

        "GhostTheme" to ColorTheme(
            gradientBackground = Color(93, 123, 137, alpha = alpha(0.32f)),
            colorShowcaseHeader = Color(69, 32, 46),
            gradientShowcaseHeaderLeft = Color(93, 123, 136),
            btnBackground = Color(93, 122, 135),
            btnOutline = Color(126, 126, 126)
        ),

        "ColorNightmareTheme" to ColorTheme(
            gradientBackground = Color(58, 183, 180, alpha = alpha(0.51f)),
            colorShowcaseHeader = Color(57, 144, 152),
            gradientShowcaseHeaderLeft = Color(198, 96, 50),
            btnBackground = Color(45, 114, 123),
            btnOutline = Color(196, 96, 50)
        ),

        "MurugiahTheme" to ColorTheme(
            gradientBackground = Color(84, 75, 158),
            colorShowcaseHeader = Color(11, 160, 232),
            gradientShowcaseHeaderLeft = Color(11, 160, 232),
            btnBackground = Color(225, 69, 40),
            btnOutline = Color(225, 69, 40)
        ),

        "Winter2021Theme" to ColorTheme(
            gradientBackground = Color(22, 77, 81, alpha = alpha(0.88f)),
            colorShowcaseHeader = Color(184, 99, 69),
            gradientShowcaseHeaderLeft = Color(207, 132, 36),
            btnBackground = Color(184, 99, 69),
            btnOutline = Color(196, 96, 50)
        ),

        "Lunar2022Theme" to ColorTheme(
            gradientBackground = Color(58, 44, 122, alpha = alpha(0.83f)),
            colorShowcaseHeader = Color(86, 62, 173),
            gradientShowcaseHeaderLeft = Color(234, 101, 69),
            btnBackground = Color(86, 62, 173),
            btnOutline = Color(173, 85, 113)
        ),

        "SteamDeckTheme" to ColorTheme(
            gradientBackground = Color.Black,
            colorShowcaseHeader = Color(149, 1, 169, alpha = alpha(0.19f)),
            gradientShowcaseHeaderLeft = Color(87, 89, 123),
            btnBackground = Color(29, 10, 25),
            btnOutline = Color(29, 10, 25)
        ),

        "Steam3000Theme" to ColorTheme(
            gradientBackground = Color.Black,
            colorShowcaseHeader = Color(231, 196, 52),
            gradientShowcaseHeaderLeft = Color(239, 0, 255),
            btnBackground = Color.Black,
            btnOutline = Color.Black
        ),
    )

    fun getColorTheme(id: String?) = id?.let { registeredThemes[it] } ?: default()
    fun default() = registeredThemes["Default"]!!

    @Immutable
    class ColorTheme(
        val gradientBackground: Color,
        val colorShowcaseHeader: Color,
        val gradientShowcaseHeaderLeft: Color,
        val btnBackground: Color,
        val btnOutline: Color,
    )

    private fun alpha(prcnt: Float): Int = (prcnt * 255f).toInt()
}