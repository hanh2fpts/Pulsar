package com.hmst.pulsar.core.network.dnse.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmst.pulsar.core.network.model.PriceQuotes
import com.hmst.pulsar.core.network.usecase.ObserveQuotesUseCase
import com.hmst.pulsar.core.network.usecase.ObserveStreamStatusUseCase
import com.hmst.pulsar.core.network.usecase.StartQuoteStreamUseCase
import com.hmst.pulsar.core.network.usecase.StopQuoteStreamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PulsarViewModel @Inject constructor(
    observeQuotes: ObserveQuotesUseCase,
    observeStreamStatus: ObserveStreamStatusUseCase,
    startQuoteStream: StartQuoteStreamUseCase,
    private val stopQuoteStream: StopQuoteStreamUseCase,
) : ViewModel() {

    val status: StateFlow<String> = observeStreamStatus()
    val rows: StateFlow<List<PriceQuotes>> = observeQuotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        startQuoteStream()
    }

    override fun onCleared() = stopQuoteStream()
}
