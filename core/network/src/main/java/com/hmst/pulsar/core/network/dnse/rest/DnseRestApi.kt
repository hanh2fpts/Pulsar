package com.hmst.pulsar.core.network.dnse.rest

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface DnseRestApi {
    @GET("market/trading-session")
    suspend fun tradingSession(
        @Query("tscProdGrpId") tscProdGrpId: String,
        @Query("boardId") boardId: String,
    ): TradingSessionResponse
}

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