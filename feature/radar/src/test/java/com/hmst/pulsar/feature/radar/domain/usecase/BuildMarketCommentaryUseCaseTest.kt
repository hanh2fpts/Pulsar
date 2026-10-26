package com.hmst.pulsar.feature.radar.domain.usecase

import com.hmst.pulsar.core.network.model.MarketIndex
import com.hmst.pulsar.feature.radar.domain.model.TradingSessionEnum
import com.hmst.pulsar.feature.radar.domain.model.TradingSessionEnum.Closed
import com.hmst.pulsar.feature.radar.domain.model.TradingSessionEnum.Continuous
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BuildMarketCommentaryUseCaseTest {

    private val build = BuildMarketCommentaryUseCase()

    private fun commentary(
        change: Double,
        up: Long,
        down: Long,
        ceilings: Long = 0,
        floors: Long = 0,
        amount: Double = 100.0,
        avg: Double? = null,
        session: TradingSessionEnum = Continuous,
    ) = build(session, index(change, up, down, ceilings, floors, amount), avg)

    @Test
    fun `A va B khong can du lieu`() {
        assertEquals("Thị trường chưa mở cửa.", build(TradingSessionEnum.PreMarket, null, null))
        assertEquals(
            "Thị trường đang nghỉ trưa, giá giữ nguyên đến phiên chiều.",
            build(TradingSessionEnum.LunchBreak, null, null)
        )
    }

    @Test
    fun `thieu du lieu thi an`() {
        assertNull(build(Continuous, null, null))
        assertNull(commentary(change = 1.0, up = 0, down = 0))
    }

    @Test
    fun `C xet truoc D va truoc bien dong gia`() {
        assertEquals(
            "VN-Index có 23 mã giảm sàn, áp lực bán đang rất mạnh.",
            commentary(change = 3.0, up = 300, down = 50, ceilings = 25, floors = 23)
        )
        assertEquals(
            "VN-Index có 21 mã tăng trần, lực mua đang rất mạnh.",
            commentary(change = 3.0, up = 300, down = 50, ceilings = 21)
        )
    }

    @Test
    fun `tang manh E va F`() {
        assertEquals(
            "VN-Index tăng mạnh 2,35%, đa số cổ phiếu cùng tăng.",
            commentary(change = 2.35, up = 60, down = 40)
        )
        assertEquals(
            "VN-Index tăng mạnh 2,35%, chủ yếu nhờ vài mã lớn, số mã tăng chưa áp đảo.",
            commentary(change = 2.35, up = 59, down = 41)
        )
    }

    @Test
    fun `tang G va H tai bien`() {
        assertEquals(
            "VN-Index tăng, số mã tăng nhiều hơn số mã giảm.",
            commentary(change = 0.5, up = 55, down = 45)
        )
        assertEquals(
            "VN-Index tăng nhưng đà tăng chưa lan rộng, số mã tăng chưa áp đảo.",
            commentary(change = 1.99, up = 54, down = 46)
        )
    }

    @Test
    fun `di ngang I J K, 45 phan tram roi vao J`() {
        assertEquals(
            "VN-Index đi ngang nhưng số mã tăng nhiều hơn rõ rệt.",
            commentary(change = 0.49, up = 55, down = 45)
        )
        assertEquals(
            "VN-Index đi ngang, mã tăng và mã giảm gần như cân bằng.",
            commentary(change = -0.49, up = 45, down = 55)
        )
        assertEquals(
            "VN-Index đi ngang nhưng số mã giảm nhiều hơn rõ rệt.",
            commentary(change = 0.0, up = 44, down = 56)
        )
    }

    @Test
    fun `giam L M tai bien`() {
        assertEquals(
            "VN-Index giảm nhưng đà giảm chưa lan rộng, số mã giảm chưa áp đảo.",
            commentary(change = -0.5, up = 46, down = 54)
        )
        assertEquals(
            "VN-Index giảm, số mã giảm nhiều hơn số mã tăng.",
            commentary(change = -1.99, up = 45, down = 55)
        )
    }

    @Test
    fun `giam manh N va O`() {
        assertEquals(
            "VN-Index giảm mạnh 2,35%, chủ yếu do vài mã lớn, số mã giảm chưa áp đảo.",
            commentary(change = -2.35, up = 41, down = 59)
        )
        assertEquals(
            "VN-Index giảm mạnh 2,00%, đa số cổ phiếu cùng giảm.",
            commentary(change = -2.0, up = 40, down = 60)
        )
    }

    @Test
    fun `chot phien va thanh khoan`() {
        assertEquals(
            "Chốt phiên: VN-Index tăng, số mã tăng nhiều hơn số mã giảm. Thanh khoản cao hơn trung bình 20 phiên.",
            commentary(change = 1.0, up = 60, down = 40, amount = 130.0, avg = 100.0, session = Closed)
        )
        assertEquals(
            "VN-Index tăng, số mã tăng nhiều hơn số mã giảm. Thanh khoản thấp hơn trung bình 20 phiên.",
            commentary(change = 1.0, up = 60, down = 40, amount = 70.0, avg = 100.0)
        )
        assertEquals(
            "VN-Index tăng, số mã tăng nhiều hơn số mã giảm.",
            commentary(change = 1.0, up = 60, down = 40, amount = 100.0, avg = 100.0)
        )
    }

    private fun index(
        change: Double, up: Long, down: Long, ceilings: Long, floors: Long, amount: Double,
    ) = MarketIndex(
        indexName = "VNINDEX",
        changedRatio = change,
        changedValue = 0.0,
        fluctuationSteadinessIssueCount = 50,
        fluctuationDownIssueCount = down,
        fluctuationUpIssueCount = up,
        fluctuationLowerLimitIssueCount = floors,
        fluctuationUpperLimitIssueCount = ceilings,
        fluctuationDownIssueVolume = 0,
        fluctuationUpIssueVolume = 0,
        fluctuationSteadinessIssueVolume = 0,
        currencyCode = "VND",
        indexTypeCode = "001",
        lowestValueIndexes = 0.0,
        highestValueIndexes = 0.0,
        priorValueIndexes = 0.0,
        valueIndexes = 0.0,
        contauctAccTrdVal = 0.0,
        contauctAccTrdVol = 0,
        blkTrdAccTrdVal = 0.0,
        blkTrdAccTrdVol = 0,
        grossTradeAmount = amount,
        totalVolumeTraded = 0,
        marketIndexClass = "HSX",
        marketId = "STO",
        tradingSessionId = "40",
    )
}
