package com.hmst.pulsar.feature.priceboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hmst.pulsar.feature.priceboard.ui.PriceBoardScreen
import kotlinx.serialization.Serializable

@Serializable
data object PriceBoardRoute

fun NavController.navigateToPriceBoard() = navigate(PriceBoardRoute)

fun NavGraphBuilder.priceBoardScreen(onBack: () -> Unit) {
    composable<PriceBoardRoute> {
        PriceBoardScreen(onBack = onBack)
    }
}
