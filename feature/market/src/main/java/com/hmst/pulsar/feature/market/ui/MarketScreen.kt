package com.hmst.pulsar.feature.market.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
        topBar = { TopAppBar(title = { Text("Market") }) },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (val content = uiState.content) {
                Content.Loading -> CircularProgressIndicator()
                Content.Success -> Unit
                is Content.Error -> Text(content.message, color = MaterialTheme.colorScheme.error)
            }
            Button(onClick = onPriceBoardClick) { Text("Bảng giá") }
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
