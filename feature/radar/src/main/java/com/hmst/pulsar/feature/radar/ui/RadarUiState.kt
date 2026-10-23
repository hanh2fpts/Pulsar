package com.hmst.pulsar.feature.radar.ui

data class RadarUiState(
    val status: String = "",
    /** Tên phiên giao dịch hiện tại, rỗng khi chưa nhận được */
    val tradingSession: String = "",
    val content: Content = Content.Loading,
) {
    sealed interface Content {
        data object Loading : Content
        data object Success : Content
        data class Error(val message: String) : Content
    }
}
