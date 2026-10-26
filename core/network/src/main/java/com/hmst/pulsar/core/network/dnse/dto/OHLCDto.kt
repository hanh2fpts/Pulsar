package com.hmst.pulsar.core.network.dnse.dto

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

/** Response của GET /price/ohlc: các mảng song song, phần tử thứ i của mỗi mảng là cây nến thứ i */
@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class OHLCDto(
    val t: List<Long> = emptyList(), // Thời gian bắt đầu nến, unix giây
    val o: List<Double> = emptyList(), // Giá mở cửa
    val h: List<Double> = emptyList(), // Giá cao nhất
    val l: List<Double> = emptyList(), // Giá thấp nhất
    val c: List<Double> = emptyList(), // Giá đóng cửa
    val v: List<Long> = emptyList(), // Khối lượng
    val nextTime: Long = 0, // Mốc nến tiếp theo khi chưa trả hết, 0 nếu đã hết
)
