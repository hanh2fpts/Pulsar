package com.hmst.pulsar.feature.market.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hmst.pulsar.core.designsystem.theme.PulsarTheme
import com.hmst.pulsar.feature.market.ui.MarketUiState.Content
import com.hmst.pulsar.feature.market.ui.component.IndexCompose
import kotlin.collections.listOf

/** Stateful entry point used by navigation */
@Composable
fun MarketScreen(
    onPriceBoardClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MarketViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MarketContent(
        uiState = uiState,
        onPriceBoardClick = onPriceBoardClick,
        modifier = modifier,
    )
}

/** Stateless UI: previewable without Hilt or network */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketContent(
    uiState: MarketUiState,
    onPriceBoardClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Pulsar") },
                actions = {
                    Icon(Icons.Filled.AccountCircle, contentDescription = "user_setting")
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LazyRow(
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
               items(listOf("a", "b", "c")){
                   IndexCompose(
                       Modifier
                           .height(80.dp)
                           .width(150.dp)
                   )
               }
            }
        }
    }
}

@Preview
@Composable
private fun MarketContentPreview() {
    PulsarTheme() {
        MarketContent(uiState = MarketUiState(), onPriceBoardClick = {})
    }
}
