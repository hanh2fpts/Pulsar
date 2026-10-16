package com.hmst.pulsar.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.hmst.pulsar.feature.market.navigation.MarketRoute
import com.hmst.pulsar.feature.market.navigation.marketScreen
import com.hmst.pulsar.feature.priceboard.navigation.navigateToPriceBoard
import com.hmst.pulsar.feature.priceboard.navigation.priceBoardScreen

/**
 * Root NavHost: the only place that knows every feature.
 * Features expose a graph builder + navigateToX(); this file wires them together.
 */
@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    val onBack: () -> Unit = { navController.popBackStack() }

    NavHost(
        navController = navController,
        startDestination = MarketRoute,
        modifier = modifier,
    ) {
        marketScreen(onPriceBoardClick = navController::navigateToPriceBoard)
        priceBoardScreen(onBack = onBack)
    }
}
