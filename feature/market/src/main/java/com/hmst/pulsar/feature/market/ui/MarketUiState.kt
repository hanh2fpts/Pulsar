package com.hmst.pulsar.feature.market.ui

data class MarketUiState(
    val content: Content = Content.Loading,
) {
    sealed interface Content {
        data object Loading : Content
        data object Success : Content
        data class Error(val message: String) : Content
    }
}
