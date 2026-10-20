package com.hmst.pulsar.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// Brand (lấy từ icon)
private val Emerald = Color(0xFF10B981)
private val EmeraldLight = Color(0xFF34D399)
private val Foam = Color(0xFFCFF1E6)

// Light
private val Mist = Color(0xFFF2FBF8)
private val Forest = Color(0xFF0B3D2E)
private val ForestVariant = Color(0xFF4B6B5F)
private val OutlineLight = Color(0xFF9DC4B5)
private val OutlineVariantLight = Color(0xFFD5EBE3)
private val Coral = Color(0xFFF4566B)

// Dark
private val Night = Color(0xFF0C1311)
private val NightSurface = Color(0xFF131D19)
private val NightSurfaceHigh = Color(0xFF182420)
private val NightText = Color(0xFFE4EEEA)
private val NightTextVariant = Color(0xFF93A7A0)
private val OutlineDark = Color(0xFF3B4D46)
private val OutlineVariantDark = Color(0xFF25332E)
private val CoralDark = Color(0xFFFF6B7E)

val PulsarLightColors: ColorScheme = lightColorScheme(
    primary = Emerald,
    onPrimary = Forest,
    primaryContainer = Foam,
    onPrimaryContainer = Forest,
    secondary = Forest,
    onSecondary = Color.White,
    secondaryContainer = Foam,
    onSecondaryContainer = Forest,
    background = Mist,
    onBackground = Forest,
    surface = Mist,
    onSurface = Forest,
    surfaceVariant = Color(0xFFE3F4EE),
    onSurfaceVariant = ForestVariant,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Mist,
    surfaceContainer = Color(0xFFE9F7F2),
    surfaceContainerHigh = Color(0xFFE3F4EE),
    surfaceContainerHighest = Foam,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
    error = Coral,
    onError = Color.White,
)

val PulsarDarkColors: ColorScheme = darkColorScheme(
    primary = EmeraldLight,
    onPrimary = Color(0xFF04261B),
    primaryContainer = Color(0xFF123D2F),
    onPrimaryContainer = Color(0xFFBFF0DA),
    secondary = Color(0xFF7FD6B8),
    onSecondary = Color(0xFF04261B),
    secondaryContainer = Color(0xFF1B3A30),
    onSecondaryContainer = Color(0xFFBFF0DA),      // đổi: NightText -> mint nhạt
    background = Night,
    onBackground = NightText,
    surface = Night,
    onSurface = NightText,
    surfaceVariant = Color(0xFF1E2D28),            // đổi: NightSurfaceHigh -> tách khỏi card
    onSurfaceVariant = NightTextVariant,
    surfaceContainerLowest = Color(0xFF080D0B),    // đổi
    surfaceContainerLow = Color(0xFF0F1714),       // đổi: không còn bằng Night
    surfaceContainer = NightSurface,               // bottom bar
    surfaceContainerHigh = NightSurfaceHigh,       // card
    surfaceContainerHighest = Color(0xFF1E2D28),   // đổi: bỏ 0xFF1D4533 quá xanh
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    error = CoralDark,
    onError = Color(0xFF2A0A10),                   // đổi: Night -> đỏ rất đậm
)

// Màu riêng của thị trường chứng khoán VN (Material không có sẵn)
@Immutable
data class MarketColors(
    val up: Color,
    val down: Color,
    val reference: Color,
    val ceiling: Color,
    val floor: Color,
)

val LightMarketColors = MarketColors(
    up = Color(0xFF16A34A),
    down = Coral,
    reference = Color(0xFFF5B700),
    ceiling = Color(0xFFC026D3),
    floor = Color(0xFF06B6D4),
)

val DarkMarketColors = MarketColors(
    up = Color(0xFF22C55E),
    down = CoralDark,
    reference = Color(0xFFFFC83D),
    ceiling = Color(0xFFD946EF),
    floor = Color(0xFF22D3EE),
)