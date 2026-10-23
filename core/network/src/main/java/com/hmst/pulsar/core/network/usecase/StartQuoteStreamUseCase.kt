package com.hmst.pulsar.core.network.usecase

import com.hmst.pulsar.core.network.repository.PulsarRepository
import javax.inject.Inject

/** Mở kết nối */
class StartQuoteStreamUseCase @Inject constructor(
    private val repository: PulsarRepository,
) {
    operator fun invoke() = repository.start()
}
