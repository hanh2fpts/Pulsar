package com.hmst.pulsar.feature.radar.domain.usecase

import com.hmst.pulsar.core.network.model.MarketIndex
import com.hmst.pulsar.feature.radar.domain.model.TradingSessionEnum
import java.util.Locale
import javax.inject.Inject
import kotlin.math.abs

/**
 * Câu nhận xét VN-Index ở đầu Radar theo bảng tình huống A–O: xét lần lượt, khớp đầu tiên thắng.
 * Trả null khi thiếu dữ liệu để UI ẩn khối.
 *
 * Ngưỡng là đề xuất, chỉnh ở companion object.
 */
class BuildMarketCommentaryUseCase @Inject constructor() {

    /** [avgTradeAmount20] là trung bình giá trị GD 20 phiên trước, null khi chưa đủ dữ liệu */
    operator fun invoke(
        session: TradingSessionEnum,
        vnIndex: MarketIndex?,
        avgTradeAmount20: Double?,
    ): String? {
        when (session) {
            TradingSessionEnum.PreMarket -> return "Thị trường chưa mở cửa."
            TradingSessionEnum.LunchBreak ->
                return "Thị trường đang nghỉ trưa, giá giữ nguyên đến phiên chiều."
            else -> Unit
        }
        val index = vnIndex ?: return null
        val up = index.fluctuationUpIssueCount
        val down = index.fluctuationDownIssueCount
        if (up + down == 0L) return null

        // số mã không đổi chỉ để hiển thị, không tính vào tỉ lệ
        val upRatio = up * 100.0 / (up + down)
        val change = index.changedRatio
        val pct = formatPercent(abs(change))
        val floors = index.fluctuationLowerLimitIssueCount
        val ceilings = index.fluctuationUpperLimitIssueCount

        val sentence = when {
            floors >= LIMIT_COUNT ->                                            // C
                "VN-Index có $floors mã giảm sàn, áp lực bán đang rất mạnh."
            ceilings >= LIMIT_COUNT ->                                          // D
                "VN-Index có $ceilings mã tăng trần, lực mua đang rất mạnh."
            change >= STRONG_CHANGE ->
                if (upRatio >= 60.0) "VN-Index tăng mạnh $pct, đa số cổ phiếu cùng tăng."    // E
                else "VN-Index tăng mạnh $pct, chủ yếu nhờ vài mã lớn, số mã tăng chưa áp đảo." // F
            change >= MILD_CHANGE ->
                if (upRatio >= BROAD_RATIO) "VN-Index tăng, số mã tăng nhiều hơn số mã giảm." // G
                else "VN-Index tăng nhưng đà tăng chưa lan rộng, số mã tăng chưa áp đảo."    // H
            change > -MILD_CHANGE -> when {
                upRatio >= BROAD_RATIO -> "VN-Index đi ngang nhưng số mã tăng nhiều hơn rõ rệt." // I
                // J lấy cả 45% vì xét trước K
                upRatio >= NARROW_RATIO -> "VN-Index đi ngang, mã tăng và mã giảm gần như cân bằng." // J
                else -> "VN-Index đi ngang nhưng số mã giảm nhiều hơn rõ rệt."                    // K
            }
            change > -STRONG_CHANGE ->
                if (upRatio > NARROW_RATIO) "VN-Index giảm nhưng đà giảm chưa lan rộng, số mã giảm chưa áp đảo." // L
                else "VN-Index giảm, số mã giảm nhiều hơn số mã tăng."                          // M
            else ->
                if (upRatio > 40.0) "VN-Index giảm mạnh $pct, chủ yếu do vài mã lớn, số mã giảm chưa áp đảo." // N
                else "VN-Index giảm mạnh $pct, đa số cổ phiếu cùng giảm."                        // O
        }

        val liquidity = avgTradeAmount20?.takeIf { it > 0 }?.let { avg ->
            val ratio = index.grossTradeAmount / avg
            when {
                ratio >= HIGH_LIQUIDITY -> " Thanh khoản cao hơn trung bình 20 phiên."
                ratio <= LOW_LIQUIDITY -> " Thanh khoản thấp hơn trung bình 20 phiên."
                else -> null
            }
        }.orEmpty()
        val prefix = if (session == TradingSessionEnum.Closed) "Chốt phiên: " else ""
        return prefix + sentence + liquidity
    }

    /** 2,35% — hai chữ số thập phân, dấu phẩy kiểu VN */
    private fun formatPercent(value: Double): String =
        String.format(Locale.US, "%.2f", value).replace('.', ',') + "%"

    private companion object {
        const val LIMIT_COUNT = 20L
        const val STRONG_CHANGE = 2.0
        const val MILD_CHANGE = 0.5
        const val BROAD_RATIO = 55.0
        const val NARROW_RATIO = 45.0
        const val HIGH_LIQUIDITY = 1.3
        const val LOW_LIQUIDITY = 0.7
    }
}
