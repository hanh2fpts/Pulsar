package com.hmst.pulsar.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CandlestickChart
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.WaterfallChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hmst.pulsar.R
import com.hmst.pulsar.core.designsystem.theme.PulsarTheme
import com.hmst.pulsar.navigation.AppNavHost
import com.hmst.pulsar.navigation.MoneyFlowGraph
import com.hmst.pulsar.navigation.PortfolioGraph
import com.hmst.pulsar.navigation.PriceBoardGraph
import com.hmst.pulsar.navigation.RadarGraph
import com.hmst.pulsar.navigation.SignalGraph

/** Bottom bar tabs */
enum class Destination(@field:StringRes val labelRes: Int, val icon: ImageVector, val route: Any) {
    RADAR(R.string.radar_nav_tab_title, Icons.Filled.TrackChanges, RadarGraph),
    PRICE_BOARD(R.string.priceboard_nav_tab_title, Icons.Filled.CandlestickChart, PriceBoardGraph),
    MONEY_FLOW(R.string.money_flow_nav_tab_title, Icons.Filled.WaterfallChart, MoneyFlowGraph),
    SIGNAL(R.string.signal_nav_tab_title, Icons.Filled.Insights, SignalGraph),
    PORTFOLIO(R.string.portfolio_nav_tab_title, Icons.Filled.PieChart, PortfolioGraph),
}

/** App root: owns the bottom bar and hosts navigation */
@Composable
fun PulsarApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val selected = Destination.entries.firstOrNull { d ->
        backStackEntry?.destination?.hierarchy?.any { it.hasRoute(d.route::class) } == true
    } ?: Destination.RADAR
    PulsarAppContent(
        selected = selected,
        onDestinationClick = {
            navController.navigate(it.route) {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true   // bấm lại tab đang mở thì không tạo màn mới
                restoreState = true      // quay lại tab thì giữ nguyên scroll/state cũ
            }
        },
        modifier = Modifier.fillMaxSize(),
    ) { contentModifier ->
        AppNavHost(navController = navController, modifier = contentModifier)
    }
}

/** Stateless shell: previewable without Hilt (screens use hiltViewModel()) */
@Composable
fun PulsarAppContent(
    selected: Destination,
    onDestinationClick: (Destination) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                Destination.entries.forEach { destination ->
                    NavigationBarItem(
                        selected = destination == selected,
                        onClick = { onDestinationClick(destination) },
                        icon = { Icon(destination.icon, contentDescription = null) },
                        label = { Text(stringResource(destination.labelRes)) },
                    )
                }
            }
        },
    ) { innerPadding ->
        // Screens draw their own Scaffold: consume the insets so they aren't padded twice
        content(
            Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        )
    }
}

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PulsarAppContentPreview() {
    PulsarTheme {
        PulsarAppContent(
            selected = Destination.RADAR,
            onDestinationClick = {}) { contentModifier ->
            Box(contentModifier.fillMaxSize())
        }
    }
}
