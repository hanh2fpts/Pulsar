package com.hmst.pulsar.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.hmst.pulsar.feature.market.navigation.MarketRoute
import com.hmst.pulsar.feature.market.navigation.marketScreen
import com.hmst.pulsar.feature.priceboard.navigation.navigateToPriceBoard
import com.hmst.pulsar.feature.priceboard.navigation.priceBoardScreen
import com.hmst.pulsar.feature.signal.navigation.SignalRoute
import com.hmst.pulsar.feature.signal.navigation.signalScreen
import kotlinx.serialization.Serializable

/**
 * Root NavHost: the only place that knows every feature.
 * Features expose a graph builder + navigateToX(); this file wires them together.
 */
@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    val onBack: () -> Unit = { navController.popBackStack() }

    NavHost(
        navController = navController,
        startDestination = MarketGraph,
        modifier = modifier,
    ) {
        navigation<MarketGraph>(startDestination = MarketRoute) {
            marketScreen(onPriceBoardClick = navController::navigateToPriceBoard)
            priceBoardScreen(onBack = onBack)
        }
        navigation<SignalGraph>(startDestination = SignalRoute) {
            signalScreen()
        }
    }
}

/** Bottom bar tab graphs: a screen pushed inside a graph keeps its tab selected */
@Serializable
data object MarketGraph

@Serializable
data object SignalGraph
