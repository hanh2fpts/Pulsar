package com.hmst.pulsar.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.hmst.pulsar.feature.moneyflow.navigation.MoneyFlowRoute
import com.hmst.pulsar.feature.moneyflow.navigation.moneyFlowScreen
import com.hmst.pulsar.feature.portfolio.navigation.PortfolioRoute
import com.hmst.pulsar.feature.portfolio.navigation.portfolioScreen
import com.hmst.pulsar.feature.priceboard.navigation.PriceBoardRoute
import com.hmst.pulsar.feature.priceboard.navigation.navigateToPriceBoard
import com.hmst.pulsar.feature.priceboard.navigation.priceBoardScreen
import com.hmst.pulsar.feature.radar.navigation.RadarRoute
import com.hmst.pulsar.feature.radar.navigation.radarScreen
import com.hmst.pulsar.feature.signal.navigation.SignalRoute
import com.hmst.pulsar.feature.signal.navigation.signalScreen
import kotlinx.serialization.Serializable

/**
 * Root NavHost: the only place that knows every feature.
 * Features expose a graph builder + navigateToX(); this file wires them together.
 */
@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = RadarGraph,
        modifier = modifier,
    ) {
        navigation<RadarGraph>(startDestination = RadarRoute) {
            radarScreen()
        }
        navigation<PriceBoardGraph>(startDestination = PriceBoardRoute){
            priceBoardScreen()
        }
        navigation<MoneyFlowGraph>(startDestination = MoneyFlowRoute) {
            moneyFlowScreen()
        }
        navigation<PortfolioGraph>(startDestination = PortfolioRoute) {
            portfolioScreen()
        }
        navigation<SignalGraph>(startDestination = SignalRoute) {
            signalScreen()
        }
    }
}

/** Bottom bar tab graphs: a screen pushed inside a graph keeps its tab selected */
@Serializable
data object RadarGraph

@Serializable
data object PriceBoardGraph
@Serializable
data object MoneyFlowGraph

@Serializable
data object PortfolioGraph

@Serializable
data object SignalGraph
