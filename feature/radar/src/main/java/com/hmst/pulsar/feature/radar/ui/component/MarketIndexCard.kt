package com.hmst.pulsar.feature.radar.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hmst.pulsar.feature.radar.domain.model.IndexEnum

@Composable
fun MarketIndexCard(modifier: Modifier = Modifier, index: IndexEnum) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(index.indexCode)
            Row(modifier = Modifier.paddingFromBaseline(16.dp)) {
                Text("${index.indexValue}", modifier = Modifier
                    .weight(1f)
                    .alignByBaseline())
                Text(index.indexChange, modifier = Modifier.alignByBaseline())
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(50.dp)
                    .background(color = MaterialTheme.colorScheme.primaryFixedDim)
            )
        }
    }
}

@Preview
@Composable
fun MarketIndexCardPreview() {
    MarketIndexCard(index = IndexEnum.VNINDEX)
}