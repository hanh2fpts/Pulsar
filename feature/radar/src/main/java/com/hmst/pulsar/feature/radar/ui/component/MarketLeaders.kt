package com.hmst.pulsar.feature.radar.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.hmst.pulsar.core.designsystem.theme.PulsarTextStyle
import com.hmst.pulsar.core.designsystem.theme.market
import com.hmst.pulsar.core.network.model.IndexInfluence
import java.util.Locale
import kotlin.math.abs

private const val TOP_COUNT = 10
private const val LOGO_URL = "https://storage.googleapis.com/cdn-entrade/company/"

@Composable
fun MarketLeaders(influences: List<IndexInfluence>, modifier: Modifier = Modifier) {
    val gainers = influences.filter { it.influence > 0 }
        .sortedByDescending { it.influence }.take(TOP_COUNT)
    val losers = influences.filter { it.influence < 0 }
        .sortedBy { it.influence }.take(TOP_COUNT)
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Nhóm dẫn dắt thị trường",
            style = PulsarTextStyle.Body2Semibold,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (gainers.isEmpty() && losers.isEmpty()) {
            Text(
                "Dữ liệu sẽ có khi vào phiên",
                style = PulsarTextStyle.Body3,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            return@Column
        }
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            LeaderColumn(gainers, isUp = true, modifier = Modifier.weight(1f))
            LeaderColumn(losers, isUp = false, modifier = Modifier.weight(1f))
        }
    }
}

/** Mỗi cột chuẩn hoá thanh theo mã lớn nhất của chính cột đó */
@Composable
private fun LeaderColumn(items: List<IndexInfluence>, isUp: Boolean, modifier: Modifier) {
    val max = items.maxOfOrNull { abs(it.influence) } ?: return
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.forEach { item ->
            LeaderRow(item, fraction = (abs(item.influence) / max).toFloat(), isUp = isUp)
        }
    }
}

/** Cột tăng: điểm | mã + thanh | logo; cột giảm đối xứng, thanh mọc từ phía logo ra */
@Composable
private fun LeaderRow(item: IndexInfluence, fraction: Float, isUp: Boolean) {
    val color = if (isUp) MaterialTheme.market.up else MaterialTheme.market.down
    val textColor = if (isUp) MaterialTheme.market.upText else MaterialTheme.market.downText
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isUp) InfluenceValue(item.influence, textColor, TextAlign.Start)
        else CompanyLogo(item.symbol)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = if (isUp) Alignment.End else Alignment.Start,
        ) {
            Text(
                item.symbol,
                style = PulsarTextStyle.Label1Semibold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
            )
            InfluenceBar(fraction, color, alignEnd = isUp)
        }
        if (isUp) CompanyLogo(item.symbol)
        else InfluenceValue(item.influence, textColor, TextAlign.End)
    }
}

@Composable
private fun InfluenceValue(value: Double, color: Color, align: TextAlign) {
    // rộng cố định để thanh các dòng thẳng hàng; 36dp đủ cho "+12,3"
    Text(
        formatInfluence(value),
        style = PulsarTextStyle.Label1,
        color = color,
        textAlign = align,
        maxLines = 1,
        modifier = Modifier.width(36.dp),
    )
}

@Composable
private fun InfluenceBar(fraction: Float, color: Color, alignEnd: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .align(if (alignEnd) Alignment.CenterEnd else Alignment.CenterStart)
                .fillMaxWidth(fraction)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(color)
        )
    }
}

/** Chữ viết tắt nằm dưới, logo tải xong sẽ che lên; lỗi tải thì vẫn còn chữ */
@Composable
private fun CompanyLogo(symbol: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(28.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Text(
            symbol.take(2),
            style = PulsarTextStyle.Caption1Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        AsyncImage(
            model = LOGO_URL + symbol,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

/** Một chữ số thập phân, dấu phẩy kiểu VN, dấu trừ chuẩn (−) cho dễ đọc */
private fun formatInfluence(value: Double): String {
    val text = String.format(Locale.US, "%.1f", abs(value)).replace('.', ',')
    return if (value >= 0) "+$text" else "−$text"
}

private val previewInfluences = listOf(
    IndexInfluence("VCB", 2.4), IndexInfluence("BID", 1.9), IndexInfluence("FPT", 1.6),
    IndexInfluence("HPG", 1.1), IndexInfluence("MBB", 0.9), IndexInfluence("TCB", 0.8),
    IndexInfluence("CTG", 0.7), IndexInfluence("ACB", 0.6), IndexInfluence("VPB", 0.5),
    IndexInfluence("SSI", 0.4), IndexInfluence("VIC", -2.0), IndexInfluence("GAS", -1.3),
    IndexInfluence("VHM", -0.9), IndexInfluence("VNM", -0.8), IndexInfluence("MSN", -0.7),
    IndexInfluence("SAB", -0.6), IndexInfluence("GVR", -0.5), IndexInfluence("PLX", -0.4),
    IndexInfluence("BCM", -0.4), IndexInfluence("POW", -0.3),
)

@Preview
@Composable
fun PreviewMarketLeaders() {
    Surface() {
        MarketLeaders(influences = previewInfluences)
    }
}
