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
import com.hmst.pulsar.feature.radar.domain.model.IndexMarketEnum

@Composable
fun MarketBreadthCard(modifier: Modifier = Modifier, index: IndexMarketEnum) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Độ rộng thị trường",
                style = PulsarTextStyle.Body2Semibold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .weight(1f)
                    .alignByBaseline()
            )
            Text(
                index.indexCode,
                style = PulsarTextStyle.Caption1Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.alignByBaseline()
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
        ) {
            Box(
                modifier = Modifier
                    .weight(2f)
                    .height(12.dp)
                    .background(MaterialTheme.market.up)
            )
            Box(
                modifier = Modifier
                    .weight(3f)
                    .height(12.dp)
                    .background(MaterialTheme.market.reference)
            )
            Box(
                modifier = Modifier
                    .weight(4f)
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
                "144 tăng",
                style = PulsarTextStyle.Label1Semibold,
                color = MaterialTheme.market.upText,
                modifier = Modifier
                    .alignByBaseline()
            )
            Text(
                "64 đứng",
                style = PulsarTextStyle.Label1Semibold,
                color = MaterialTheme.market.referenceText,
                modifier = Modifier
                    .alignByBaseline()
            )
            Text(
                "158 giảm",
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
                    "Trần 7",
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
                    "TC 67", style = PulsarTextStyle.Caption1Semibold,
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
                    "Sàn 10", style = PulsarTextStyle.Caption1Semibold,
                    color = MaterialTheme.market.floorText
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewMarketBreadthCard() {
    Surface() {
        MarketBreadthCard(index = IndexMarketEnum.VNINDEX)
    }

}