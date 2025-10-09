package com.nb.coininfo.ui.screens.coindetails

import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LineStyle
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.yml.charts.ui.linechart.model.LinePlotData
import com.nb.coininfo.data.models.CoinGraphData

@Composable
fun PriceHistoryChart(historicalData: List<CoinGraphData?>?) {
    historicalData ?: return
    // 1. Convert your OhlcData list into a list of Point objects
    val points = historicalData.mapIndexed { index, data ->
        Point(x = index.toFloat(), y = data?.close?.toFloat() ?: 0f)
    }

    // 2. Configure the X and Y axes
    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(historicalData.size - 1)
        //.startAtZero(false)
        .labelData { i -> i.toString() } // You can format dates here
        .build()

    val yAxisData = AxisData.Builder()
        .steps(5)
        .labelData { i -> i.toString()/*String.format("%.2f", i)*/ } // Format price labels
        .build()

    // 3. Create the LineChartData object
    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = points,
                    lineStyle = LineStyle(color = Color(0xFF00C7B1)), // Using your theme's accent color
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = Color.Transparent
    )

    // 4. Display the chart 📈
    LineChart(
        modifier = Modifier.height(250.dp),
        lineChartData = lineChartData
    )
}