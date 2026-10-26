package com.hmst.pulsar.core.network.usecase

import com.hmst.pulsar.core.network.model.IndexInfluence
import com.hmst.pulsar.core.network.repository.PulsarRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ObserveIndexInfluencesUseCase @Inject constructor(private val repo: PulsarRepository) {
    operator fun invoke(): StateFlow<Map<String, List<IndexInfluence>>> = repo.indexInfluences
}