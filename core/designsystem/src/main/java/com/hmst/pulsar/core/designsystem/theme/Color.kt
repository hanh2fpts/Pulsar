package com.hmst.pulsar.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// Thương hiệu: than chì trung tính, không ngả sắc nào
private val Graphite = Color(0xFF111827)      // primary sáng, onSurface sáng
private val Frost = Color(0xFFF2F3F5)         // primary tối

// Giao diện sáng
private val Paper = Color(0xFFF6F6F7)         // background
private val Silver = Color(0xFFE5E7EB)        // primaryContainer
private val Fog = Color(0xFFE8E9EC)           // surfaceVariant, surfaceContainerHigh
private val Haze = Color(0xFFEEEFF1)          // surfaceContainer
private val Slate = Color(0xFF5B6270)         // onSurfaceVariant
private val OutlineLight = Color(0xFFA0A5AF)
private val OutlineVariantLight = Color(0xFFDFE1E5)
private val Coral = Color(0xFFF4566B)

// Giao diện tối
private val Ink = Color(0xFF0E1013)           // background
private val InkLowest = Color(0xFF07080A)
private val InkLow = Color(0xFF101318)
private val InkContainer = Color(0xFF14171C)
private val InkHigh = Color(0xFF1B1F26)
private val InkVariant = Color(0xFF232831)
private val InkText = Color(0xFFECEEF1)
private val InkTextVariant = Color(0xFF9BA1AD)
private val PrimaryContainerDark = Color(0xFF2A2F3A)
private val OutlineDark = Color(0xFF4A505C)
private val OutlineVariantDark = Color(0xFF2B3039)
private val CoralDark = Color(0xFFFF6B7E)

val PulsarLightColors: ColorScheme = lightColorScheme(
    primary = Graphite,
    onPrimary = Color.White,
    primaryContainer = Silver,
    onPrimaryContainer = Graphite,
    secondary = Graphite,
    onSecondary = Color.White,
    secondaryContainer = Silver,
    onSecondaryContainer = Graphite,
    tertiary = Graphite,
    onTertiary = Color.White,
    tertiaryContainer = Silver,
    onTertiaryContainer = Graphite,
    background = Paper,
    onBackground = Graphite,
    surface = Paper,
    onSurface = Graphite,
    surfaceVariant = Fog,
    onSurfaceVariant = Slate,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Paper,
    surfaceContainer = Haze,
    surfaceContainerHigh = Fog,
    surfaceContainerHighest = Silver,
    inverseSurface = Graphite,
    inverseOnSurface = Paper,
    inversePrimary = Frost,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
    error = Coral,
    onError = Color.White,
)

val PulsarDarkColors: ColorScheme = darkColorScheme(
    primary = Frost,
    onPrimary = Graphite,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = Frost,
    secondary = Color(0xFFC5C9D2),
    onSecondary = Graphite,
    secondaryContainer = PrimaryContainerDark,
    onSecondaryContainer = Frost,
    tertiary = Color(0xFFC5C9D2),
    onTertiary = Graphite,
    tertiaryContainer = PrimaryContainerDark,
    onTertiaryContainer = Frost,
    background = Ink,
    onBackground = InkText,
    surface = Ink,
    onSurface = InkText,
    surfaceVariant = InkVariant,
    onSurfaceVariant = InkTextVariant,
    surfaceContainerLowest = InkLowest,
    surfaceContainerLow = InkLow,
    surfaceContainer = InkContainer,    // thanh điều hướng
    surfaceContainerHigh = InkHigh,     // thẻ
    surfaceContainerHighest = InkVariant,
    inverseSurface = InkText,
    inverseOnSurface = Ink,
    inversePrimary = Graphite,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    error = CoralDark,
    onError = Color(0xFF2A0A10),
)

// Màu riêng của thị trường chứng khoán VN (Material không có sẵn)
@Immutable
data class MarketColors(
    // Màu tô: nến, thanh breadth, heatmap, số lớn (từ 24sp)
    val up: Color, val down: Color, val reference: Color, val ceiling: Color, val floor: Color,
    // Màu chữ: chữ nhỏ và chip trên nền sáng, đạt 4,5:1
    val upText: Color, val downText: Color, val referenceText: Color,
    val ceilingText: Color, val floorText: Color,
    // Nút Mua / Bán
    val buy: Color, val onBuy: Color, val sell: Color, val onSell: Color,
)

val LightMarketColors = MarketColors(
    up = Color(0xFF16A34A), down = Color(0xFFE5484D), reference = Color(0xFFE5A100),
    ceiling = Color(0xFFA855F7), floor = Color(0xFF06B6D4),
    upText = Color(0xFF15803D), downText = Color(0xFFC62828), referenceText = Color(0xFFA16207),
    ceilingText = Color(0xFF7E22CE), floorText = Color(0xFF0E7490),
    buy = Color(0xFF15803D), onBuy = Color.White,
    sell = Color(0xFFC62828), onSell = Color.White,
)

val DarkMarketColors = MarketColors(
    up = Color(0xFF22C55E), down = CoralDark, reference = Color(0xFFFFC83D),
    ceiling = Color(0xFFD946EF), floor = Color(0xFF22D3EE),
    // Nền tối đã đủ tương phản nên bản chữ trùng bản tô
    upText = Color(0xFF22C55E), downText = CoralDark, referenceText = Color(0xFFFFC83D),
    ceilingText = Color(0xFFD946EF), floorText = Color(0xFF22D3EE),
    buy = Color(0xFF22C55E), onBuy = Color(0xFF04261B),
    sell = CoralDark, onSell = Color(0xFF2A0A10),
)

// Nền nhạt và nền heatmap: màu thị trường pha độ trong suốt, không phải màu riêng
fun Color.tint(): Color = copy(alpha = 0.08f)

object HeatAlpha {
    const val Weak = 0.12f
    const val Medium = 0.24f
    const val Strong = 0.40f
    const val Flat = 0.16f
}