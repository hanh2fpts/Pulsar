package com.hmst.pulsar.core.network.dnse.repository

import android.util.Log
import com.hmst.pulsar.core.network.BuildConfig
import com.hmst.pulsar.core.network.dnse.DnseConstants.ENCODING
import com.hmst.pulsar.core.network.model.Quote
import com.hmst.pulsar.core.network.dnse.protocol.DnseAuth
import com.hmst.pulsar.core.network.dnse.protocol.DnseSubscribe
import com.hmst.pulsar.core.network.dnse.socket.DnseSocket
import com.hmst.pulsar.core.network.msgpack.MsgPack
import com.hmst.pulsar.core.network.msgpack.long
import com.hmst.pulsar.core.network.msgpack.num
import com.hmst.pulsar.core.network.msgpack.str
import com.hmst.pulsar.core.network.msgpack.timeMs
import com.hmst.pulsar.core.network.repository.PulsarRepository
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

@Singleton
class PulsarRepositoryImpl @Inject constructor(
    private val socket: DnseSocket,
) : PulsarRepository {
    private val apiKey = BuildConfig.DNSE_API_KEY
    private val apiSecret = BuildConfig.DNSE_API_SECRET
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var jobs: List<Job> = emptyList()
    @Volatile
    private var symbols: List<String> = emptyList()

    private val _quotes = MutableStateFlow<Map<String, Quote>>(emptyMap())
    override val quotes: StateFlow<Map<String, Quote>> = _quotes.asStateFlow()

    private val _status = MutableStateFlow("Đang kết nối…")
    override val status: StateFlow<String> = _status.asStateFlow()

    private val buffer = HashMap<String, Quote>()
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
    override fun start(symbols: List<String>) {
        if (jobs.isNotEmpty()) return
        this.symbols = symbols
        _status.value = "Đang kết nối…"
        socket.connect(::onFrame)
        jobs = listOf(
            scope.launch {                         // đẩy lên UI tối đa 4 lần/giây
                while (isActive) {
                    delay(250)
                    if (dirty) synchronized(buffer) { _quotes.value = HashMap(buffer); dirty = false }
                }
            },
            scope.launch {                         // DNSE khuyến nghị client tự ping mỗi 25 giây
                while (isActive) {
                    delay(25_000)
                    if (authed) socket.send(MsgPack.encode(mapOf("action" to "ping")))
                }
            },
        )
    }

    /** Huỷ job chứ không huỷ scope, để start() lại được */
    @Synchronized
    override fun stop() {
        authed = false
        jobs.forEach { it.cancel() }
        jobs = emptyList()
        socket.close()
    }

    private fun onFrame(msg: Map<String, Any?>) {
        val action = msg["action"] as? String
        when (action) {
            "welcome" -> socket.send(DnseAuth.frame(apiKey, apiSecret))
            "auth_success" -> {
                authed = true
                _status.value = "Đã kết nối"
                socket.send(
                    DnseSubscribe.frame(
                        mapOf(
                            "tick.G1.$ENCODING" to symbols,
                            "security_definition.G1.$ENCODING" to symbols,
                            "top_price.G1.$ENCODING" to symbols,
                        )
                    )
                )
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
        val type = m.str("T") ?: return
        if (type == "t") {
            applyTick(m); return
        }
        // loại khác: chỉ log đầy đủ lần đầu để mình map trường ở bước sau
        if (seenTypes.add(type)) Log.d("DNSE", "DATA lần đầu T=$type keys=${m.keys} full=$m")
    }

    private fun applyTick(m: Map<String, Any?>) {
        val symbol = m.str("symbol") ?: return
        synchronized(buffer) {
            val q = buffer[symbol] ?: Quote(symbol)
            buffer[symbol] = q.copy(
                last = m.num("matchPrice") ?: q.last,
                lastQty = m.num("matchQtty") ?: q.lastQty,
                open = m.num("openPrice") ?: q.open,
                high = m.num("highestPrice") ?: q.high,
                low = m.num("lowestPrice") ?: q.low,
                totalVolume = m.long("totalVolumeTraded") ?: q.totalVolume,
                tradeTimeMs = m.timeMs("time") ?: q.tradeTimeMs,
            )
            dirty = true
        }
    }
}