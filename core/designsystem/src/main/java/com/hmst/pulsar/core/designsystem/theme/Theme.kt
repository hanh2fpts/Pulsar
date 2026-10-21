package com.hmst.pulsar.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

val LocalMarketColors = staticCompositionLocalOf { LightMarketColors }

// Dùng: MaterialTheme.market.up
val MaterialTheme.market: MarketColors
    @Composable
    @ReadOnlyComposable
    get() = LocalMarketColors.current

object Spacing { val xs = 4.dp; val sm = 8.dp; val md = 12.dp; val lg = 16.dp; val xl = 24.dp; val xxl = 32.dp }

val PulsarShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),   // chip
    small = RoundedCornerShape(12.dp),       // ô nhập, ô thông số
    medium = RoundedCornerShape(16.dp),      // thẻ
    large = RoundedCornerShape(24.dp),       // sheet, nút pill
)

@Composable
fun PulsarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) PulsarDarkColors else PulsarLightColors
    val market = if (darkTheme) DarkMarketColors else LightMarketColors

    CompositionLocalProvider(LocalMarketColors provides market) {
        MaterialTheme(
            colorScheme = colors,
            typography = PulsarTypography,
            shapes = PulsarShapes,
            content = content,
        )
    }
}