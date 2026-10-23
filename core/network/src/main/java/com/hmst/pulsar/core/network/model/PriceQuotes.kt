package com.hmst.pulsar.core.network.model

data class PriceQuotes(
    val marketId: String? = null, // string // Mã thị trường niêm yết mã chứng khoán
    val boardId: String? = null, // string // Mã bảng giao dịch
    val isin: String? = null, // string // Mã định danh quốc tế
    val symbol: String? = null, // string // Mã chứng khoán

    val matchPrice: Double? = null, // float // Giá khớp gần nhất
    val matchQtty: Double? = null, // float // Khối lượng khớp gần nhất

    val side: String? = null, // string // Chiều mua, bán chủ động
    val avgPrice: Double? = null, // float // Giá khớp trung bình

    val totalVolumeTraded: Long? = null, // integer // Tổng khối lượng khớp trong ngày
    val grossTradeAmount: Double? = null, // float // Tổng giá trị giao dịch trong ngày

    val highestPrice: Double? = null, // float // Giá khớp cao nhất trong ngày
    val lowestPrice: Double? = null, // float // Giá khớp thấp nhất trong ngày
    val openPrice: Double? = null, // float // Giá mở cửa
    val tradingSessionId: String? = null, // string // Mã phiên giao dịch hiện tại

)