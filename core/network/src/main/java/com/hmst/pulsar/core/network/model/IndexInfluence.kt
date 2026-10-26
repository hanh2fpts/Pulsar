package com.hmst.pulsar.core.network.model

import kotlinx.serialization.Serializable

/** Một mã trong top ảnh hưởng chỉ số; [influence] là số điểm đóng góp, dương kéo lên, âm đè xuống */
@Serializable
data class IndexInfluence(
    val symbol: String,
    val influence: Double,
)
