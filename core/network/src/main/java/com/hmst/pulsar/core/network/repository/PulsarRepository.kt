package com.hmst.pulsar.core.network.repository

import com.hmst.pulsar.core.network.model.Quote
import kotlinx.coroutines.flow.StateFlow

/** Luồng giá realtime. Feature và usecase chỉ biết interface này, không biết nguồn dữ liệu */
interface PulsarRepository {
    /** Giá mới nhất theo mã */
    val quotes: StateFlow<Map<String, Quote>>

    /** Trạng thái kết nối */
    val status: StateFlow<String>

    /** Gọi nhiều lần vẫn chỉ mở 1 kết nối */
    fun start(symbols: List<String>)

    fun stop()
}
