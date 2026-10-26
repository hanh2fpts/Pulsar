package com.hmst.pulsar.core.network.mapper

import com.hmst.pulsar.core.network.dnse.dto.OHLCDto
import com.hmst.pulsar.core.network.model.Candle

/** Ghép các mảng song song thành list nến; cắt theo mảng ngắn nhất phòng server trả lệch */
fun OHLCDto.toCandles(): List<Candle> {
    val size = minOf(t.size, o.size, h.size, l.size, c.size, v.size)
    return List(size) { i -> Candle(t[i], o[i], h[i], l[i], c[i], v[i]) }
}
