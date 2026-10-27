package com.hmst.pulsar.feature.radar.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hmst.pulsar.core.designsystem.theme.PulsarTextStyle
import com.hmst.pulsar.core.designsystem.theme.market
import com.hmst.pulsar.core.network.model.Candle
import com.hmst.pulsar.core.network.model.MarketIndex
import java.util.Locale

@Composable
fun MarketIndexCard(
    modifier: Modifier = Modifier,
    index: MarketIndex,
    candles: List<Candle> = emptyList(),
) {
    val change = index.changedValue
    // tăng xanh, giảm đỏ, đứng giá vàng tham chiếu
    val tint = when {
        change > 0 -> MaterialTheme.market.up
        change < 0 -> MaterialTheme.market.down
        else -> MaterialTheme.market.reference
    }
    val textColor = when {
        change > 0 -> MaterialTheme.market.upText
        change < 0 -> MaterialTheme.market.downText
        else -> MaterialTheme.market.referenceText
    }
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // giá trị chỉ số
            Text(
                String.format(Locale.US, "%,.2f", index.valueIndexes),
                style = PulsarTextStyle.Heading2,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    //.alignByBaseline()
                    .padding(end = 10.dp)
            )

            // change
            if (change != 0.0) Icon(
                if (change > 0) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                contentDescription = null,
                tint = tint
            )
            Text(
                // %+ luôn in dấu: +6.43 / -6.43 / +0.00
                String.format(Locale.US, "%+.2f", change),
                style = PulsarTextStyle.Label1Semibold,
                color = textColor,
                modifier = Modifier
                    //.alignByBaseline()
                    .padding(end = 4.dp)
            )
            Text(
                String.format(Locale.US, "%+.2f%%", index.changedRatio),
                style = PulsarTextStyle.Label1Semibold,
                color = textColor,
                //modifier = Modifier.alignByBaseline()
            )
        }
        // phần chart index, chưa đủ nến (đầu phiên) thì vẽ đường ngang tại tham chiếu
        val reference = index.priorValueIndexes.takeIf { it > 0 } ?: index.valueIndexes
        val points = remember(candles, reference) {
            candles.toChartPoints().takeIf { it.size >= 2 } ?: flatChartPoints(reference)
        }
        IndexLineChart(
            points = points,
            color = tint,
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .padding(top = 8.dp),
        )
    }
}

/** Dữ liệu mẫu cho preview, lấy theo payload ví dụ trong doc DNSE */
private val previewMarketIndex = MarketIndex(
    indexName = "VNINDEX",
    changedRatio = 0.41,
    changedValue = 6.84,
    fluctuationSteadinessIssueCount = 67,
    fluctuationDownIssueCount = 158,
    fluctuationUpIssueCount = 144,
    fluctuationLowerLimitIssueCount = 0,
    fluctuationUpperLimitIssueCount = 7,
    fluctuationDownIssueVolume = 220_246_500,
    fluctuationUpIssueVolume = 446_927_155,
    fluctuationSteadinessIssueVolume = 39_390_038,
    currencyCode = "VND",
    indexTypeCode = "001",
    lowestValueIndexes = 1662.05,
    highestValueIndexes = 1677.83,
    priorValueIndexes = 1662.54,
    valueIndexes = 1669.38,
    contauctAccTrdVal = 15609.88011093,
    contauctAccTrdVol = 606_182_599,
    blkTrdAccTrdVal = 3040.58723198,
    blkTrdAccTrdVol = 100_381_155,
    grossTradeAmount = 18650.46734291,
    totalVolumeTraded = 706_563_754,
    marketIndexClass = "HSX",
    marketId = "STO",
    tradingSessionId = "40",
)

/** 12 nến 5 phút giả lập từ 9h15 ngày 26/10/2026 */
private val previewCandles = listOf(
    1662.5, 1664.1, 1663.2, 1666.8, 1665.0, 1668.3, 1671.2, 1669.9, 1673.4, 1677.8, 1674.1, 1669.4,
).mapIndexed { i, close ->
    Candle(time = 1_792_980_900L + i * 300, open = close, high = close, low = close, close = close, volume = 0)
}

@Preview
@Composable
fun MarketIndexCardPreview() {
    Surface {
        MarketIndexCard(index = previewMarketIndex, candles = previewCandles)
    }
}
