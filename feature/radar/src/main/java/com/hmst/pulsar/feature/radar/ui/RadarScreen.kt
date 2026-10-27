package com.hmst.pulsar.feature.radar.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SsidChart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hmst.pulsar.core.designsystem.theme.PulsarTextStyle
import com.hmst.pulsar.core.designsystem.theme.PulsarTheme
import com.hmst.pulsar.feature.radar.R
import com.hmst.pulsar.feature.radar.domain.model.MarketIndexEnum
import com.hmst.pulsar.feature.radar.ui.component.CommentarySkeleton
import com.hmst.pulsar.feature.radar.domain.model.TradingSessionEnum
import com.hmst.pulsar.feature.radar.ui.component.MarketBreadthCard
import com.hmst.pulsar.feature.radar.ui.component.MarketHealth
import com.hmst.pulsar.feature.radar.ui.component.MarketIndexCard
import com.hmst.pulsar.feature.radar.ui.component.MarketLeaders
import com.hmst.pulsar.feature.radar.ui.component.MarketLiquidity
import com.hmst.pulsar.feature.radar.ui.component.RadarPageSkeleton
import com.hmst.pulsar.feature.radar.ui.component.TradingSessionView
import kotlinx.coroutines.launch

/** Stateful entry point used by navigation */
@Composable
fun RadarScreen(
    modifier: Modifier = Modifier,
    viewModel: RadarViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    RadarContent(
        uiState = uiState,
        modifier = modifier,
    )
}

/** Stateless UI: previewable without Hilt or network */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RadarContent(
    uiState: RadarUiState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                expandedHeight = 56.dp,
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            stringResource(R.string.radar_title),
                            style = PulsarTextStyle.Heading3,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Icon(
                            Icons.Filled.KeyboardArrowDown,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)

                        )
                    }
                },
                actions = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Phien lam viec
                        TradingSessionView(uiState.tradingSession)
                        // icon search: vùng chạm 44dp, icon 24dp ở giữa
                        IconButton(
                            onClick = {},
                            modifier = Modifier.size(44.dp),
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = stringResource(R.string.radar_search),
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // dong nhan dinh thi truong
            // chưa có VN-Index nào thì đang tải lần đầu; có rồi mà commentary null là thiếu dữ liệu, ẩn hẳn
            if (uiState.indices.isEmpty()) CommentarySkeleton()
            else uiState.commentary?.let { MarketOverView(it) }
            // vung thong tin index
            IndexMarketOverView(uiState)
        }
    }
}

@Composable
fun IndexMarketOverView(uiState: RadarUiState) {
    val pagerState = rememberPagerState(pageCount = { MarketIndexEnum.entries.size })
    val coroutineScope = rememberCoroutineScope()
    Column {
        PrimaryScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            edgePadding = 0.dp,
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
            indicator = {},
            divider = {},
            minTabWidth = 0.dp,
        ) {
            MarketIndexEnum.entries.forEach { tab ->
                FilterChip(
                    selected = pagerState.currentPage == tab.ordinal,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(tab.ordinal) }
                    },
                    label = {
                        Text(
                            text = tab.indexCode,
                            style = if (pagerState.currentPage == tab.ordinal) PulsarTextStyle.Label1Semibold else PulsarTextStyle.Label1Medium,
                            color = if (pagerState.currentPage == tab.ordinal) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    },
                    shape = RoundedCornerShape(22.dp),
                    border = null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(38.dp),
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) { page ->
            // thêm nhóm dẫn dắt 10 dòng thì vượt chiều cao màn hình, cần cuộn
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                val name = MarketIndexEnum.entries[page].name
                val index = uiState.indices[name]
                if (index == null) {
                    RadarPageSkeleton()
                    return@Column
                }
                MarketIndexCard(index = index, candles = uiState.candles[name].orEmpty())
                HorizontalDivider()
                MarketBreadthCard(
                    index = index,
                    showCounts = uiState.tradingSession != TradingSessionEnum.ATO,
                )
                HorizontalDivider()
                Row(
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    MarketLiquidity(modifier = Modifier.weight(1f))
                    VerticalDivider()
                    MarketHealth(modifier = Modifier.weight(1f))
                }
                HorizontalDivider()
                MarketLeaders(influences = uiState.influences[name].orEmpty())
            }
        }
    }
}

@Composable
private fun MarketOverView(status: String) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .fillMaxWidth()
    ) {
        Icon(Icons.AutoMirrored.Filled.ShowChart, contentDescription = null)
        Text(
            status,
            style = PulsarTextStyle.Body3Medium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun RadarContentPreview() {
    PulsarTheme {
        RadarContent(uiState = RadarUiState())
    }
}
