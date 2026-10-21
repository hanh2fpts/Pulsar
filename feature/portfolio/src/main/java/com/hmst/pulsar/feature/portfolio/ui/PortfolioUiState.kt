package com.hmst.pulsar.feature.portfolio.ui

data class PortfolioUiState(
    val content: Content = Content.Loading,
) {
    sealed interface Content {
        data object Loading : Content
        data object Success : Content
        data class Error(val message: String) : Content
    }
}
