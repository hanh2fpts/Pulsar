package com.hmst.pulsar.core.network.usecase

import com.hmst.pulsar.core.network.model.Candle
import com.hmst.pulsar.core.network.repository.PulsarRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/* Nến 5 phút của index, REST lịch sử + socket realtime, key = indexName */
class ObserveIndexCandlesUseCase @Inject constructor(private val repo: PulsarRepository) {
    operator fun invoke(): StateFlow<Map<String, List<Candle>>> = repo.indexCandles
}
