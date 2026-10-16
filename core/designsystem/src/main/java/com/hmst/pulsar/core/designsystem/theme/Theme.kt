package com.hmst.pulsar.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalMarketColors = staticCompositionLocalOf { LightMarketColors }

// Dùng: MaterialTheme.market.up
val MaterialTheme.market: MarketColors
    @Composable
    @ReadOnlyComposable
    get() = LocalMarketColors.current

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
            content = content,
        )
    }
}