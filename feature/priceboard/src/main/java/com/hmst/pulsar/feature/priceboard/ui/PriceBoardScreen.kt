package com.hmst.pulsar.feature.priceboard.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hmst.pulsar.core.designsystem.theme.PulsarTheme
import com.hmst.pulsar.feature.priceboard.R
import com.hmst.pulsar.feature.priceboard.ui.PriceBoardUiState.Content

/** Stateful entry point used by navigation */
@Composable
fun PriceBoardScreen(
    modifier: Modifier = Modifier,
    viewModel: PriceBoardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PriceBoardContent(
        uiState = uiState,
        modifier = modifier,
    )
}

/** Stateless UI: previewable without Hilt or network */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceBoardContent(
    uiState: PriceBoardUiState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.priceboard_title)) },
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize(), contentAlignment = Alignment.Center) {
            when (val content = uiState.content) {
                Content.Loading -> CircularProgressIndicator()
                Content.Success -> Unit
                is Content.Error -> Text(content.message, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Preview
@Composable
private fun PriceBoardContentPreview() {
    PulsarTheme() {
        PriceBoardContent(uiState = PriceBoardUiState())
    }
}
