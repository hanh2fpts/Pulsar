package com.hmst.pulsar.core.network.model

data class MarketIndex (
    val indexName: String, // string // Tên chỉ số thị trường
    val changedRatio: Double, // float // Tỷ lệ thay đổi (%)
    val changedValue: Double,// float // Giá trị thay đổi so với tham chiếu
    val fluctuationSteadinessIssueCount: Long, // integer // Số lượng mã có giá không đổi
    val fluctuationDownIssueCount: Long, // integer // Số lượng mã có giá giảm
    val fluctuationUpIssueCount: Long, // integer // Số lượng mã có giá tăng
    val fluctuationLowerLimitIssueCount: Long, // integer // Số lượng mã giảm sàn
    val fluctuationUpperLimitIssueCount: Long, // integer // Số lượng mã tăng trần
    val fluctuationDownIssueVolume: Long, // integer // Tổng khối lượng giao dịch các mã có giá giảm
    val fluctuationUpIssueVolume: Long, // integer // Tổng khối lượng giao dịch các mã có giá tăng
    val fluctuationSteadinessIssueVolume: Long, // integer // Tổng khối lượng giao dịch các mã có giá không đổi
    val currencyCode: String, // string // Đơn vị tiền tệ
    val indexTypeCode: String, // string // Mã loại chỉ số
    val lowestValueIndexes: Double, // float // Giá thấp nhất trong phiên
    val highestValueIndexes: Double, // float // Giá cao nhất trong phiên
    val priorValueIndexes: Double, // float // Giá trị tham chiếu
    val valueIndexes: Double, // float // Giá trị hiện tại của chỉ số
    val contauctAccTrdVal: Double, // float // Tổng giá trị giao dịch theo phương thức khớp lệnh
    val contauctAccTrdVol: Long, // integer // Tổng khối lượng giao dịch theo phương thức khớp lệnh
    val blkTrdAccTrdVal: Double, // float // Tổng giá trị giao dịch theo phương thức thỏa thuận
    val blkTrdAccTrdVol: Long, // integer // Tổng khối lượng giao dịch theo phương thức thỏa thuận
    val grossTradeAmount: Double, // float // Tổng giá trị giao dịch trong ngày
    val totalVolumeTraded: Long, // integer // Tổng khối lượng giao dịch trong ngày
    val marketIndexClass: String, // string // Phân loại chỉ số
    val marketId: String, // string // Mã thị trường
    val tradingSessionId: String, // string // Mã phiên giao dịch hiện tại
)