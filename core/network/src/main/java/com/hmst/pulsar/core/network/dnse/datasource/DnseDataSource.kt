package com.hmst.pulsar.core.network.dnse.datasource

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hmst.pulsar.core.network.BuildConfig
import com.hmst.pulsar.core.network.dnse.dto.MarketIndexDto
import com.hmst.pulsar.core.network.dnse.protocol.DnseAuth
import com.hmst.pulsar.core.network.dnse.protocol.DnseSubscribe
import com.hmst.pulsar.core.network.dnse.rest.DnseRestApi
import com.hmst.pulsar.core.network.dnse.socket.DnseSocket
import com.hmst.pulsar.core.network.mapper.toCandles
import com.hmst.pulsar.core.network.mapper.toIndexQuotes
import com.hmst.pulsar.core.network.model.BoardId
import com.hmst.pulsar.core.network.model.Candle
import com.hmst.pulsar.core.network.model.IndexInfluence
import com.hmst.pulsar.core.network.model.IndexName
import com.hmst.pulsar.core.network.model.IndexQuotes
import com.hmst.pulsar.core.network.model.PriceQuotes
import com.hmst.pulsar.core.network.model.ProductGrpId
import com.hmst.pulsar.core.network.model.Resolution
import com.hmst.pulsar.core.network.model.TradingSession
import com.hmst.pulsar.core.network.msgpack.MsgPack
import com.hmst.pulsar.core.network.msgpack.doubleExt
import com.hmst.pulsar.core.network.msgpack.longExt
import com.hmst.pulsar.core.network.msgpack.strExt
import com.hmst.pulsar.core.network.msgpack.timeMs
import com.hmst.pulsar.core.network.util.DnseUtil.ENCODING
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.milliseconds

/** Nhận frame từ socket DNSE, giải mã và gom thành các StateFlow */
@Singleton
class DnseDataSource @Inject constructor(
    private val socket: DnseSocket,
    private val restApi: DnseRestApi,
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
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
    private val _indexCandles = MutableStateFlow<Map<String, List<Candle>>>(emptyMap())

    /** Nến [CANDLE_RESOLUTION] trong ngày giao dịch gần nhất, key = indexName */
    val indexCandles: StateFlow<Map<String, List<Candle>>> = _indexCandles.asStateFlow()

    private val _indexInfluences = MutableStateFlow<Map<String, List<IndexInfluence>>>(emptyMap())

    /** Top mã ảnh hưởng chỉ số trong ngày, key = indexName; ngoài phiên là bản cuối cùng đã lưu */
    val indexInfluences: StateFlow<Map<String, List<IndexInfluence>>> =
        _indexInfluences.asStateFlow()

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

    private val listIndex = listOf(
        IndexName.VNINDEX,
        IndexName.VN30,
        IndexName.VN100,
        IndexName.HNX,
        IndexName.HNX30,
        IndexName.UPCOM
    )

    /**
     * Channel influence chỉ hỗ trợ các chỉ số này (không có UPCOM).
     * symbols phải là [] như doc; truyền tên chỉ số thì vẫn subscribed nhưng không nhận được gì
     */
    private val influenceIndex = listOf(
        IndexName.VNINDEX,
        IndexName.VN30,
        IndexName.VN100,
        IndexName.HNX,
        IndexName.HNX30,
    )

    @Synchronized
    fun start() {
        if (jobs.isNotEmpty()) return
        _status.value = "Đang kết nối…"
        socket.connect(::onFrame)
        jobs = listOf(
            scope.launch { fetchTradingSession() },
            scope.launch { fetchMarketIndex() },
            scope.launch { fetchIndexCandles() },
            scope.launch { restoreInfluences() },
            scope.launch { persistInfluences() },
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
        _indexQuotes.value = emptyMap()
        _indexCandles.value = emptyMap()
        _indexInfluences.value = emptyMap()
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

    private suspend fun fetchMarketIndex() = coroutineScope {
        val fromRest = listIndex.map { index ->
            async {
                runCatching { latestMarketIndex(index.name) }
                    .onFailure {
                        if (it is CancellationException) throw it
                        Log.e("DNSE", "REST market-index ${index.name} lỗi", it)
                    }.getOrNull()
            }
        }.awaitAll().filterNotNull()
            .mapNotNull { dto -> dto.indexName?.let { it to dto.toIndexQuotes() } }.toMap()
        _indexQuotes.update { current -> fromRest + current }
    }

    /**
     * API bắt from và to cùng một ngày, nên lùi từng ngày tới khi có dữ liệu
     * (cuối tuần, lễ, sáng sớm trước phiên). Lấy bản ghi có transactionTime mới nhất.
     *
     * Cửa sổ 07:00–23:59:59 giờ VN = 00:00–16:59:59 UTC, cùng ngày theo cả hai múi
     * (doc không nói server tính ngày theo múi nào) và vẫn chứa trọn phiên 9h–15h.
     */
    private suspend fun latestMarketIndex(indexName: String): MarketIndexDto? {
        val now = ZonedDateTime.now(VN_ZONE)
        repeat(MAX_LOOKBACK_DAYS) { daysBack ->
            val day = now.toLocalDate().minusDays(daysBack.toLong())
            val from = day.atTime(7, 0).atZone(VN_ZONE).toEpochSecond()
            val to = minOf(day.atTime(23, 59, 59).atZone(VN_ZONE), now).toEpochSecond()
            // trước 7h sáng thì hôm nay chưa có cửa sổ hợp lệ
            if (to <= from) return@repeat
            val records = restApi.getMarketIndex(indexName, from, to).marketIndices
            if (records.isNotEmpty()) {
                return records.maxByOrNull { it.transactionTime.orEmpty() }
            }
        }
        return null
    }

    private suspend fun fetchIndexCandles() = coroutineScope {
        val fromRest = listIndex.map { index ->
            async {
                runCatching { latestIndexCandles(index.name) }
                    .onFailure {
                        if (it is CancellationException) throw it
                        Log.e("DNSE", "REST ohlc ${index.name} lỗi", it)
                    }.getOrNull()?.let { index.name to it }
            }
        }.awaitAll().filterNotNull().toMap()
        // socket đặt sau để nến đang chạy từ socket thắng bản REST cùng time
        _indexCandles.update { current ->
            fromRest.mapValues { (name, rest) -> mergeCandles(rest, current[name].orEmpty()) } +
                    current.filterKeys { it !in fromRest }
        }
    }

    /** Cùng cửa sổ 07:00–23:59:59 giờ VN và cách lùi ngày như [latestMarketIndex] */
    private suspend fun latestIndexCandles(indexName: String): List<Candle>? {
        val now = ZonedDateTime.now(VN_ZONE)
        repeat(MAX_LOOKBACK_DAYS) { daysBack ->
            val day = now.toLocalDate().minusDays(daysBack.toLong())
            val from = day.atTime(7, 0).atZone(VN_ZONE).toEpochSecond()
            val to = minOf(day.atTime(23, 59, 59).atZone(VN_ZONE), now).toEpochSecond()
            if (to <= from) return@repeat
            val candles = restApi.getOhlc(
                symbol = indexName,
                type = OHLC_TYPE_INDEX,
                resolution = CANDLE_RESOLUTION.code,
                from = from,
                to = to,
            ).toCandles()
            if (candles.isNotEmpty()) return candles
        }
        return null
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
                    mapOf("session.${ProductGrpId.STO.name}.${BoardId.G1.name}.${ENCODING}" to emptyList<String>())
                            + listIndex.associate { "market_index.${it.name}.$ENCODING" to emptyList() }
                            + mapOf("ohlc.${CANDLE_RESOLUTION.code}.$ENCODING" to listIndex.map { it.name })
                            + influenceIndex.associate { "market_index_influence.${it.name}.1.$ENCODING" to emptyList<String>() }
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
        // payload channel ohlc nhận biết qua resolution, có symbol thay vì indexName
        if (m.containsKey("resolution")) {
            applyCandle(m); return
        }
        // payload channel market_index_influence nhận biết qua index_name (snake_case)
        if (m.containsKey("index_name")) {
            applyInfluence(m); return
        }
        // payload channel market_index nhận biết qua indexName
        if (m.containsKey("indexName")) {
            applyIndex(m); return
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

    private fun applyInfluence(m: Map<String, Any?>) {
        val name = m.strExt("index_name") ?: return
        // doc ghi "data" nhưng server thực tế trả "Data"
        val items = (m["Data"] as? List<*> ?: m["data"] as? List<*>).orEmpty().mapNotNull { raw ->
            @Suppress("UNCHECKED_CAST")
            val d = raw as? Map<String, Any?> ?: return@mapNotNull null
            IndexInfluence(
                symbol = d.strExt("symbol") ?: return@mapNotNull null,
                influence = d.doubleExt("influence") ?: return@mapNotNull null,
            )
        }
        _indexInfluences.update { it + (name to items) }
    }

    /** Socket chỉ gửi influence trong phiên và không có REST, nên ngoài giờ hiện bản đã lưu */
    private suspend fun restoreInfluences() {
        runCatching {
            dataStore.data.first()[INFLUENCES_KEY]
                ?.let { json.decodeFromString<Map<String, List<IndexInfluence>>>(it) }
        }.onSuccess { cached ->
            // socket đặt sau để dữ liệu mới về trước lúc đọc xong không bị bản lưu đè
            if (cached != null) _indexInfluences.update { current -> cached + current }
        }.onFailure {
            if (it is CancellationException) throw it
            Log.e("DNSE", "Đọc influence đã lưu lỗi", it)
        }
    }

    /** Payload có thể về dồn dập, chỉ ghi khi đã lặng [PERSIST_DEBOUNCE] */
    @OptIn(FlowPreview::class)
    private suspend fun persistInfluences() {
        _indexInfluences
            .filter { it.isNotEmpty() }   // stop() xoá về rỗng, không ghi đè bản đã lưu
            .debounce(PERSIST_DEBOUNCE)
            .collect { latest ->
                runCatching {
                    dataStore.edit { it[INFLUENCES_KEY] = json.encodeToString(latest) }
                }.onFailure {
                    if (it is CancellationException) throw it
                    Log.e("DNSE", "Lưu influence lỗi", it)
                }
            }
    }

    /** Socket ghi bên phải nên luôn thắng REST; field null giữ giá trị cũ */
    private fun applyIndex(m: Map<String, Any?>) {
        val name = m.strExt("indexName") ?: return
        _indexQuotes.update { cur ->
            val old = cur[name]
            cur + (name to IndexQuotes(
                indexName = name,
                changedRatio = m.doubleExt("changedRatio") ?: old?.changedRatio,
                changedValue = m.doubleExt("changedValue") ?: old?.changedValue,
                fluctuationSteadinessIssueCount = m.longExt("fluctuationSteadinessIssueCount")
                    ?: old?.fluctuationSteadinessIssueCount,
                fluctuationDownIssueCount = m.longExt("fluctuationDownIssueCount")
                    ?: old?.fluctuationDownIssueCount,
                fluctuationUpIssueCount = m.longExt("fluctuationUpIssueCount")
                    ?: old?.fluctuationUpIssueCount,
                fluctuationLowerLimitIssueCount = m.longExt("fluctuationLowerLimitIssueCount")
                    ?: old?.fluctuationLowerLimitIssueCount,
                fluctuationUpperLimitIssueCount = m.longExt("fluctuationUpperLimitIssueCount")
                    ?: old?.fluctuationUpperLimitIssueCount,
                fluctuationDownIssueVolume = m.longExt("fluctuationDownIssueVolume")
                    ?: old?.fluctuationDownIssueVolume,
                fluctuationUpIssueVolume = m.longExt("fluctuationUpIssueVolume")
                    ?: old?.fluctuationUpIssueVolume,
                fluctuationSteadinessIssueVolume = m.longExt("fluctuationSteadinessIssueVolume")
                    ?: old?.fluctuationSteadinessIssueVolume,
                currencyCode = m.strExt("currencyCode") ?: old?.currencyCode,
                indexTypeCode = m.strExt("indexTypeCode") ?: old?.indexTypeCode,
                lowestValueIndexes = m.doubleExt("lowestValueIndexes") ?: old?.lowestValueIndexes,
                highestValueIndexes = m.doubleExt("highestValueIndexes")
                    ?: old?.highestValueIndexes,
                priorValueIndexes = m.doubleExt("priorValueIndexes") ?: old?.priorValueIndexes,
                valueIndexes = m.doubleExt("valueIndexes") ?: old?.valueIndexes,
                contauctAccTrdVal = m.doubleExt("contauctAccTrdVal") ?: old?.contauctAccTrdVal,
                contauctAccTrdVol = m.longExt("contauctAccTrdVol") ?: old?.contauctAccTrdVol,
                blkTrdAccTrdVal = m.doubleExt("blkTrdAccTrdVal") ?: old?.blkTrdAccTrdVal,
                blkTrdAccTrdVol = m.longExt("blkTrdAccTrdVol") ?: old?.blkTrdAccTrdVol,
                grossTradeAmount = m.doubleExt("grossTradeAmount") ?: old?.grossTradeAmount,
                totalVolumeTraded = m.longExt("totalVolumeTraded") ?: old?.totalVolumeTraded,
                marketIndexClass = m.strExt("marketIndexClass") ?: old?.marketIndexClass,
                marketId = m.strExt("marketId") ?: old?.marketId,
                tradingSessionId = m.strExt("tradingSessionId") ?: old?.tradingSessionId,
            ))
        }
    }

    private fun applyCandle(m: Map<String, Any?>) {
        if (m.strExt("resolution") != CANDLE_RESOLUTION.code) return
        val symbol = m.strExt("symbol") ?: return
        // doc ghi time là số giây, nhưng các channel giá khác trả {Seconds, Nanos} nên đọc cả hai
        val time = m.longExt("time") ?: m.timeMs("time")?.div(1000)
        if (time == null) {
            if (seenTypes.add("ohlc-bad-time")) Log.w("DNSE", "OHLC không đọc được time, full=$m")
            return
        }
        val candle = Candle(
            time = time,
            open = m.doubleExt("open") ?: return,
            high = m.doubleExt("high") ?: return,
            low = m.doubleExt("low") ?: return,
            close = m.doubleExt("close") ?: return,
            volume = m.longExt("volume") ?: 0L,
        )
        _indexCandles.update { cur ->
            cur + (symbol to mergeCandles(
                cur[symbol].orEmpty(),
                listOf(candle)
            ))
        }
    }

    /**
     * Gộp theo time, [incoming] thắng khi trùng (nến đang hình thành được cập nhật liên tục).
     * Chỉ giữ ngày giao dịch mới nhất: REST lùi về hôm trước thì sang phiên mới socket sẽ thay hẳn.
     */
    private fun mergeCandles(old: List<Candle>, incoming: List<Candle>): List<Candle> {
        val merged = (old + incoming).associateBy { it.time }.values.sortedBy { it.time }
        val lastDay = merged.lastOrNull()?.let { vnDay(it.time) } ?: return emptyList()
        return merged.filter { vnDay(it.time) == lastDay }
    }

    /** Số ngày tính theo giờ VN (UTC+7, không có giờ mùa hè) */
    private fun vnDay(epochSecond: Long): Long = Math.floorDiv(epochSecond + 7 * 3600, 86_400L)

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

    private companion object {
        val VN_ZONE: ZoneId = ZoneId.of("Asia/Ho_Chi_Minh")

        // đủ qua kỳ nghỉ Tết dài nhất (~9 ngày)
        const val MAX_LOOKBACK_DAYS = 10

        val CANDLE_RESOLUTION = Resolution.MINUTE_5
        const val OHLC_TYPE_INDEX = "INDEX"

        val INFLUENCES_KEY = stringPreferencesKey("dnse_index_influences")
        val PERSIST_DEBOUNCE = 2_000.milliseconds
    }
}
