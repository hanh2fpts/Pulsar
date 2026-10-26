package com.hmst.pulsar.feature.radar.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hmst.pulsar.core.designsystem.theme.PulsarTextStyle
import com.hmst.pulsar.core.designsystem.theme.market
import com.hmst.pulsar.core.designsystem.theme.tint
import com.hmst.pulsar.core.network.model.MarketIndex

@Composable
fun MarketBreadthCard(modifier: Modifier = Modifier, index: MarketIndex) {
    val total = index.fluctuationDownIssueCount + index.fluctuationUpIssueCount + index.fluctuationSteadinessIssueCount
    if (total == 0L) return
    val up = index.fluctuationUpIssueCount.toFloat() / total * 100
    val reference = index.fluctuationSteadinessIssueCount.toFloat() / total * 100
    val down = index.fluctuationDownIssueCount.toFloat() / total * 100
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            "Độ rộng thị trường",
            style = PulsarTextStyle.Body2Semibold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
        ) {
            Box(
                modifier = Modifier
                    .weight(up)
                    .height(12.dp)
                    .background(MaterialTheme.market.up)
            )
            Box(
                modifier = Modifier
                    .weight(reference)
                    .height(12.dp)
                    .background(MaterialTheme.market.reference)
            )
            Box(
                modifier = Modifier
                    .weight(down)
                    .height(12.dp)
                    .background(MaterialTheme.market.down)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "${index.fluctuationUpIssueCount} tăng",
                style = PulsarTextStyle.Label1Semibold,
                color = MaterialTheme.market.upText,
                modifier = Modifier
                    .alignByBaseline()
            )
            Text(
                "${index.fluctuationSteadinessIssueCount} đứng",
                style = PulsarTextStyle.Label1Semibold,
                color = MaterialTheme.market.referenceText,
                modifier = Modifier
                    .alignByBaseline()
            )
            Text(
                "${index.fluctuationDownIssueCount} giảm",
                style = PulsarTextStyle.Label1Semibold,
                color = MaterialTheme.market.downText,
                modifier = Modifier
                    .alignByBaseline()
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.market.ceiling.tint(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            ) {
                Text(
                    "Trần ${index.fluctuationUpperLimitIssueCount}",
                    style = PulsarTextStyle.Caption1Semibold,
                    color = MaterialTheme.market.ceilingText
                )
            }
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.market.reference.tint(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            ) {
                Text(
                    "TC ${index.fluctuationSteadinessIssueCount}", style = PulsarTextStyle.Caption1Semibold,
                    color = MaterialTheme.market.referenceText
                )
            }
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.market.floor.tint(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            ) {
                Text(
                    "Sàn ${index.fluctuationLowerLimitIssueCount}", style = PulsarTextStyle.Caption1Semibold,
                    color = MaterialTheme.market.floorText
                )
            }
        }
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

@Preview
@Composable
fun PreviewMarketBreadthCard() {
    Surface() {
        MarketBreadthCard(index = previewMarketIndex)
    }

}