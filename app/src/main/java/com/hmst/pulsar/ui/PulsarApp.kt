package com.hmst.pulsar.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.hmst.pulsar.R
import com.hmst.pulsar.navigation.AppNavHost

/** App root: every screen draws its own Scaffold, so the root only hosts navigation */
@Composable
fun PulsarApp() {
    val navController = rememberNavController()
    PulsarAppContent(Modifier.fillMaxSize(), navController)
}

@Composable
fun PulsarAppContent(
    modifier: Modifier = Modifier,
    controller: NavHostController = rememberNavController(),
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                NavigationBarItem(
                    label = { Text(stringResource(R.string.market_nav_tab_title)) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "market") },
                    onClick = {},
                    selected = true,
                )
                NavigationBarItem(
                    label = { Text(stringResource(R.string.filter_nav_tab_title)) },
                    icon = { Icon(Icons.Default.Star, contentDescription = "filter") },
                    onClick = {},
                    selected = false,
                )

            }
        }
    ) { innerPadding ->
        AppNavHost(navController = controller, modifier = modifier.padding(innerPadding))
    }
}

@Preview
@Composable
fun PulsarAppContentPreview() {
    PulsarTheme {
        PulsarAppContent()
    }
}
