package com.hmst.pulsar.core.network.model

/** Một mã trong top ảnh hưởng chỉ số; [influence] là số điểm đóng góp, dương kéo lên, âm đè xuống */
data class IndexInfluence(
    val symbol: String,
    val influence: Double,
)
