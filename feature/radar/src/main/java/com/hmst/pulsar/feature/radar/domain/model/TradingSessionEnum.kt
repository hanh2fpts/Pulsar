package com.hmst.pulsar.feature.radar.domain.model

enum class TradingSessionEnum(val status: String) {
    PreMarket("Chờ mở cửa"),
    ATO("ATO"),
    Continuous("Phiên liên tục"),
    LunchBreak("Nghỉ trưa"),
    ATC("ATC"),
    PT("Phiên thỏa thuận"),
    Closed("Đóng cửa")
}