package com.hmst.pulsar.core.network.repository

import com.hmst.pulsar.core.network.dnse.datasource.DnseDataSource
import com.hmst.pulsar.core.network.model.IndexQuotes
import com.hmst.pulsar.core.network.model.PriceQuotes
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
    override val status: StateFlow<String> = dataSource.status

    override fun start(symbols: List<String>) = dataSource.start(symbols)

    override fun stop() = dataSource.stop()
}
