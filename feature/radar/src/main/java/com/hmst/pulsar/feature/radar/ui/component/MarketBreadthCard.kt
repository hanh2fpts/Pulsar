package com.hmst.pulsar.feature.radar.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
    val up = index.fluctuationUpIssueCount.toFloat() / total * 100
    val reference = index.fluctuationSteadinessIssueCount.toFloat() / total * 100
    val down = index.fluctuationDownIssueCount.toFloat() / total * 100
    val ceil = index.fluctuationUpperLimitIssueCount.toFloat() / total * 100
    val floor = index.fluctuationLowerLimitIssueCount.toFloat() / total * 100
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
            // weight() không nhận 0, nên bỏ qua nhóm không có mã nào
            listOf(
                ceil to MaterialTheme.market.ceiling,
                up to MaterialTheme.market.up,
                reference to MaterialTheme.market.reference,
                down to MaterialTheme.market.down,
                floor to MaterialTheme.market.floor,
            ).filter { (weight, _) -> weight > 0f }.forEach { (weight, color) ->
                Box(
                    modifier = Modifier
                        .weight(weight)
                        .height(12.dp)
                        .background(color)
                )
            }
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
            .padding(vertical = 4.dp, horizontal = 8.dp)
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