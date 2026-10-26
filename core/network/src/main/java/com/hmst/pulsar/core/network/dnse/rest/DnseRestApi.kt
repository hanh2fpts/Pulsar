package com.hmst.pulsar.core.network.dnse.rest

import com.hmst.pulsar.core.network.dnse.dto.MarketIndexResponse
import com.hmst.pulsar.core.network.dnse.dto.OHLCDto
import com.hmst.pulsar.core.network.dnse.dto.TradingSessionResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DnseRestApi {
    @GET("market/trading-session")
    suspend fun tradingSession(
        @Query("tscProdGrpId") tscProdGrpId: String,
        @Query("boardId") boardId: String,
    ): TradingSessionResponse

    /** Doc ghi from/to không bắt buộc nhưng server trả 400 nếu thiếu from.
     * Đơn vị giây, to - from không vượt quá 1 ngày */
    @GET("price/{indexName}/market-index")
    suspend fun getMarketIndex(
        @Path("indexName") indexName: String,
        @Query("from") from: Long,
        @Query("to") to: Long,
    ): MarketIndexResponse

    @GET("price/ohlc")
    suspend fun getOhlc(
        @Query("symbol") symbol: String,
        @Query("type") type: String,
        @Query("resolution") resolution: String,
        @Query("from") from: Long,
        @Query("to") to: Long,
    ): OHLCDto
}