package com.hmst.pulsar.feature.radar.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hmst.pulsar.feature.radar.domain.repository.TradeAmountHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/** Lưu dạng JSON {"2026-10-26": 18650.47, ...} trong DataStore chung, chỉ giữ [MAX_DAYS] ngày gần nhất */
@Singleton
class TradeAmountHistoryRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
) : TradeAmountHistoryRepository {

    override fun observe(): Flow<Map<LocalDate, Double>> =
        dataStore.data.map { decode(it[KEY]) }

    override suspend fun save(date: LocalDate, amount: Double) {
        dataStore.edit { prefs ->
            val updated = (decode(prefs[KEY]) + (date to amount))
                .toSortedMap().entries.toList().takeLast(MAX_DAYS)
                .associate { (d, v) -> d.toString() to v }
            prefs[KEY] = json.encodeToString(updated)
        }
    }

    private fun decode(raw: String?): Map<LocalDate, Double> {
        if (raw == null) return emptyMap()
        return runCatching {
            json.decodeFromString<Map<String, Double>>(raw)
                .mapKeys { (date, _) -> LocalDate.parse(date) }
        }.getOrElse {
            Log.e("Radar", "Đọc lịch sử giá trị GD lỗi", it)
            emptyMap()
        }
    }

    private companion object {
        val KEY = stringPreferencesKey("radar_vnindex_trade_amount_history")

        // dư so với 20 phiên để không thiếu khi có ngày bị bỏ sót
        const val MAX_DAYS = 30
    }
}
