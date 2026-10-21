package com.hmst.pulsar.feature.radar.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hmst.pulsar.feature.radar.ui.RadarScreen
import kotlinx.serialization.Serializable

@Serializable
data object RadarRoute

fun NavController.navigateToRadar() = navigate(RadarRoute)

fun NavGraphBuilder.radarScreen(onPriceBoardClick: () -> Unit) {
    composable<RadarRoute> {
        RadarScreen(onPriceBoardClick = onPriceBoardClick)
    }
}
