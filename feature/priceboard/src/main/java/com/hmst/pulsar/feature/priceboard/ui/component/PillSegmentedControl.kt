package com.hmst.pulsar.feature.priceboard.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hmst.pulsar.core.designsystem.theme.PulsarTextStyle

/**
 * Thanh chọn dạng viên thuốc: nền xám bo tròn, ô đang chọn là khối đậm lọt bên trong.
 * Các ô chia đều chiều ngang.
 */
@Composable
fun PillSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(22.dp))
            .padding(4.dp),
    ) {
        options.forEachIndexed { i, label ->
            val selected = i == selectedIndex
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (selected) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .clickable(role = Role.Tab) { onSelect(i) },
            ) {
                Text(
                    label,
                    style = if (selected) PulsarTextStyle.Label1Semibold else PulsarTextStyle.Label1Medium,
                    color = if (selected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                )
            }
        }
    }
}

@Preview
@Composable
fun PillSegmentedControlPreview() {
    Surface {
        PillSegmentedControl(
            options = listOf("Danh mục của tôi", "Bảng giá chỉ số"),
            selectedIndex = 0,
            onSelect = {},
        )
    }
}
