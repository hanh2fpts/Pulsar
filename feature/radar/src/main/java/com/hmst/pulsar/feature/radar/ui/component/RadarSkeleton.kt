package com.hmst.pulsar.feature.radar.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hmst.pulsar.core.designsystem.component.Shimmer
import com.hmst.pulsar.core.designsystem.component.SkeletonBlock

// Khung giữ chỗ khi Radar chưa có dữ liệu; kích thước bám theo layout thật để lúc có dữ liệu không nhảy

/** Giữ chỗ cho dòng nhận xét thị trường */
@Composable
fun CommentarySkeleton(modifier: Modifier = Modifier) {
    Shimmer(modifier = modifier.fillMaxWidth()) {
        SkeletonBlock(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(16.dp),
        )
    }
}

/** Giữ chỗ cho cả trang một chỉ số: chỉ số + chart, độ rộng, thanh khoản/sức khoẻ, nhóm dẫn dắt */
@Composable
fun RadarPageSkeleton(modifier: Modifier = Modifier) {
    // một Shimmer cho cả trang để dải sáng chạy liền mạch qua các phần
    Shimmer(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            IndexCardSkeleton()
            HorizontalDivider()
            BreadthSkeleton()
            HorizontalDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LiquiditySkeleton(Modifier.weight(1f))
                VerticalDivider()
                HealthSkeleton(Modifier.weight(1f))
            }
            HorizontalDivider()
            LeadersSkeleton()
        }
    }
}

@Composable
private fun IndexCardSkeleton() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SkeletonBlock(Modifier.size(width = 120.dp, height = 30.dp))
            SkeletonBlock(Modifier.size(width = 96.dp, height = 16.dp))
        }
        SkeletonBlock(
            Modifier
                .fillMaxWidth()
                .height(42.dp)
        )
    }
}

@Composable
private fun BreadthSkeleton() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SkeletonBlock(Modifier.size(width = 130.dp, height = 16.dp))
        SkeletonBlock(
            Modifier
                .fillMaxWidth()
                .height(12.dp),
            shape = RoundedCornerShape(6.dp),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            repeat(5) {
                SkeletonBlock(
                    Modifier
                        .weight(1f)
                        .height(26.dp),
                    shape = RoundedCornerShape(8.dp),
                )
            }
        }
    }
}

@Composable
private fun LiquiditySkeleton(modifier: Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SkeletonBlock(Modifier.size(width = 72.dp, height = 14.dp))
        SkeletonBlock(Modifier.size(width = 110.dp, height = 24.dp))
        SkeletonBlock(Modifier.size(width = 140.dp, height = 12.dp))
        SkeletonBlock(
            Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
    }
}

@Composable
private fun HealthSkeleton(modifier: Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        SkeletonBlock(Modifier.size(width = 120.dp, height = 14.dp))
        SkeletonBlock(
            Modifier
                .fillMaxWidth()
                .height(46.dp)
        )
        SkeletonBlock(Modifier.size(width = 70.dp, height = 14.dp))
        SkeletonBlock(Modifier.size(width = 100.dp, height = 12.dp))
    }
}

@Composable
private fun LeadersSkeleton() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SkeletonBlock(Modifier.size(width = 160.dp, height = 16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            LeaderColumnSkeleton(isUp = true, modifier = Modifier.weight(1f))
            LeaderColumnSkeleton(isUp = false, modifier = Modifier.weight(1f))
        }
    }
}

/** Cùng bố cục với LeaderRow: số | mã + thanh | logo, cột giảm đối xứng */
@Composable
private fun LeaderColumnSkeleton(isUp: Boolean, modifier: Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(10) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                if (isUp) SkeletonBlock(Modifier.size(width = 30.dp, height = 12.dp))
                else LogoSkeleton()
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = if (isUp) Alignment.End else Alignment.Start,
                ) {
                    SkeletonBlock(Modifier.size(width = 30.dp, height = 12.dp))
                    SkeletonBlock(
                        Modifier
                            .fillMaxWidth()
                            .height(6.dp),
                        shape = RoundedCornerShape(3.dp),
                    )
                }
                if (isUp) LogoSkeleton()
                else SkeletonBlock(Modifier.size(width = 30.dp, height = 12.dp))
            }
        }
    }
}

@Composable
private fun LogoSkeleton() {
    SkeletonBlock(Modifier.size(28.dp), shape = RoundedCornerShape(8.dp))
}

@Preview
@Composable
fun PreviewRadarSkeleton() {
    Surface {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            CommentarySkeleton()
            RadarPageSkeleton()
        }
    }
}
