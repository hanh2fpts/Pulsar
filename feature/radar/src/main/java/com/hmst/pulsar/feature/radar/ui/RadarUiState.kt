package com.hmst.pulsar.feature.radar.ui

import com.hmst.pulsar.feature.radar.domain.model.TradingSessionEnum

data class RadarUiState(
    val status: String = "",
    /** Tên phiên giao dịch hiện tại, rỗng khi chưa nhận được */
    val tradingSession: TradingSessionEnum = TradingSessionEnum.PreMarket,
    val content: Content = Content.Loading,
) {
    sealed interface Content {
        data object Loading : Content
        data object Success : Content
        data class Error(val message: String) : Content
    }
}
