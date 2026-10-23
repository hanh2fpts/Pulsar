package com.hmst.pulsar.core.network.model

import com.hmst.pulsar.core.network.dnse.DnseConstants.SHARES_PER_QTY_UNIT

data class Quote(
    val symbol: String,
    val last: Double? = null,       // nghìn đồng (57.8 = 57.800đ)
    val lastQty: Double? = null,    // đơn vị gốc của DNSE
    val open: Double? = null,
    val high: Double? = null,
    val low: Double? = null,
    val totalVolume: Long = 0,      // đơn vị gốc của DNSE
    val tradeTimeMs: Long = 0,
)

val Quote.volumeShares: Long get() = totalVolume * SHARES_PER_QTY_UNIT