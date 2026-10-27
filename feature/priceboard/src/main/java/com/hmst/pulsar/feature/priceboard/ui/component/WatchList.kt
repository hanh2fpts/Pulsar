package com.hmst.pulsar.feature.priceboard.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hmst.pulsar.core.designsystem.theme.PulsarTextStyle
import com.hmst.pulsar.core.designsystem.theme.market

@Composable
fun WatchList(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(10) {
            WatchListItem()
        }
    }
}

@Composable
fun WatchListItem() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(64.dp)
            .fillMaxWidth()
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // logo
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            // company name
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    "HPG",
                    style = PulsarTextStyle.Heading6,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "KL 1.18 tr",
                    style = PulsarTextStyle.Caption1,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        //chart
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(5.dp)
                .background(MaterialTheme.market.ceiling)
        )
        // price
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                "24.35",
                style = PulsarTextStyle.Heading7,
                color = MaterialTheme.market.upText
            )
            Text(
                "+0.30 +1.25%",
                style = PulsarTextStyle.Caption1Semibold,
                color = MaterialTheme.market.upText
            )
        }
    }
}

@Preview
@Composable
fun WatchListItemPreview() {
    Surface() {
        WatchList()
    }
}