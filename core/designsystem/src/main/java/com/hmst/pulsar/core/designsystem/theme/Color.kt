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
private val Night = Color(0xFF081A15)
private val NightSurface = Color(0xFF10291F)
private val NightSurfaceHigh = Color(0xFF173829)
private val NightText = Color(0xFFE6F6F0)
private val NightTextVariant = Color(0xFF8FB3A5)
private val OutlineDark = Color(0xFF3A6A57)
private val OutlineVariantDark = Color(0xFF244A3A)
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
    surface = Color.White,
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
    primary = Emerald,
    onPrimary = Night,
    primaryContainer = Color(0xFF0B3D2E),
    onPrimaryContainer = Color(0xFFBFF0DA),
    secondary = EmeraldLight,
    onSecondary = Night,
    secondaryContainer = NightSurfaceHigh,
    onSecondaryContainer = NightText,
    background = Night,
    onBackground = NightText,
    surface = Night,
    onSurface = NightText,
    surfaceVariant = NightSurfaceHigh,
    onSurfaceVariant = NightTextVariant,
    surfaceContainerLowest = Color(0xFF05110E),
    surfaceContainerLow = Night,
    surfaceContainer = NightSurface,
    surfaceContainerHigh = NightSurfaceHigh,
    surfaceContainerHighest = Color(0xFF1D4533),
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    error = CoralDark,
    onError = Night,
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