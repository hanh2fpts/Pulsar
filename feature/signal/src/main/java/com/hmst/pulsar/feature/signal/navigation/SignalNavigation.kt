package com.hmst.pulsar.feature.signal.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hmst.pulsar.feature.signal.ui.SignalScreen
import kotlinx.serialization.Serializable

@Serializable
data object SignalRoute

fun NavController.navigateToSignal() = navigate(SignalRoute)

fun NavGraphBuilder.signalScreen() {
    composable<SignalRoute> {
        SignalScreen()
    }
}
