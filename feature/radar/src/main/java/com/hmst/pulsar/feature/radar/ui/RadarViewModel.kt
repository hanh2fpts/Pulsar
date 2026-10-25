package com.hmst.pulsar.feature.radar.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmst.pulsar.core.network.model.EventId
import com.hmst.pulsar.core.network.model.TradingSession
import com.hmst.pulsar.core.network.model.TradingSessionId
import com.hmst.pulsar.core.network.usecase.ObserveIndexQuotesUseCase
import com.hmst.pulsar.core.network.usecase.ObserveQuotesUseCase
import com.hmst.pulsar.core.network.usecase.ObserveStreamStatusUseCase
import com.hmst.pulsar.core.network.usecase.ObserveTradingSessionUseCase
import com.hmst.pulsar.core.network.usecase.StartQuoteStreamUseCase
import com.hmst.pulsar.feature.radar.domain.model.TradingSessionEnum
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
                val tradingSession = session?.let(::toTradingSession) ?: return@collect
                _uiState.update { it.copy(tradingSession = tradingSession) }
            }
        }
        startStreamUC.invoke()
    }

    /**
     * Nghỉ trưa chỉ nhận biết được qua eventId (AW8), lúc đó tradingSessionId vẫn là 40.
     * Còn lại lấy theo tradingSessionId; chưa có/không xác định coi là chờ mở cửa.
     */
    private fun toTradingSession(session: TradingSession): TradingSessionEnum {
        if (session.eventId == EventId.AW8.name) return TradingSessionEnum.LunchBreak
        val sessionId = TradingSessionId.entries.firstOrNull { it.code == session.tradingSessionId }
        return when (sessionId) {
            TradingSessionId.ATO -> TradingSessionEnum.ATO
            TradingSessionId.CONTINUOUS -> TradingSessionEnum.Continuous
            TradingSessionId.ATC -> TradingSessionEnum.ATC
            TradingSessionId.CLOSED, TradingSessionId.PCA_HALT -> TradingSessionEnum.Closed
            TradingSessionId.UNSPECIFIED, null -> TradingSessionEnum.PreMarket
        }
    }
}
