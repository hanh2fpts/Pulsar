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
                val name = session?.let(::sessionName) ?: return@collect
                _uiState.update { it.copy(tradingSession = name) }
            }
        }
        startStreamUC.invoke()
    }

    /**
     * Ưu tiên eventId vì nó phân biệt được cả nghỉ trưa, trừ khi đã đóng bảng (99):
     * lúc đó eventId vẫn là sự kiện cuối ngày (vd AC2 "Thực hiện khớp lệnh định kỳ đóng cửa"), hiện ra sẽ sai.
     * Mã chưa có trong enum thì hiện nguyên mã để còn biết mà bổ sung.
     */
    private fun sessionName(session: TradingSession): String? {
        val sessionId = TradingSessionId.entries.firstOrNull { it.code == session.tradingSessionId }
        if (sessionId == TradingSessionId.CLOSED) return sessionId.description
        val event = EventId.entries.firstOrNull { it.name == session.eventId }
        return event?.description
            ?: sessionId?.description
            ?: session.eventId
            ?: session.tradingSessionId
    }
}
