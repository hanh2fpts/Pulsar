package com.hmst.pulsar.core.network.usecase

import com.hmst.pulsar.core.network.mapper.toDomain
import com.hmst.pulsar.core.network.model.MarketIndex
import com.hmst.pulsar.core.network.repository.PulsarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/* Danh sach index realtime */
class ObserveIndexQuotesUseCase @Inject constructor(private val repo: PulsarRepository) {
    operator fun invoke(): Flow<List<MarketIndex>> = repo.indexQuotes.map { it.values.map { q -> q.toDomain() } }
}
