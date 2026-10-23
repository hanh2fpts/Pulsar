package com.hmst.pulsar.feature.radar.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmst.pulsar.core.network.model.TradingSessionId
import com.hmst.pulsar.core.network.usecase.ObserveIndexQuotesUseCase
import com.hmst.pulsar.core.network.usecase.ObserveQuotesUseCase
import com.hmst.pulsar.core.network.usecase.ObserveStreamStatusUseCase
import com.hmst.pulsar.core.network.usecase.ObserveTradingSessionUseCase
import com.hmst.pulsar.core.network.usecase.StartQuoteStreamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RadarViewModel @Inject constructor(
    observeQuotesUC: ObserveQuotesUseCase,
    observeStreamStatusUC: ObserveStreamStatusUseCase,
    observeIndexQuotesUC: ObserveIndexQuotesUseCase,
    observeTradingSessionUC: ObserveTradingSessionUseCase,
    startStreamUC: StartQuoteStreamUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RadarUiState())
    val uiState: StateFlow<RadarUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeStreamStatusUC().collect { status ->
                _uiState.update { it.copy(status = status) }
            }
        }
        viewModelScope.launch {
            observeTradingSessionUC().collect { session ->
                val code = session?.tradingSessionId ?: return@collect
                // mã chưa có trong enum thì hiện nguyên mã để còn biết mà bổ sung
                val name = TradingSessionId.entries.firstOrNull { it.code == code }?.description ?: code
                _uiState.update { it.copy(tradingSession = name) }
            }
        }
        startStreamUC.invoke()
    }
}
