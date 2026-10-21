package com.hmst.pulsar.feature.portfolio.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hmst.pulsar.feature.portfolio.ui.PortfolioScreen
import kotlinx.serialization.Serializable

@Serializable
data object PortfolioRoute

fun NavController.navigateToPortfolio() = navigate(PortfolioRoute)

fun NavGraphBuilder.portfolioScreen() {
    composable<PortfolioRoute> {
        PortfolioScreen()
    }
}
