package com.hmst.pulsar.core.network.usecase

import com.hmst.pulsar.core.network.repository.PulsarRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/** Trạng thái kết nối luồng giá */
class ObserveStreamStatusUseCase @Inject constructor(
    private val repository: PulsarRepository,
) {
    operator fun invoke(): StateFlow<String> = repository.status
}
