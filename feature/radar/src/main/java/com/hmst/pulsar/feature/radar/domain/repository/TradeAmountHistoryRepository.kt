package com.hmst.pulsar.feature.radar.domain.repository

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/** Giá trị giao dịch cuối ngày của VN-Index do app tự lưu, để tính trung bình 20 phiên */
interface TradeAmountHistoryRepository {
    /** key = ngày giao dịch */
    fun observe(): Flow<Map<LocalDate, Double>>

    /** Ghi đè nếu ngày đó đã có */
    suspend fun save(date: LocalDate, amount: Double)
}
