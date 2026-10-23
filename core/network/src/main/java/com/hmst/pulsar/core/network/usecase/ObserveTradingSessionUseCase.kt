package com.hmst.pulsar.core.network.usecase

import com.hmst.pulsar.core.network.model.TradingSession
import com.hmst.pulsar.core.network.repository.PulsarRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/** Phiên giao dịch hiện tại của thị trường */
class ObserveTradingSessionUseCase @Inject constructor(
    private val repository: PulsarRepository,
) {
    operator fun invoke(): StateFlow<TradingSession?> = repository.tradingSession
}
