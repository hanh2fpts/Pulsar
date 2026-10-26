package com.hmst.pulsar.core.network.repository

import com.hmst.pulsar.core.network.model.Candle
import com.hmst.pulsar.core.network.model.IndexInfluence
import com.hmst.pulsar.core.network.model.IndexQuotes
import com.hmst.pulsar.core.network.model.PriceQuotes
import com.hmst.pulsar.core.network.model.TradingSession
import kotlinx.coroutines.flow.StateFlow

/** Luồng giá realtime. Feature và usecase chỉ biết interface này, không biết nguồn dữ liệu */
interface PulsarRepository {
    /** Giá mới nhất theo mã */
    val quotes: StateFlow<Map<String, PriceQuotes>>

    val indexQuotes: StateFlow<Map<String, IndexQuotes>>

    /** Nến 5 phút trong ngày giao dịch gần nhất, key = indexName */
    val indexCandles: StateFlow<Map<String, List<Candle>>>

    /** Trạng thái kết nối */
    val status: StateFlow<String>

    /** Phiên giao dịch hiện tại, null khi chưa nhận được */
    val tradingSession: StateFlow<TradingSession?>

    /** anh sách top cổ phiếu ảnh hưởng chỉ số **/
    val indexInfluences: StateFlow<Map<String, List<IndexInfluence>>>

    /** Gọi nhiều lần vẫn chỉ mở 1 kết nối */
    fun start()

    fun stop()
}
