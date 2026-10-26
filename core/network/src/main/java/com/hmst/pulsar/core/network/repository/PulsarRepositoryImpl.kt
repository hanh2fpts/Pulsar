package com.hmst.pulsar.core.network.repository

import com.hmst.pulsar.core.network.dnse.datasource.DnseDataSource
import com.hmst.pulsar.core.network.model.Candle
import com.hmst.pulsar.core.network.model.IndexInfluence
import com.hmst.pulsar.core.network.model.IndexQuotes
import com.hmst.pulsar.core.network.model.PriceQuotes
import com.hmst.pulsar.core.network.model.TradingSession
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

/** Chỉ chuyển tiếp từ [DnseDataSource]; việc xử lý socket nằm ở data source */
@Singleton
class PulsarRepositoryImpl @Inject constructor(
    private val dataSource: DnseDataSource,
) : PulsarRepository {
    override val quotes: StateFlow<Map<String, PriceQuotes>> = dataSource.quotes
    override val indexQuotes: StateFlow<Map<String, IndexQuotes>> = dataSource.indexQuotes
    override val indexCandles: StateFlow<Map<String, List<Candle>>> = dataSource.indexCandles
    override val status: StateFlow<String> = dataSource.status
    override val tradingSession: StateFlow<TradingSession?> = dataSource.tradingSession
    override val indexInfluences: StateFlow<Map<String, List<IndexInfluence>>> = dataSource.indexInfluences

    override fun start() = dataSource.start()

    override fun stop() = dataSource.stop()
}
