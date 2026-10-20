package com.hmst.pulsar.feature.market.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hmst.pulsar.core.designsystem.theme.PulsarTheme
import com.hmst.pulsar.feature.market.R
import com.hmst.pulsar.feature.market.domain.model.IndexEnum
import com.hmst.pulsar.feature.market.ui.component.MarketIndexCard

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
                title = { Text(stringResource(R.string.market_title)) },
                actions = {
                    Icon(Icons.Filled.AccountCircle, contentDescription = "user_setting")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LazyRow(
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(IndexEnum.entries) {
                    MarketIndexCard(
                        Modifier
                            .height(100.dp)
                            .width(150.dp),
                        it
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
