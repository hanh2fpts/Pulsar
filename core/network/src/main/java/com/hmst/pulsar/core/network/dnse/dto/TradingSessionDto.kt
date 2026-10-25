package com.hmst.pulsar.core.network.dnse.dto

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class TradingSessionResponse(
    val tradingSessions: List<TradingSessionDto> = emptyList(),
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class TradingSessionDto(
    val marketId: String? = null,
    val boardId: String? = null,
    val tscProdGrpId: String? = null,
    val tradingSessionId: String? = null,
    val eventId: String? = null,
    val time: String? = null,
)