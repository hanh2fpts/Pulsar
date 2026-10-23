package com.hmst.pulsar.core.network.usecase

import com.hmst.pulsar.core.network.repository.PulsarRepository
import javax.inject.Inject

/** Mở kết nối và đăng ký nhận giá các mã [symbols] */
class StartQuoteStreamUseCase @Inject constructor(
    private val repository: PulsarRepository,
) {
    operator fun invoke(symbols: List<String>) = repository.start(symbols)
}
