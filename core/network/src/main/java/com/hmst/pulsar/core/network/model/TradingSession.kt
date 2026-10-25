package com.hmst.pulsar.core.network.model

data class TradingSession (
    val tradingSessionId: String?,
    /** Sự kiện chuyển trạng thái gần nhất, ví dụ AW8 = nghỉ trưa */
    val eventId: String? = null,
)
