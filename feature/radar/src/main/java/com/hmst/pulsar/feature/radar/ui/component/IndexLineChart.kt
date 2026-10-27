package com.hmst.pulsar.feature.radar.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.hmst.pulsar.core.network.model.Candle
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.Zoom
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.compose.cartesian.data.LineCartesianLayerModel
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.Fill

/**
 * Line chart giá đóng cửa nến 5 phút, không trục.
 *
 * Trục X là ô 5 phút trong phiên (9:00–11:30, 13:00–15:00, bỏ nghỉ trưa), bắt đầu từ nến
 * đầu tiên và kéo tới hết phiên, nên đường sát mép trái rồi chạy dần sang phải thay vì bị kéo giãn.
 *
 * @param points kết quả [toChartPoints], cần ít nhất 2 điểm
 */
@Composable
fun IndexLineChart(
    points: Map<Int, Double>,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val model = remember(points) {
        CartesianChartModel(LineCartesianLayerModel.build { series(points.keys, points.values) })
    }
    // co trục Y theo dữ liệu thay vì từ 0, nếu không đường gần như phẳng
    val minY = points.values.min()
    val maxY = points.values.max()
    // đường ngang thì nới hai phía để nằm giữa chart thay vì sát đáy
    val pad = if (maxY == minY) 1.0 else 0.0
    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider = LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.rememberLine(
                        fill = LineCartesianLayer.LineFill.single(Fill(color)),
                        areaFill = LineCartesianLayer.AreaFill.single(
                            Fill(Brush.verticalGradient(listOf(color.copy(alpha = 0.3f), Color.Transparent)))
                        ),
                    )
                ),
                rangeProvider = CartesianLayerRangeProvider.fixed(
                    // từ nến đầu chứ không từ 9:00, để ATO (chưa có nến) không thành khoảng trống bên trái
                    minX = points.keys.first().toDouble(),
                    maxX = SESSION_SLOTS - 1.0,
                    minY = minY - pad,
                    maxY = maxY + pad,
                ),
            ),
            getXStep = { _, _, _ -> 1.0 },
        ),
        model = model,
        modifier = modifier,
        scrollState = rememberVicoScrollState(scrollEnabled = false),
        zoomState = rememberVicoZoomState(zoomEnabled = false, initialZoom = Zoom.Content),
    )
}

/** Đường ngang phủ cả phiên tại [value], dùng đầu phiên khi chưa đủ nến */
fun flatChartPoints(value: Double): Map<Int, Double> = mapOf(0 to value, SESSION_SLOTS - 1 to value)

/** Ô trong phiên -> giá đóng cửa, tăng dần theo ô. Nến ngoài giờ khớp lệnh bị bỏ, trùng ô thì lấy nến sau */
fun List<Candle>.toChartPoints(): Map<Int, Double> =
    mapNotNull { c -> sessionSlot(c.time)?.let { it to c.close } }.toMap().toSortedMap()

/** Ô 5 phút trong phiên theo giờ VN (UTC+7), null nếu ngoài giờ khớp lệnh */
private fun sessionSlot(epochSecond: Long): Int? {
    val minute = (Math.floorMod(epochSecond + 7 * 3600, 86_400L) / 60).toInt()
    return when (minute) {
        in MORNING_START until MORNING_END -> (minute - MORNING_START) / 5
        in AFTERNOON_START until AFTERNOON_END ->
            (MORNING_END - MORNING_START) / 5 + (minute - AFTERNOON_START) / 5
        else -> null
    }
}

private const val MORNING_START = 9 * 60
private const val MORNING_END = 11 * 60 + 30
private const val AFTERNOON_START = 13 * 60
private const val AFTERNOON_END = 15 * 60

/** 30 ô buổi sáng + 24 ô buổi chiều */
private const val SESSION_SLOTS =
    (MORNING_END - MORNING_START + AFTERNOON_END - AFTERNOON_START) / 5
