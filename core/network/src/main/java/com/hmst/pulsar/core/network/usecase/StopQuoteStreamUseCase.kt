package com.hmst.pulsar.core.network.usecase

import com.hmst.pulsar.core.network.repository.PulsarRepository
import javax.inject.Inject

/** Đóng kết nối luồng giá */
class StopQuoteStreamUseCase @Inject constructor(
    private val repository: PulsarRepository,
) {
    operator fun invoke() = repository.stop()
}
