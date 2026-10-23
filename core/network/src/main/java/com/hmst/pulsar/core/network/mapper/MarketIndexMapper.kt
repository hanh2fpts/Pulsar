package com.hmst.pulsar.core.network.mapper

import com.hmst.pulsar.core.network.model.IndexQuotes
import com.hmst.pulsar.core.network.model.MarketIndex

fun IndexQuotes.toDomain() = MarketIndex(
    indexName = indexName.orEmpty(),
    changedValue = changedValue ?: 0.00,
    changedRatio = changedRatio ?: 0.00,
    fluctuationSteadinessIssueCount = fluctuationSteadinessIssueCount ?: 0L,
    fluctuationDownIssueCount = fluctuationDownIssueCount ?: 0L,
    fluctuationUpIssueCount = fluctuationUpIssueCount ?: 0L,
    fluctuationLowerLimitIssueCount = fluctuationLowerLimitIssueCount ?: 0L,
    fluctuationUpperLimitIssueCount = fluctuationUpperLimitIssueCount ?: 0L,
    fluctuationDownIssueVolume = fluctuationDownIssueVolume ?: 0L,
    fluctuationUpIssueVolume = fluctuationUpIssueVolume ?: 0L,
    fluctuationSteadinessIssueVolume = fluctuationSteadinessIssueVolume ?: 0L,
    currencyCode = currencyCode.orEmpty(),
    indexTypeCode = indexTypeCode.orEmpty(),
    lowestValueIndexes = lowestValueIndexes ?: 0.00,
    highestValueIndexes = highestValueIndexes ?: 0.00,
    priorValueIndexes = priorValueIndexes ?: 0.00,
    valueIndexes = valueIndexes ?: 0.00,
    contauctAccTrdVal = contauctAccTrdVal ?: 0.00,
    contauctAccTrdVol = contauctAccTrdVol ?: 0L,
    blkTrdAccTrdVal = blkTrdAccTrdVal ?: 0.00,
    blkTrdAccTrdVol = blkTrdAccTrdVol ?: 0L,
    grossTradeAmount = grossTradeAmount ?: 0.00,
    totalVolumeTraded = totalVolumeTraded ?: 0L,
    marketIndexClass = marketIndexClass.orEmpty(),
    marketId = marketId.orEmpty(),
    tradingSessionId = tradingSessionId.orEmpty(),
)