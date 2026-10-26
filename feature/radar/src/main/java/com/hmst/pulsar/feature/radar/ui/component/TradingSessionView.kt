package com.hmst.pulsar.feature.radar.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hmst.pulsar.core.designsystem.theme.PulsarTextStyle
import com.hmst.pulsar.core.designsystem.theme.market
import com.hmst.pulsar.feature.radar.domain.model.TradingSessionEnum

@Composable
fun TradingSessionView(tradingSession: TradingSessionEnum) {
    val bgColor = when (tradingSession) {
        TradingSessionEnum.PreMarket -> MaterialTheme.colorScheme.surfaceVariant
        TradingSessionEnum.ATO -> MaterialTheme.colorScheme.surfaceContainerLowest
        TradingSessionEnum.Continuous -> MaterialTheme.colorScheme.primaryContainer
        TradingSessionEnum.LunchBreak -> MaterialTheme.colorScheme.surfaceVariant
        TradingSessionEnum.ATC -> MaterialTheme.colorScheme.surfaceContainerLowest
        TradingSessionEnum.PT -> MaterialTheme.colorScheme.primary
        TradingSessionEnum.Closed -> MaterialTheme.colorScheme.surfaceVariant
    }
    val textColor = when (tradingSession) {
        TradingSessionEnum.PreMarket -> MaterialTheme.colorScheme.onSurfaceVariant
        TradingSessionEnum.ATO -> MaterialTheme.colorScheme.primary
        TradingSessionEnum.Continuous -> MaterialTheme.colorScheme.onPrimaryContainer
        TradingSessionEnum.LunchBreak -> MaterialTheme.colorScheme.onSurfaceVariant
        TradingSessionEnum.ATC -> MaterialTheme.colorScheme.primary
        TradingSessionEnum.PT -> MaterialTheme.colorScheme.onPrimary
        TradingSessionEnum.Closed -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    val dotColor = when (tradingSession) {
        TradingSessionEnum.PreMarket -> MaterialTheme.colorScheme.outline
        TradingSessionEnum.ATO -> MaterialTheme.colorScheme.primary
        TradingSessionEnum.Continuous -> MaterialTheme.market.up
        TradingSessionEnum.LunchBreak -> MaterialTheme.colorScheme.outline
        TradingSessionEnum.ATC -> MaterialTheme.colorScheme.primary
        TradingSessionEnum.PT -> MaterialTheme.colorScheme.primaryContainer
        TradingSessionEnum.Closed -> MaterialTheme.colorScheme.outline
    }
    val borderColor = when (tradingSession) {
        TradingSessionEnum.PreMarket -> MaterialTheme.colorScheme.surfaceVariant
        TradingSessionEnum.ATO -> MaterialTheme.colorScheme.primary
        TradingSessionEnum.Continuous -> MaterialTheme.colorScheme.primaryContainer
        TradingSessionEnum.LunchBreak -> MaterialTheme.colorScheme.surfaceVariant
        TradingSessionEnum.ATC -> MaterialTheme.colorScheme.primary
        TradingSessionEnum.PT -> MaterialTheme.colorScheme.primary
        TradingSessionEnum.Closed -> MaterialTheme.colorScheme.surfaceVariant
    }
    // phien lam viec
    Box(
        modifier = Modifier
            .background(
                bgColor,
                shape = RoundedCornerShape(16.dp)
            )
            .border(1.dp, borderColor, CircleShape)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 4.dp
            )
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(dotColor, CircleShape)
            )
            Text(
                tradingSession.status,
                style = PulsarTextStyle.Caption1Semibold,
                color = textColor
            )
        }
    }
}