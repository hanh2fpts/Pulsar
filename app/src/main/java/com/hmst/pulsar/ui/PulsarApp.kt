package com.hmst.pulsar.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.hmst.pulsar.navigation.AppNavHost

/** App root: every screen draws its own Scaffold, so the root only hosts navigation */
@Composable
fun PulsarApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    AppNavHost(navController = navController, modifier = modifier)
}
