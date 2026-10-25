package com.hmst.pulsar.core.network.dnse.dto

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
class MarketIndexResponse(
    val marketIndices: List<MarketIndexDto> = emptyList()
)


@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class MarketIndexDto(
    val indexName: String? = null, // Tên chỉ số thị trường
    val valueIndexes: Double? = null, // Giá trị hiện tại của chỉ số
    val changedValue: Double? = null, // Giá trị thay đổi so với tham chiếu
    val changedRatio: Double? = null, // Tỷ lệ thay đổi (%)
    val fluctuationDownIssueVolume: Int? = null, // Tổng khối lượng giao dịch các mã có giá giảm
    val fluctuationSteadinessIssueVolume: Int? = null, // Tổng khối lượng giao dịch các mã có giá không đổi
    val fluctuationUpIssueVolume: Int? = null, // Tổng khối lượng giao dịch các mã có giá tăng
    val fluctuationLowerLimitIssueCount: Int? = null, // Số lượng mã giảm sàn
    val fluctuationDownIssueCount: Int? = null, // Số lượng mã có giá giảm
    val fluctuationSteadinessIssueCount: Int? = null, // Số lượng mã có giá không đổi
    val fluctuationUpIssueCount: Int? = null, // Số lượng mã có giá tăng
    val fluctuationUpperLimitIssueCount: Int? = null, // Số lượng mã tăng trần
    val marketId: String? = null, // Mã thị trường
    val tradingSessionId: String? = null, // Mã phiên giao dịch hiện tại
    val marketIndexClass: String? = null, // Phân loại chỉ số
    val indexTypeCode: String? = null, // Mã loại chỉ số
    val currencyCode: String? = null, // Đơn vị tiền tệ
    val transactionTime: String? = null, // Thời gian giao dịch
    val totalVolumeTraded: Long? = null, // Tổng khối lượng giao dịch trong ngày
    val grossTradeAmount: Double? = null, // Tổng giá trị giao dịch trong ngày
    val contauctAccTrdVol: Long? = null, // Tổng khối lượng giao dịch theo phương thức khớp lệnh
    val contauctAccTrdVal: Double? = null, // Tổng giá trị giao dịch theo phương thức khớp lệnh
    val blkTrdAccTrdVol: Long? = null, // Tổng khối lượng giao dịch theo phương thức thỏa thuận
    val blkTrdAccTrdVal: Double? = null, // Tổng giá trị giao dịch theo phương thức thỏa thuận
    val highestValueIndexes: Double? = null, // Giá cao nhất trong phiên
    val lowestValueIndexes: Double? = null, // Giá thấp nhất trong phiên
    val priorValueIndexes: Double? = null, // Giá trị tham chiếu
)

