package com.hmst.pulsar.core.network.model

/** Một cây nến OHLC; [time] là thời điểm bắt đầu nến, unix giây */
data class Candle(
    val time: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Long,
)
