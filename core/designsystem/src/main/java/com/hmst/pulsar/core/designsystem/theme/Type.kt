package com.hmst.pulsar.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hmst.pulsar.core.designsystem.R

val BeVietnamPro = FontFamily(
    Font(R.font.be_vietnam_pro_regular, FontWeight.Normal),
    Font(R.font.be_vietnam_pro_medium, FontWeight.Medium),
    Font(R.font.be_vietnam_pro_semibold, FontWeight.SemiBold),
    Font(R.font.be_vietnam_pro_bold, FontWeight.Bold),
)

private fun style(size: Int, line: Int, weight: FontWeight, tabular: Boolean = false) = TextStyle(
    fontFamily = BeVietnamPro,
    fontWeight = weight,
    fontSize = size.sp,
    lineHeight = line.sp,
    letterSpacing = 0.sp,
    fontFeatureSettings = if (tabular) "tnum" else null,
)

// Áp phông cho các style không tự khai báo, tránh lẫn FontFamily.Default
private fun Typography.withFamily(f: FontFamily) = copy(
    displayLarge = displayLarge.copy(fontFamily = f),
    displayMedium = displayMedium.copy(fontFamily = f),
    displaySmall = displaySmall.copy(fontFamily = f),
    headlineLarge = headlineLarge.copy(fontFamily = f),
    headlineMedium = headlineMedium.copy(fontFamily = f),
    headlineSmall = headlineSmall.copy(fontFamily = f),
    titleLarge = titleLarge.copy(fontFamily = f),
    titleMedium = titleMedium.copy(fontFamily = f),
    titleSmall = titleSmall.copy(fontFamily = f),
    bodyLarge = bodyLarge.copy(fontFamily = f),
    bodyMedium = bodyMedium.copy(fontFamily = f),
    bodySmall = bodySmall.copy(fontFamily = f),
    labelLarge = labelLarge.copy(fontFamily = f),
    labelMedium = labelMedium.copy(fontFamily = f),
    labelSmall = labelSmall.copy(fontFamily = f),
)

val PulsarTypography = Typography(
    displayMedium = style(32, 40, FontWeight.Bold),
    titleLarge = style(22, 28, FontWeight.Bold),
    titleMedium = style(18, 24, FontWeight.SemiBold),
    bodyLarge = style(15, 22, FontWeight.Normal),
    bodyMedium = style(13, 18, FontWeight.Normal),
    labelLarge = style(15, 20, FontWeight.SemiBold),   // chữ trên nút
    labelMedium = style(12, 16, FontWeight.Medium),
    labelSmall = style(12, 16, FontWeight.Medium),     // nhỏ nhất 12sp
).withFamily(BeVietnamPro)

/** Số liệu giá: chữ số đều bề rộng để cột không nhảy khi giá đổi. */
object PulsarNumbers {
    val Large = style(28, 32, FontWeight.Bold, tabular = true)
    val Row = style(15, 20, FontWeight.SemiBold, tabular = true)
    val Chip = style(12, 16, FontWeight.SemiBold, tabular = true)
}