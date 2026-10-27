package com.hmst.pulsar.feature.radar.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hmst.pulsar.core.network.model.Candle
import com.hmst.pulsar.core.network.model.EventId
import com.hmst.pulsar.core.network.model.MarketIndex
import com.hmst.pulsar.core.network.model.TradingSession
import com.hmst.pulsar.core.network.model.TradingSessionId
import com.hmst.pulsar.core.network.usecase.ObserveIndexCandlesUseCase
import com.hmst.pulsar.core.network.usecase.ObserveIndexInfluencesUseCase
import com.hmst.pulsar.core.network.usecase.ObserveIndexQuotesUseCase
import com.hmst.pulsar.core.network.usecase.ObserveQuotesUseCase
import com.hmst.pulsar.core.network.usecase.ObserveStreamStatusUseCase
import com.hmst.pulsar.core.network.usecase.ObserveTradingSessionUseCase
import com.hmst.pulsar.core.network.usecase.StartQuoteStreamUseCase
import com.hmst.pulsar.feature.radar.domain.model.MarketIndexEnum
import com.hmst.pulsar.feature.radar.domain.model.TradingSessionEnum
import com.hmst.pulsar.feature.radar.domain.repository.TradeAmountHistoryRepository
import com.hmst.pulsar.feature.radar.domain.usecase.BuildMarketCommentaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class RadarViewModel @Inject constructor(
    observeQuotesUC: ObserveQuotesUseCase,
    observeStreamStatusUC: ObserveStreamStatusUseCase,
    observeIndexQuotesUC: ObserveIndexQuotesUseCase,
    observeIndexCandlesUC: ObserveIndexCandlesUseCase,
    observeTradingSessionUC: ObserveTradingSessionUseCase,
    observeIndexInfluencesUC: ObserveIndexInfluencesUseCase,
    startStreamUC: StartQuoteStreamUseCase,
    private val buildCommentaryUC: BuildMarketCommentaryUseCase,
    private val tradeAmountHistory: TradeAmountHistoryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RadarUiState())
    val uiState: StateFlow<RadarUiState> = _uiState.asStateFlow()

    private var history: Map<LocalDate, Double> = emptyMap()

    init {
        viewModelScope.launch {
            tradeAmountHistory.observe().collect {
                history = it
                refreshCommentary()
            }
        }
        // câu nhận xét chỉ phụ thuộc phiên, VN-Index và ngày của nến cuối
        viewModelScope.launch {
            _uiState
                .map { Triple(it.tradingSession, it.indices[VNINDEX], tradingDate(it)) }
                .distinctUntilChanged()
                .collect { refreshCommentary() }
        }
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
        viewModelScope.launch {
            observeIndexQuotesUC().collect { indices ->
                _uiState.update { it.copy(indices = indices) }
            }
        }
        viewModelScope.launch {
            combine(
                observeIndexCandlesUC(),
                _uiState.map { it.tradingSession }.distinctUntilChanged(),
                ::todayCandles,
            ).collect { candles ->
                _uiState.update { it.copy(candles = candles) }
            }
        }
        viewModelScope.launch {
            observeIndexInfluencesUC().collect { indexInfluences ->
                _uiState.update { it.copy(influences = indexInfluences) }
            }
        }
        startStreamUC.invoke()
    }

    private fun refreshCommentary() {
        val state = _uiState.value
        val vnIndex = state.indices[VNINDEX]
        val date = tradingDate(state)
        saveFinalTradeAmount(state.tradingSession, vnIndex, date)
        // phiên đang xem không tính vào trung bình của chính nó
        val previous = history.filterKeys { date == null || it < date }
            .toSortedMap().values.toList().takeLast(AVG_SESSIONS)
        val avg = if (previous.size >= AVG_SESSIONS) previous.average() else null
        val commentary = buildCommentaryUC(state.tradingSession, vnIndex, avg)
        _uiState.update { it.copy(commentary = commentary) }
    }

    /**
     * Chỉ lưu giá trị đã chốt: đã đóng cửa, hoặc dữ liệu đang hiện thuộc ngày trước hôm nay
     * (mở app sáng hôm sau/cuối tuần). Lưu theo ngày của dữ liệu, không theo ngày mở app.
     */
    private fun saveFinalTradeAmount(
        session: TradingSessionEnum,
        vnIndex: MarketIndex?,
        date: LocalDate?,
    ) {
        if (vnIndex == null || date == null || vnIndex.grossTradeAmount <= 0) return
        val isFinal = session == TradingSessionEnum.Closed || date < LocalDate.now(VN_ZONE)
        if (!isFinal || history[date] == vnIndex.grossTradeAmount) return
        viewModelScope.launch { tradeAmountHistory.save(date, vnIndex.grossTradeAmount) }
    }

    /**
     * Đầu phiên REST chưa có nến hôm nay nên lùi về hôm qua, tới khi socket đẩy nến đầu tiên.
     * Phiên hôm nay đã mở thì bỏ nến ngày cũ để chart vẽ đường ngang tham chiếu.
     */
    private fun todayCandles(
        candles: Map<String, List<Candle>>,
        session: TradingSessionEnum,
    ): Map<String, List<Candle>> {
        // ATO chưa khớp liên tục, luôn để chart là đường ngang tham chiếu
        if (session == TradingSessionEnum.ATO) return emptyMap()
        if (session !in OPEN_SESSIONS) return candles
        val today = LocalDate.now(VN_ZONE)
        return candles.filterValues { list ->
            list.lastOrNull()?.let { Instant.ofEpochSecond(it.time).atZone(VN_ZONE).toLocalDate() } == today
        }
    }

    /** Ngày giao dịch của dữ liệu đang hiện, lấy theo nến cuối của VN-Index */
    private fun tradingDate(state: RadarUiState): LocalDate? =
        state.candles[VNINDEX]?.lastOrNull()?.let {
            Instant.ofEpochSecond(it.time).atZone(VN_ZONE).toLocalDate()
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

    private companion object {
        /** Câu nhận xét chỉ xét VN-Index */
        val VNINDEX = MarketIndexEnum.VNINDEX.name
        val VN_ZONE: ZoneId = ZoneId.of("Asia/Ho_Chi_Minh")
        const val AVG_SESSIONS = 20
        val OPEN_SESSIONS = setOf(
            TradingSessionEnum.ATO,
            TradingSessionEnum.Continuous,
            TradingSessionEnum.LunchBreak,
            TradingSessionEnum.ATC,
        )
    }
}
