package com.hmst.pulsar.feature.radar.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hmst.pulsar.core.designsystem.theme.PulsarTextStyle
import com.hmst.pulsar.core.designsystem.theme.market
import com.hmst.pulsar.feature.radar.domain.model.MarketIndexEnum

@Composable
fun MarketIndexCard(modifier: Modifier = Modifier, index: MarketIndexEnum) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // indexCode
            Text(
                index.indexCode,
                style = PulsarTextStyle.Heading2,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    //.alignByBaseline()
                    .padding(end = 10.dp)
            )

            // change
            Icon(
                Icons.Filled.ArrowDropUp,
                contentDescription = null,
                tint = MaterialTheme.market.up
            )
            Text(
                "+6.43",
                style = PulsarTextStyle.Label1Semibold,
                color = MaterialTheme.market.upText,
                modifier = Modifier
                    //.alignByBaseline()
                    .padding(end = 4.dp)
            )
            Text(
                "+0.41%",
                style = PulsarTextStyle.Heading6,
                color = MaterialTheme.market.upText,
                //modifier = Modifier.alignByBaseline()
            )
        }
        Box(
            modifier = Modifier
                .padding(top = 12.dp)
                .background(MaterialTheme.market.floor)
                .height(40.dp)
                .fillMaxWidth()
        ) { }
    }
}

@Preview
@Composable
fun MarketIndexCardPreview() {
    MarketIndexCard(index = MarketIndexEnum.VNINDEX)
}