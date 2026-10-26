package com.hmst.pulsar.core.designsystem.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp

/**
 * Phủ dải sáng chạy ngang lên mọi pixel của [content] (BlendMode.SrcIn): dựng khung bằng các
 * [SkeletonBlock], cả khung lấp lánh đồng bộ chỉ với một animation.
 */
@Composable
fun Shimmer(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val base = MaterialTheme.colorScheme.surfaceVariant
    // nền sáng thì dải sáng gần trắng, nền tối chỉ nhích nhẹ để không chói
    val isLight = MaterialTheme.colorScheme.surface.luminance() > 0.5f
    val highlight = lerp(base, Color.White, if (isLight) 0.6f else 0.08f)
    val progress by rememberInfiniteTransition(label = "shimmer").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing)),
        label = "shimmer",
    )
    Box(
        modifier = modifier
            // SrcIn cần lớp riêng, nếu không sẽ hoà cả nền phía sau
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithContent {
                drawContent()
                val band = size.width * 0.4f
                // đọc progress trong lúc vẽ nên mỗi khung chỉ vẽ lại, không recompose
                val x = -band + (size.width + band) * progress
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(base, highlight, base),
                        start = Offset(x, 0f),
                        end = Offset(x + band, 0f),
                    ),
                    blendMode = BlendMode.SrcIn,
                )
            }
    ) {
        content()
    }
}

/** Một khối giữ chỗ trong [Shimmer]; kích thước truyền qua [modifier] */
@Composable
fun SkeletonBlock(modifier: Modifier = Modifier, shape: Shape = RoundedCornerShape(4.dp)) {
    Box(modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant, shape))
}
