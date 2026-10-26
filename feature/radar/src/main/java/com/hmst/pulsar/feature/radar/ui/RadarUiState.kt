package com.hmst.pulsar.feature.radar.ui

import com.hmst.pulsar.core.network.model.Candle
import com.hmst.pulsar.core.network.model.MarketIndex
import com.hmst.pulsar.feature.radar.domain.model.TradingSessionEnum

data class RadarUiState(
    val status: String = "",
    /** Tên phiên giao dịch hiện tại, rỗng khi chưa nhận được */
    val tradingSession: TradingSessionEnum = TradingSessionEnum.PreMarket,
    val content: Content = Content.Loading,
    /** key = indexName (VNINDEX, VN30...), trùng tên MarketIndexEnum; chỉ số đang chọn do pager giữ */
    val indices: Map<String, MarketIndex> = emptyMap(),
    /** Nến 5 phút cho chart, key giống [indices] */
    val candles: Map<String, List<Candle>> = emptyMap(),
) {
    sealed interface Content {
        data object Loading : Content
        data object Success : Content
        data class Error(val message: String) : Content
    }
}
