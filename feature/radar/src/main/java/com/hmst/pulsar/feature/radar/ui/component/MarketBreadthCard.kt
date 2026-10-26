package com.hmst.pulsar.feature.radar.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.VerticalAlignBottom
import androidx.compose.material.icons.filled.VerticalAlignTop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hmst.pulsar.core.designsystem.theme.PulsarTextStyle
import com.hmst.pulsar.core.designsystem.theme.market
import com.hmst.pulsar.core.designsystem.theme.tint
import com.hmst.pulsar.core.network.model.MarketIndex

@Composable
fun MarketBreadthCard(modifier: Modifier = Modifier, index: MarketIndex) {
    val total =
        index.fluctuationDownIssueCount + index.fluctuationUpIssueCount + index.fluctuationSteadinessIssueCount
    if (total == 0L) return
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
                .height(12.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // số mã tăng/giảm đã bao gồm mã trần/sàn, nên phần trần/sàn nằm trong nhóm
            BreadthSegment(
                index.fluctuationUpperLimitIssueCount to MaterialTheme.market.ceiling,
                index.fluctuationUpIssueCount - index.fluctuationUpperLimitIssueCount to MaterialTheme.market.up,
            )
            BreadthSegment(
                index.fluctuationSteadinessIssueCount to MaterialTheme.market.reference,
            )
            BreadthSegment(
                index.fluctuationDownIssueCount - index.fluctuationLowerLimitIssueCount to MaterialTheme.market.down,
                index.fluctuationLowerLimitIssueCount to MaterialTheme.market.floor,
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            BreadthChip(
                icon = Icons.Filled.VerticalAlignTop,
                count = index.fluctuationUpperLimitIssueCount,
                color = MaterialTheme.market.ceiling,
                textColor = MaterialTheme.market.ceilingText,
            )
            BreadthChip(
                icon = Icons.AutoMirrored.Filled.TrendingUp,
                count = index.fluctuationUpIssueCount,
                color = MaterialTheme.market.up,
                textColor = MaterialTheme.market.upText,
            )
            BreadthChip(
                icon = Icons.Filled.Remove,
                count = index.fluctuationSteadinessIssueCount,
                color = MaterialTheme.market.reference,
                textColor = MaterialTheme.market.referenceText,
            )
            BreadthChip(
                icon = Icons.AutoMirrored.Filled.TrendingDown,
                count = index.fluctuationDownIssueCount,
                color = MaterialTheme.market.down,
                textColor = MaterialTheme.market.downText,
            )
            BreadthChip(
                icon = Icons.Filled.VerticalAlignBottom,
                count = index.fluctuationLowerLimitIssueCount,
                color = MaterialTheme.market.floor,
                textColor = MaterialTheme.market.floorText,
            )
        }
    }
}

/** Một nhóm trên thanh độ rộng: rộng theo tổng số mã, bo góc, chia màu theo từng phần */
@Composable
private fun RowScope.BreadthSegment(vararg parts: Pair<Long, Color>) {
    val count = parts.sumOf { it.first }
    // weight(0f) sẽ crash, nhóm rỗng thì bỏ qua
    if (count <= 0L) return
    Row(
        modifier = Modifier
            .weight(count.toFloat())
            .fillMaxHeight()
            .clip(RoundedCornerShape(6.dp))
    ) {
        parts.forEach { (partCount, color) ->
            if (partCount > 0L) {
                Box(
                    modifier = Modifier
                        .weight(partCount.toFloat())
                        .fillMaxHeight()
                        .background(color)
                )
            }
        }
    }
}

/** Ô số mã theo nhóm: nền nhạt cùng màu, icon + số căn giữa */
@Composable
private fun RowScope.BreadthChip(
    icon: ImageVector,
    count: Long,
    color: Color,
    textColor: Color,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .weight(1f)
            .background(color = color.tint(), shape = RoundedCornerShape(8.dp))
            .padding(vertical = 5.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // số đứng ngay cạnh đã đủ nghĩa, không cần đọc icon
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
            Text(
                "$count",
                style = PulsarTextStyle.Caption1Semibold,
                color = textColor,
                maxLines = 1,
            )
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
    fluctuationLowerLimitIssueCount = 5,
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