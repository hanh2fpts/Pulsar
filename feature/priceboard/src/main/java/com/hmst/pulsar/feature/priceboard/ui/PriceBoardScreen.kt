package com.hmst.pulsar.feature.priceboard.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ChipColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hmst.pulsar.core.designsystem.theme.PulsarTextStyle
import com.hmst.pulsar.core.designsystem.theme.PulsarTheme
import com.hmst.pulsar.core.designsystem.theme.market
import com.hmst.pulsar.feature.priceboard.R
import com.hmst.pulsar.feature.priceboard.ui.PriceBoardUiState.Content
import com.hmst.pulsar.feature.priceboard.ui.component.PillSegmentedControl
import com.hmst.pulsar.feature.priceboard.ui.component.WatchList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/** Stateful entry point used by navigation */
@Composable
fun PriceBoardScreen(
    modifier: Modifier = Modifier,
    viewModel: PriceBoardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PriceBoardContent(
        uiState = uiState,
        modifier = modifier,
    )
}

/** Stateless UI: previewable without Hilt or network */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceBoardContent(
    uiState: PriceBoardUiState,
    modifier: Modifier = Modifier,
) {
    val searchBarState = rememberSearchBarState()
    val textFieldState = rememberTextFieldState()
    val scope = rememberCoroutineScope()
    val inputField =
        @Composable {
            SearchBarDefaults.InputField(
                textFieldState = textFieldState,
                searchBarState = searchBarState,
                onSearch = { scope.launch { searchBarState.animateToCollapsed() } },
                placeholder = {
                    Text(
                        modifier = Modifier.clearAndSetSemantics {},
                        text = "Tìm mã hoặc tên công ty",
                        style = PulsarTextStyle.Body1,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                leadingIcon = { SampleLeadingIcon(searchBarState, scope) },
                modifier = Modifier.fillMaxWidth(),
            )
        }

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
                            stringResource(R.string.priceboard_title),
                            style = PulsarTextStyle.Heading3,
                            color = MaterialTheme.colorScheme.onSurface
                        )
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
            SearchBar(
                state = searchBarState,
                inputField = inputField,
                modifier = Modifier.border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = SearchBarDefaults.inputFieldShape,
                ),
            )
            ExpandedFullScreenSearchBar(state = searchBarState, inputField = inputField) {
                SampleSearchResults(
                    onResultClick = { result ->
                        textFieldState.setTextAndPlaceCursorAtEnd(result)
                        scope.launch { searchBarState.animateToCollapsed() }
                    }
                )
            }
            var selectedTab by rememberSaveable { mutableIntStateOf(0) }
            PillSegmentedControl(
                options = TABS,
                selectedIndex = selectedTab,
                onSelect = { selectedTab = it },
            )
            // Danh sach danh muc theo doi
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = true,
                        modifier = Modifier.height(46.dp),
                        onClick = {},
                        label = {
                            Text(
                                text = "Theo dõi",
                                style = PulsarTextStyle.Label1Semibold,
                            )
                        },
                        shape = RoundedCornerShape(22.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = true,
                            borderColor = MaterialTheme.colorScheme.surfaceVariant,
                            selectedBorderColor = MaterialTheme.colorScheme.primary,
                            borderWidth = 1.dp,
                            selectedBorderWidth = 1.dp
                        )
                    )
                    FilterChip(
                        selected = true,
                        onClick = {},
                        modifier = Modifier.height(46.dp),
                        label = {
                            Text(
                                text = "Cổ tức",
                                style = PulsarTextStyle.Label1Semibold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        },
                        shape = RoundedCornerShape(22.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = false,
                            borderColor = MaterialTheme.colorScheme.surfaceVariant,
                            selectedBorderColor = MaterialTheme.colorScheme.primary,
                            borderWidth = 1.dp,
                            selectedBorderWidth = 1.dp
                        )
                    )
                }
                IconButton(
                    onClick = { /* Xử lý thêm danh mục */ },
                    modifier = Modifier.size(44.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Thêm danh mục",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            // LazyColumn không weight sẽ ăn hết chiều cao còn lại, đẩy các view phía sau ra ngoài
            WatchList(modifier = Modifier.weight(1f))
            Text(
                "Giữ để sắp xếp, vuốt sang trái để xoá khỏi nhóm",
                style = PulsarTextStyle.Caption1,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SampleLeadingIcon(searchBarState: SearchBarState, scope: CoroutineScope) =
    if (searchBarState.currentValue == SearchBarValue.Expanded) {
        TooltipBox(
            positionProvider =
                TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
            tooltip = {
                PlainTooltip(
                    modifier =
                        Modifier.semantics {
                            // TODO(b/496338253): Remove this modifier once bug where tooltip text
                            //  is not announced by a11y screen readers is resolved.
                            liveRegion = LiveRegionMode.Assertive
                            paneTitle = "Back"
                        }
                ) {
                    Text("Back")
                }
            },
            state = rememberTooltipState(),
        ) {
            IconButton(onClick = { scope.launch { searchBarState.animateToCollapsed() } }) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
            }
        }
    } else {
        Icon(Icons.Default.Search, contentDescription = null)
    }

@Composable
private fun SampleSearchResults(onResultClick: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.verticalScroll(rememberScrollState())) {
        repeat(10) { idx ->
            val resultText = "Suggestion $idx"
            ListItem(
                headlineContent = { Text(resultText) },
                supportingContent = { Text("Additional info") },
                leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                modifier =
                    Modifier
                        .clickable { onResultClick(resultText) }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
            )
        }
    }
}

private val TABS = listOf("Danh mục của tôi", "Bảng giá chỉ số")

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PriceBoardContentPreview() {
    PulsarTheme() {
        PriceBoardContent(uiState = PriceBoardUiState())
    }
}
