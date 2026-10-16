package com.hmst.pulsar.feature.market.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hmst.pulsar.feature.market.ui.MarketScreen
import kotlinx.serialization.Serializable

@Serializable
data object MarketRoute

fun NavController.navigateToMarket() = navigate(MarketRoute)

fun NavGraphBuilder.marketScreen(onPriceBoardClick: () -> Unit) {
    composable<MarketRoute> {
        MarketScreen(onPriceBoardClick = onPriceBoardClick)
    }
}
