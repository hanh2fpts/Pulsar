package com.hmst.pulsar.feature.moneyflow.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hmst.pulsar.feature.moneyflow.ui.MoneyFlowScreen
import kotlinx.serialization.Serializable

@Serializable
data object MoneyFlowRoute

fun NavController.navigateToMoneyFlow() = navigate(MoneyFlowRoute)

fun NavGraphBuilder.moneyFlowScreen() {
    composable<MoneyFlowRoute> {
        MoneyFlowScreen()
    }
}
