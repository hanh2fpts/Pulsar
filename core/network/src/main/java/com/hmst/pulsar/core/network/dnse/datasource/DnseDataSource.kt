package com.hmst.pulsar.core.network.dnse.datasource

import android.util.Log
import com.hmst.pulsar.core.network.BuildConfig
import com.hmst.pulsar.core.network.dnse.protocol.DnseAuth
import com.hmst.pulsar.core.network.dnse.protocol.DnseSubscribe
import com.hmst.pulsar.core.network.dnse.rest.DnseRestApi
import com.hmst.pulsar.core.network.dnse.socket.DnseSocket
import com.hmst.pulsar.core.network.model.BoardId
import com.hmst.pulsar.core.network.model.IndexQuotes
import com.hmst.pulsar.core.network.model.PriceQuotes
import com.hmst.pulsar.core.network.model.ProductGrpId
import com.hmst.pulsar.core.network.model.TradingSession
import com.hmst.pulsar.core.network.msgpack.MsgPack
import com.hmst.pulsar.core.network.msgpack.doubleExt
import com.hmst.pulsar.core.network.msgpack.longExt
import com.hmst.pulsar.core.network.msgpack.strExt
import com.hmst.pulsar.core.network.util.DnseUtil.ENCODING
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

/** Nhận frame từ socket DNSE, giải mã và gom thành các StateFlow */
@Singleton
class DnseDataSource @Inject constructor(
    private val socket: DnseSocket,
    private val restApi: DnseRestApi,
) {
    private val apiKey = BuildConfig.DNSE_API_KEY
    private val apiSecret = BuildConfig.DNSE_API_SECRET
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var jobs: List<Job> = emptyList()

    @Volatile
    private var symbols: List<String> = emptyList()
    private val _quotes = MutableStateFlow<Map<String, PriceQuotes>>(emptyMap())
    val quotes: StateFlow<Map<String, PriceQuotes>> = _quotes.asStateFlow()
    private val _indexQuotes = MutableStateFlow<Map<String, IndexQuotes>>(emptyMap())
    val indexQuotes: StateFlow<Map<String, IndexQuotes>> = _indexQuotes.asStateFlow()
    private val _status = MutableStateFlow("Đang kết nối…")
    val status: StateFlow<String> = _status.asStateFlow()
    private val _tradingSession = MutableStateFlow<TradingSession?>(null)
    val tradingSession: StateFlow<TradingSession?> = _tradingSession.asStateFlow()

    private val buffer = HashMap<String, PriceQuotes>()

    @Volatile
    private var dirty = false

    @Volatile
    private var authed = false
    private val seenTypes = mutableSetOf<String>()

    private val control = setOf(
        "welcome", "auth_success", "subscribed", "unsubscribed",
        "ping", "pong", "connection_expired", "error",
    )

    @Synchronized
    fun start() {
        if (jobs.isNotEmpty()) return
        _status.value = "Đang kết nối…"
        socket.connect(::onFrame)
        jobs = listOf(
            scope.launch { fetchTradingSession() },
            scope.launch {                         // đẩy lên UI tối đa 4 lần/giây
                while (isActive) {
                    delay(250.milliseconds)
                    if (dirty) synchronized(buffer) {
                        _quotes.value = HashMap(buffer); dirty = false
                    }
                }
            },
            scope.launch {                         // DNSE khuyến nghị client tự ping mỗi 25 giây
                while (isActive) {
                    delay(25_000.milliseconds)
                    if (authed) socket.send(MsgPack.encode(mapOf("action" to "ping")))
                }
            },
        )
    }

    /** Huỷ job chứ không huỷ scope, để start() lại được */
    @Synchronized
    fun stop() {
        authed = false
        jobs.forEach { it.cancel() }
        jobs = emptyList()
        socket.close()
        // để lần start() sau REST được gán lại, không giữ phiên cũ
        _tradingSession.value = null
    }

    /** Socket session chỉ gửi khi chuyển phiên, nên lấy trạng thái hiện
    tại qua REST */
    private suspend fun fetchTradingSession() {
        runCatching {
            restApi.tradingSession(ProductGrpId.STO.name, BoardId.G1.name)
        }.onSuccess { res ->
            Log.d("DNSE", "REST trading-session: $res")
            val dto = res.tradingSessions.firstOrNull() ?: return
            // chỉ gán khi socket chưa trả, tránh REST về muộn đè dữ liệu mới hơn
            _tradingSession.compareAndSet(
                null,
                TradingSession(tradingSessionId = dto.tradingSessionId, eventId = dto.eventId)
            )
        }.onFailure {
            // stop() huỷ job không phải lỗi, ném tiếp để coroutine huỷ đúng cách
            if (it is CancellationException) throw it
            Log.e("DNSE", "REST trading-session lỗi", it)
        }
    }

    private fun onFrame(msg: Map<String, Any?>) {
        Log.d("DNSE", "<- $msg")
        val action = msg["action"] as? String
        when (action) {
            "welcome" -> socket.send(DnseAuth.frame(apiKey, apiSecret))
            "auth_success" -> {
                authed = true
                _status.value = "Đã kết nối"
                val subscribe = DnseSubscribe.frame(
                    mapOf(
                        /*"tick.G1.$ENCODING" to symbols,
                        "security_definition.G1.$ENCODING" to symbols,
                        "top_price.G1.$ENCODING" to symbols,*/
                        "session.${ProductGrpId.STO.name}.${BoardId.G1.name}.${ENCODING}" to emptyList()
                    )
                )
                val sent = socket.send(subscribe)
                Log.d("DNSE", "-> subscribe ${MsgPack.decode(subscribe)} sent=$sent")
            }

            "ping" -> socket.send(
                MsgPack.encode(
                    mapOf(
                        "action" to "pong", "timestamp" to msg["timestamp"],
                    )
                )
            )

            "error" -> _status.value = "Lỗi ${msg["code"]}: ${msg["message"]}"
            "connection_expired" -> _status.value = "Phiên hết hạn, cần kết nối lại"
            in control -> Unit
            else -> onData(msg)
        }
    }

    private fun onData(m: Map<String, Any?>) {
        // payload channel session không có trường T, nhận biết qua tscProdGrpId
        if (m.containsKey("tscProdGrpId")) {
            _tradingSession.value = TradingSession(
                tradingSessionId = m.strExt("tradingSessionId"),
                eventId = m.strExt("eventId"),
            )
            return
        }
        val type = m.strExt("T")
        if (type == null) {
            Log.d("DNSE", "DATA không có T, keys=${m.keys} full=$m")
            return
        }
        if (type == "t") {
            applyTick(m); return
        }
        if (seenTypes.add(type)) Log.d(
            "DNSE",
            "DATA lần đầu T=$type keys=${m.keys} full=$m"
        )
    }

    private fun applyTick(m: Map<String, Any?>) {
        val symbol = m.strExt("symbol") ?: return
        synchronized(buffer) {
            val q = buffer[symbol] ?: PriceQuotes(symbol = symbol)
            buffer[symbol] = q.copy(
                matchPrice = m.doubleExt("matchPrice") ?: q.matchPrice,
                matchQtty = m.doubleExt("matchQtty") ?: q.matchQtty,
                openPrice = m.doubleExt("openPrice") ?: q.openPrice,
                highestPrice = m.doubleExt("highestPrice") ?: q.highestPrice,
                lowestPrice = m.doubleExt("lowestPrice") ?: q.lowestPrice,
                totalVolumeTraded = m.longExt("totalVolumeTraded") ?: q.totalVolumeTraded,
                grossTradeAmount = m.doubleExt("grossTradeAmount") ?: q.grossTradeAmount,
            )
            dirty = true
        }
    }
}
