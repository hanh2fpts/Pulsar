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
 * Line chart giá đóng cửa theo nến, không trục. Trục X là thứ tự nến chứ không phải
 * thời gian, để giờ nghỉ trưa không thành khoảng trống. Cần ít nhất 2 nến.
 */
@Composable
fun IndexLineChart(
    candles: List<Candle>,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val closes = remember(candles) { candles.map { it.close } }
    val model = remember(closes) {
        CartesianChartModel(LineCartesianLayerModel.build { series(closes) })
    }
    // co trục Y theo dữ liệu thay vì từ 0, nếu không đường gần như phẳng
    val minY = closes.min()
    val maxY = closes.max().let { if (it == minY) it + 1 else it }
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
                rangeProvider = CartesianLayerRangeProvider.fixed(minY = minY, maxY = maxY),
            ),
        ),
        model = model,
        modifier = modifier,
        scrollState = rememberVicoScrollState(scrollEnabled = false),
        zoomState = rememberVicoZoomState(zoomEnabled = false, initialZoom = Zoom.Content),
    )
}
