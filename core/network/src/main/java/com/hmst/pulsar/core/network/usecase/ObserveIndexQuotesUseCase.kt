package com.hmst.pulsar.core.network.usecase

import com.hmst.pulsar.core.network.mapper.toDomain
import com.hmst.pulsar.core.network.model.MarketIndex
import com.hmst.pulsar.core.network.repository.PulsarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/* Index realtime, key = indexName (VNINDEX, VN30...) */
class ObserveIndexQuotesUseCase @Inject constructor(private val repo: PulsarRepository) {
    operator fun invoke(): Flow<Map<String, MarketIndex>> =
        repo.indexQuotes.map { it.mapValues { (_, q) -> q.toDomain() } }
}
