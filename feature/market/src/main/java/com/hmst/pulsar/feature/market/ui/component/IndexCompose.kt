package com.hmst.pulsar.feature.market.ui.component

import android.graphics.fonts.FontStyle
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun IndexCompose(modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("VNINDEX")
            Row(modifier = Modifier.paddingFromBaseline(16.dp)) {
                Text("1,700.40", modifier = Modifier.weight(1f).alignByBaseline())
                Text("-0.85%", modifier = Modifier.alignByBaseline())
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
fun IndexComposePreview() {
    IndexCompose()
}