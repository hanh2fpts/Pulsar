package com.hmst.pulsar.core.network.usecase

import com.hmst.pulsar.core.network.repository.PulsarRepository
import com.hmst.pulsar.core.network.model.Quote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/** Danh sách giá realtime, sắp theo mã */
class ObserveQuotesUseCase @Inject constructor(
    private val repository: PulsarRepository,
) {
    operator fun invoke(): Flow<List<Quote>> =
        repository.quotes.map { it.values.sortedBy(Quote::symbol) }
}
