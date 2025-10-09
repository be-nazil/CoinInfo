package com.nb.coininfo.ui.screens.coindetails

import android.R.attr.textSize
import android.graphics.Paint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nb.coininfo.data.models.CoinGraphData
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShader
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asComposePaint
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nb.coininfo.R
import com.nb.coininfo.data.models.CoinDetailsEntity
import com.nb.coininfo.data.models.CoinEntity
import com.nb.coininfo.data.models.MarketChartResponse
import com.nb.coininfo.ui.theme.AccentCyan
import com.nb.coininfo.ui.theme.Green10
import com.nb.coininfo.ui.theme.PurpleGrey40
import com.nb.coininfo.ui.theme.ScreenBackground
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs


@Composable
fun LineChartCustom(
    data: List<Pair<Double, Double>>,
    modifier: Modifier = Modifier,
    lineColor: Color = AccentCyan,
    expand: (List<Pair<Double, Double>>) -> Unit
) {
    if (data.isEmpty()) return
    val touchableRadius: Dp = 18.dp

    var selectedDataPoint by remember(data) {
        mutableStateOf<Pair<Double, Double>?>(null)
    }
    val density = LocalDensity.current
    val touchableRadiusPx = with(density) { touchableRadius.toPx() }

    val textPaint = remember {
        Paint().apply {
            isAntiAlias = true
            textSize = 40f
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
        }
    }

    val xMin = data.minOf { it.first }
    val xMax = data.maxOf { it.first }
    val yMin = data.minOf { it.second }
    val yMax = data.maxOf { it.second }

    Box(Modifier
        .height(300.dp)
        .border(width = 1.dp, AccentCyan, shape = RoundedCornerShape(12.dp))) {

        Canvas(
            modifier = modifier
                .fillMaxSize()
                .padding(4.dp)
                .pointerInput(data) {
                    detectTapGestures { tapOffset ->
                        val paddingLeft = 0.dp.toPx()
                        val paddingRight = 0.dp.toPx()
                        val paddingTop = 20.dp.toPx()
                        val paddingBottom = 10.dp.toPx()
                        val drawableWidth = size.width - paddingLeft - paddingRight
                        val drawableHeight = size.height - paddingTop - paddingBottom

                        // Convert data to canvas points
                        val canvasPoints = data.map {
                            val x = paddingLeft + ((it.first - xMin) / (xMax - xMin) * drawableWidth)
                            val y = size.height - paddingBottom - ((it.second - yMin) / (yMax - yMin) * drawableHeight)
                            Offset(x.toFloat(), y.toFloat()) to it
                        }

                        // Find the closest point to the tap location
                        val closestPoint = canvasPoints.minByOrNull { (point, _) ->
                            abs(point.x - tapOffset.x)
                        }

                        selectedDataPoint =
                            if (closestPoint != null && (closestPoint.first - tapOffset).getDistance() < touchableRadiusPx) {
                                closestPoint.second
                            } else {
                                null
                            }
                    }
                }
        ) {
            val paddingLeft = 0.dp.toPx()
            val paddingRight = 0.dp.toPx()
            val paddingTop = 20.dp.toPx()
            val paddingBottom = 10.dp.toPx()

            val drawableWidth = size.width - paddingLeft - paddingRight
            val drawableHeight = size.height - paddingTop - paddingBottom

            val points = data.map {
                val x = paddingLeft + ((it.first - xMin) / (xMax - xMin) * drawableWidth)
                val y = size.height - paddingBottom - ((it.second - yMin) / (yMax - yMin) * drawableHeight)
                Offset(x.toFloat(), y.toFloat())
            }

            val linePath = Path().apply {
                if (points.isNotEmpty()) {
                    moveTo(points.first().x, points.first().y)
                    for (i in 0 until points.size - 1) {
                        val currentPoint = points[i]
                        val nextPoint = points[i+1]
                        val controlPoint1 = Offset((currentPoint.x + nextPoint.x) / 2f, currentPoint.y)
                        val controlPoint2 = Offset((currentPoint.x + nextPoint.x) / 2f, nextPoint.y)
                        cubicTo(
                            controlPoint1.x, controlPoint1.y,
                            controlPoint2.x, controlPoint2.y,
                            nextPoint.x, nextPoint.y
                        )
                    }
                }
            }

            drawPath(
                path = linePath,
                color = lineColor,
                style = Stroke(width = 6f, cap = StrokeCap.Round)
            )

            val fillPath = Path().apply {
                addPath(linePath)
                lineTo(points.last().x, size.height - paddingBottom)
                lineTo(points.first().x, size.height - paddingBottom)
                close()
            }

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        lineColor.copy(alpha = 0.4f),
                        Color.Transparent
                    ),
                    endY = size.height - paddingBottom
                )
            )

            selectedDataPoint?.let { dataPoint ->
                val selectedIndex = data.indexOf(dataPoint)
                if (selectedIndex != -1) {
                    val pointToHighlight = points[selectedIndex]
                    drawCircle(color = Color.White, radius = 10f, center = pointToHighlight)
                    drawCircle(color = lineColor, radius = 6f, center = pointToHighlight)

                    val textToDraw = "%.1f".format(dataPoint.second)
                    val textYPosition = pointToHighlight.y - 25f

                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            textToDraw,
                            pointToHighlight.x,
                            textYPosition,
                            textPaint
                        )
                    }
                }
            }
        }


        IconButton(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.TopEnd),
            onClick = { expand.invoke(data) }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_fullscreen_exit), contentDescription = "",
                tint = AccentCyan
            )
        }

    }

}


@Composable
fun CoinPriceGraph(data: List<Pair<Double, Double>>) {
    data ?: return
    // Vico: 1. Create a ChartEntryModelProducer
    val modelProducer = remember { ChartEntryModelProducer() }

    // Vico: 2. Convert your data into a list of `ChartEntry`
    val datasetForModel = remember(data) {
        data.mapIndexed { index, historicalData ->
            FloatEntry(
                x = index.toFloat(),
                y = historicalData?.second?.toFloat() ?: 0f
            )
        }
    }

    // Vico: 3. Update the model producer with the new dataset
    modelProducer.setEntries(datasetForModel)

    val xMin = data.minOf { it.first }
    val xMax = data.maxOf { it.first }
    val yMin = data.minOf { it.second }
    val yMax = data.maxOf { it.second }

    val timeRange = xMax - xMin
    val oneDayInMillis = 24 * 60 * 60 * 1000
    val simpleDateFormat = remember(timeRange) {
        if (timeRange > oneDayInMillis * 2) {
            SimpleDateFormat("MMM dd", Locale.getDefault())
        } else {
            SimpleDateFormat("HH:mm", Locale.getDefault())
        }
    }
    val bottomAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
            val index = value.toInt()
            if (index in data.indices) {
                simpleDateFormat.format(Date(data[index].first?.toLong() ?: 0L))
            } else {
                ""
            }
        }

    Card(
        shape = RoundedCornerShape(size = 12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Price History (Last 14 Days)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Vico: 4. Create and display the chart
            ProvideChartStyle {
                Chart(
                    chart = lineChart(
                        lines = listOf(
                            LineChart.LineSpec(
                                lineColor = MaterialTheme.colorScheme.primary.hashCode(),
                                lineBackgroundShader = verticalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0f)
                                    )
                                ) as DynamicShader,
                            )
                        )
                    ),
                    chartModelProducer = modelProducer,
                    startAxis = rememberStartAxis(
                        valueFormatter = { value, _ ->
                            "$${value.roundToInt()}"
                        }
                    ),
                    bottomAxis = rememberBottomAxis(
                        valueFormatter = bottomAxisValueFormatter,
                        guideline = null
                    ),
                    modifier = Modifier.height(200.dp)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenLineChartScreen(
    coinName: String? = null,
    data: List<Pair<Double, Double>>,
    modifier: Modifier = Modifier,
    lineColor: Color = AccentCyan,
    onNavigateUp: () -> Unit
) {
    if (data.isEmpty()) return

    val context = LocalContext.current
    val textPaint = remember {
        Paint().apply {
            color = lineColor.copy(alpha = 0.8f).toArgb()
            textAlign = Paint.Align.CENTER
            textSize = context.resources.displayMetrics.density * 12f // 12sp
        }
    }

    val xMin = data.minOf { it.first }
    val xMax = data.maxOf { it.first }
    val yMin = data.minOf { it.second }
    val yMax = data.maxOf { it.second }

    val timeRange = xMax - xMin
    val oneDayInMillis = 24 * 60 * 60 * 1000
    val simpleDateFormat = remember(timeRange) {
        if (timeRange > oneDayInMillis * 2) {
            SimpleDateFormat("MMM dd", Locale.getDefault())
        } else {
            SimpleDateFormat("HH:mm", Locale.getDefault())
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text( coinName ?: "NA") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ScreenBackground,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = ScreenBackground
    ) { paddingValues ->

        Canvas(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .height(300.dp)
        ) {
            val paddingLeft = 50.dp.toPx()
            val paddingRight = 10.dp.toPx()
            val paddingTop = 30.dp.toPx()
            val paddingBottom = 50.dp.toPx()

            val drawableWidth = size.width - paddingLeft - paddingRight
            val drawableHeight = size.height - paddingTop - paddingBottom

            val yLabelCount = 5
            val yRange = yMax - yMin
            val yInterval = if (yRange == 0.0) 0.0 else yRange / (yLabelCount - 1)

            for (i in 0 until yLabelCount) {
                val yValue = yMin + (i * yInterval)
                val yPosOnCanvas =
                    size.height - paddingBottom - (i * drawableHeight / (yLabelCount - 1))

                val textX = paddingLeft - 8.dp.toPx()
                val textY = yPosOnCanvas + textPaint.textSize / 2

                drawContext.canvas.nativeCanvas.apply {
                    save()
                    rotate(-45f, textX, textY)
                    drawText(
                        "$${yValue.roundToInt()}",
                        textX,
                        textY,
                        textPaint.apply { textAlign = Paint.Align.RIGHT }
                    )
                    restore()
                }
            }

            // --- DRAW SMARTER X-AXIS LABELS ---
            val xLabelCount = 4
            val labelIndices = (0 until xLabelCount).map {
                (it * (data.size - 1) / (xLabelCount - 1)).coerceIn(0, data.size - 1)
            }

            labelIndices.forEach { index ->
                val point = data[index]
                val xValue = point.first
                val xPos = paddingLeft + ((xValue - xMin) / (xMax - xMin) * drawableWidth)

                drawContext.canvas.nativeCanvas.drawText(
                    simpleDateFormat.format(Date(xValue.toLong())),
                    xPos.toFloat(),
                    size.height - paddingBottom + textPaint.textSize + 10.dp.toPx() / 2,
                    textPaint.apply { textAlign = Paint.Align.CENTER }
                )
            }

            // --- CONVERT DATA TO SCALED POINTS ---
            val points = data.map {
                val x = paddingLeft + ((it.first - xMin) / (xMax - xMin) * drawableWidth)
                val y =
                    size.height - paddingBottom - ((it.second - yMin) / (yMax - yMin) * drawableHeight)
                Offset(x.toFloat(), y.toFloat())
            }

            // --- DRAW THE SMOOTH, CURVED LINE ---
            val linePath = Path().apply {
                if (points.isNotEmpty()) {
                    moveTo(points.first().x, points.first().y)
                    for (i in 0 until points.size - 1) {
                        val currentPoint = points[i]
                        val nextPoint = points[i + 1]
                        val controlPoint1 =
                            Offset((currentPoint.x + nextPoint.x) / 2f, currentPoint.y)
                        val controlPoint2 = Offset((currentPoint.x + nextPoint.x) / 2f, nextPoint.y)
                        cubicTo(
                            controlPoint1.x, controlPoint1.y,
                            controlPoint2.x, controlPoint2.y,
                            nextPoint.x, nextPoint.y
                        )
                    }
                }
            }

            drawPath(
                path = linePath,
                color = lineColor,
                style = Stroke(width = 6f, cap = StrokeCap.Round)
            )

            // --- DRAW THE GRADIENT FILL ---
            val fillPath = Path().apply {
                addPath(linePath)
                lineTo(points.last().x, size.height - paddingBottom)
                lineTo(points.first().x, size.height - paddingBottom)
                close()
            }

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        lineColor.copy(alpha = 0.4f),
                        Color.Transparent
                    ),
                    endY = size.height - paddingBottom
                )
            )
        }
    }
}

@Preview
@Composable
private fun GraphPrev() {
    val chartData : MarketChartResponse = Gson().fromJson(
        """{"prices":[[1758915480620,109195.08820218979],[1758915761559,109128.38013801574],[1758915952387,109130.52548280876],[1758916292714,109191.77134391792],[1758916593065,109258.92293994574],[1758917331791,109256.46837195756],[1758917563157,109268.84084739523],[1758917760615,109329.72071838507],[1758918120903,109173.10640316052],[1758918453241,109168.74307904595],[1758918623919,109196.39555638835],[1758919041557,109260.44345354667],[1758919297149,109243.06208024379],[1758919581441,109232.01948349147],[1758919826585,109260.62948942094],[1758920200836,109350.77400822885],[1758920515438,109302.71576792157],[1758920744631,109260.27265551806],[1758921111839,109260.94999566967],[1758921461638,109402.66061676435],[1758921772588,109391.5420096433],[1758922102386,109478.19451204938],[1758922260610,109498.67299539295],[1758922514161,109476.9400990876],[1758922852013,109470.66774164973],[1758923221584,109378.67530039033],[1758923460605,109415.58066011292],[1758923711962,109475.27268853989],[1758924173949,109520.71047796034],[1758924492444,109411.6901232317],[1758924795384,109544.11135486788],[1758924953785,109562.01015309541],[1758925260661,109574.49308284483],[1758925561432,109536.81509154267],[1758925861465,109546.70048046444],[1758926160938,109558.7526758667],[1758926440601,109594.31711732272],[1758926841920,109607.67495037409],[1758927044102,109638.7646290477],[1758927414290,109613.47250213713],[1758927622079,109611.1798647101],[1758927921499,109548.38129650301],[1758928243879,109535.51614106324],[1758928522254,109532.93650630425],[1758928821881,109507.06926328785],[1758929124157,109586.29266122673],[1758929460730,109600.68016641174],[1758929821502,109686.23867595464],[1758930120585,109701.96484440796],[1758930326721,109701.79195595336],[1758930635479,109706.53724818886],[1758931001933,109710.20874667744],[1758931714100,109660.37389675],[1758931872149,109655.30373573788],[1758932242416,109661.70630567754],[1758932551825,109668.4401484297],[1758932881910,109614.09637923715],[1758933073396,109637.24136130272],[1758933374732,109572.47689621834],[1758933603953,109559.49274926865],[1758933972093,109535.49518976788],[1758934321735,109561.05273061509],[1758934621419,109631.16147182384],[1758934835195,109670.9711899422],[1758935101687,109675.90598450796],[1758935593350,109597.87029424097],[1758935924918,109533.57003753592],[1758936055801,109542.4444677097],[1758936393814,109508.99968198543],[1758936705790,109546.70208457949],[1758936900794,109478.96849271577],[1758937231503,109521.72269692476],[1758937592292,109383.34298689416],[1758937814619,109409.47730461975],[1758938190744,109451.55273652473],[1758938441448,109468.93225169746],[1758938792120,109527.63304050917],[1758939161326,109452.58068195607],[1758939493319,109478.45971947041],[1758939621587,109423.97415562617],[1758939961460,109378.65173780291],[1758940330839,109392.19691125276],[1758940682475,109472.7010805955],[1758940851910,109489.17294206005],[1758941187723,109480.7241295551],[1758941424332,109464.3698898585],[1758941723899,109460.0404524421],[1758942051332,109445.90730953793],[1758942335197,109505.774997863],[1758942636224,109491.87537548937],[1758943054330,109501.49387850486],[1758943381364,109541.2461870314],[1758943542601,109548.36803862281],[1758943861130,109593.93982954783],[1758944126468,109647.99222448745],[1758944462618,109746.18227996337],[1758944787299,109684.53131579614],[1758945141816,109654.8116031144],[1758945451794,109602.17944296282],[1758945760858,109609.75779282032],[1758946044313,109635.3615434116],[1758946362835,109603.29031749317],[1758946572037,109597.13290637125],[1758946912428,109630.47494212756],[1758947281839,109636.2262791756],[1758947481941,109721.18062718918],[1758947831684,109721.27728705682],[1758948182554,109675.73061630421],[1758948332306,109677.86402850325],[1758948696943,109695.05298980747],[1758949040652,109701.63953102907],[1758949381358,109695.63165561004],[1758949581540,109678.54230262176],[1758949920809,109660.27412584446],
            [1758950271511,109679.04208626438],[1758950580791,109678.09962769799],[1758950882104,109647.64862884603],[1758951162458,109658.74994996854],[1758951473262,109694.67453936314],[1758951764280,109725.40953164673],[1758952075322,109701.34127858176],[1758952362527,109672.49923787659],[1758952631820,109686.67328894763],[1758952842818,109681.99331101133],[1758953220607,109667.09960776933],[1758953444208,109617.21641829569],[1758953821809,109623.96895266244],[1758954181260,109599.65859549672],[1758954362173,109576.84406229787],[1758954660889,109591.78227169905],[1758954950824,109575.44009588378],[1758955281851,109574.44150652563],[1758955615746,109534.59283622241],[1758955814438,109556.51286550249],[1758956193185,109569.72107086328],[1758956581576,109600.45255118015],[1758956844135,109599.4271168652],[1758957112307,109587.71777027033],[1758957312950,109603.33926962626],[1758957672131,109613.7367036871],[1758958102100,109563.85981947169],[1758958320957,109544.14970567226],[1758958672774,109479.41015263468],[1758958826006,109473.37672222566],[1758959240924,109409.60460110933],[1758959568188,109426.45989608808],[1758959820685,109450.036932864],[1758960182015,109422.90265091351],[1758960364087,109417.04328746109],[1758960720936,109327.58314265843],[1758960932238,109322.53841214861],[1758961351151,109344.92355517542],[1758961502615,109365.54734609276],[1758961872915,109399.19826260074],[1758962204492,109378.07511999785],[1758962481832,109357.64216998174],[1758962842940,109357.57377779289],[1758963053982,109345.38511708262],[1758963382791,109352.39355701752],[1758963823166,109313.46429620442],[1758964020655,109259.58493114964],[1758964345609,109228.81066569549],[1758964570548,109214.18094931878],[1758964874910,109197.71407724179],[1758965121822,109193.34338654765],[1758965532244,109158.55837767913],[1758965864134,109247.2408379113],[1758966061427,109295.28984782551],[1758966383605,109312.81566792038],[1758966743289,109310.06549386562],[1758966922351,109346.67565617636],[1758967332130,109329.99376052197],[1758967663014,109333.6544756636],[1758967920660,109327.96596201943],[1758968271357,109319.26182702389],[1758968561656,109333.93753505658],[1758968721761,109325.60412831421],[1758969151006,109334.17471426798],[1758969495541,109415.74946094495],[1758969660746,109401.88544352289],[1758970062502,109456.25145230997],[1758970231929,109451.43360176987],[1758970620717,109465.73666021855],[1758970837235,109476.54927693262],[1758971220562,109434.39840262328],[1758971423968,109401.59488036364],[1758971842653,109356.05229420145],[1758972025201,109347.90978318252],[1758972394721,109344.56568509489],[1758972762558,109359.44686723303],[1758972942891,109377.33771318535],[1758973250705,109352.10315055757],[1758973544842,109345.03142884623],[1758973832630,109339.33344451626],[1758974160622,109334.00286036594],[1758974521288,109342.64744553846],[1758974782577,109336.74219306372],[1758975100988,109305.74392558102],[1758975353652,109315.7206303804],[1758975731045,109309.97018351361],[1758976042588,109295.07643742723],[1758976292551,109301.58419991078],[1758976620560,109283.09003600733],[1758976826289,109339.9453184747],[1758977181656,109437.20002955086],[1758977581529,109424.71721232367],[1758977780864,109406.72883075265],[1758978173616,109431.9900852975],[1758978371388,109419.70988071398],[1758978734245,109397.84109932075],[1758978954102,109352.29806772398],[1758979321150,109392.44445931537],[1758979691297,109403.218266457],[1758979886991,109396.29208951659],[1758980176986,109361.69012711667],[1758980408097,109393.30675471986],[1758980772580,109384.87016515847],[1758981201082,109376.9805940801],[1758981462629,109363.34199051761],[1758981685335,109350.69171430042],[1758981984851,109369.79441539408],[1758982361239,109381.83242509364],[1758982661617,109366.90375691591],[1758982843608,109354.496513024],[1758983112017,109363.12671278049],[1758983561507,109360.47612600232],[1758983820771,109375.98993765381],[1758984042531,109373.98142595241],[1758984300722,109350.17852072635],[1758984713782,109310.10780130564],
[1758984922261,109321.29668787651],[1758985364906,109303.9057184713],[1758985633965,109281.92037829466],[1758985939244,109276.44270600633],[1758986220655,109294.91869450161],[1758986481011,109306.27432855577],[1758986723907,109295.58091810952],[1758987141544,109294.15145530773],[1758987360728,109334.73884886612],[1758987657431,109333.45813781007],[1758988032924,109372.11534404145],[1758988320720,109438.08866579965],[1758988620714,109425.46388045549],[1758988823253,109433.85846854272],[1758989342198,109421.98515448018],[1758989425105,109426.07491520095],[1758989881185,109411.69816359848],[1758990092715,109434.02750598059],[1758990352869,109434.85104792991],[1758990722240,109419.16193575835],[1758990976420,109451.01819394728],[1758991381452,109431.52659178169],[1758991561348,109420.86140873906],[1758991822254,109418.53713769466],[1758992262849,109393.83162151331],[1758992520822,109385.55928560787],[1758992880883,109381.46855987459],[1758993031041,109386.02865122691],[1758993376270,109365.27184968411],[1758993781240,109310.79950571903],[1758993994582,109333.76291288335],[1758994213315,109342.19908726355],[1758994621742,109404.98480927321],[1758994921206,109387.52219089172],[1758995272568,109380.08233277495],[1758995552406,109350.3729080568],[1758995774503,109391.31010259358],[1758996042722,109413.47318967037],[1758996432520,109420.49321214826],[1758996713136,109433.20241193379],[1758996925462,109445.89439500228],[1758997271988,109445.60325218223],[1758997741292,109440.67902820447],[1758998033667,109441.59857676433],[1758998163117,109429.68970890965],[1758998454459,109436.91215926064],[1758998812368,109430.96523623966],[1758999023946,109450.79633710322],[1758999333201,109457.15758853668],[1758999962718,109452.83343697815],[1759000065702,109452.05792174363],[1759000322734,109456.24727737052],[1759000621602,109465.21131377787],[1759000828908,109451.62581989841],[1759001221370,109448.58397591667],[1759001561650,109444.03799990761],[1759001951000,109444.10650081551]],"market_caps":[[1758915480620,2176983260198.139],[1758915761559,2175793720184.173],[1758915952387,2175793720184.173],[1758916292714,2174631539574.0251],[1758916593065,2175695904483.919],[1758917331791,2175574569277.963],[1758917563157,2177107828680.457],[1758917760615,2178113646874.7188],[1758918120903,2175618781989.6064],[1758918453241,2175218552279.0872],[1758918623919,2175218552279.0872],[1758919041557,2176613421351.194],[1758919297149,2177448226832.9988],[1758919581441,2176615256053.8594],[1758919826585,2176615256053.8594],[1758920200836,2177850183052.8972],[1758920515438,2179014815581.8152],[1758920744631,2177994488498.1116],[1758921111839,2177233691643.4495],[1758921461638,2180102256785.6602],[1758921772588,2179934392089.227],[1758922102386,2181453156030.0383],[1758922260610,2181453156030.0383],[1758922514161,2181374586766.4126],[1758922852013,2181544524606.6235],[1758923221584,2180616009560.7375],[1758923460605,2179826628254.2412],[1758923711962,2181418073582.373],[1758924173949,2182447344284.023],[1758924492444,2180385424154.9473],[1758924795384,2182662141587.3457],[1758924953785,2182662141587.3457],[1758925260661,2184092599524.638],[1758925561432,2182216625457.5552],[1758925861465,2182830426368.1655],[1758926160938,2182886544075.0613],[1758926440601,2183531883130.5674],[1758926841920,2183956264572.125],[1758927044102,2184680083467.8462],[1758927414290,2184573104682.4287],[1758927622079,2184573104682.4287],[1758927921499,2184056043499.2173],[1758928243879,2182835432210.126],[1758928522254,2182902916503.1309],[1758928821881,2181842927564.437],[1758929124157,2183683684788.2446],[1758929460730,2183683684788.2446],[1758929821502,2184115419336.7249],[1758930120585,2185675119835.141],[1758930326721,2185690816263.7788],[1758930635479,2185726350683.6895],[1758931001933,2185866776171.5464],[1758931714100,2186075315820.048],[1758931872149,2185181205927.3198],[1758932242416,2185181205927.3198],[1758932551825,2185181205927.3198],
[1758932881910,2184386245934.777],[1758933073396,2184386245934.777],[1758933374732,2184304974992.8726],[1758933603953,2183180009178.8872],[1758933972093,2182993294600.5918],[1758934321735,2182819415165.8647],[1758934621419,2184270012532.3547],[1758934835195,2184270012532.3547],[1758935101687,2185734803999.6804],[1758935593350,2183788916763.6343],[1758935924918,2183788916763.6343],[1758936055801,2182838102648.0398],[1758936393814,2182156413294.2642],[1758936705790,2182562439725.5503],[1758936900794,2182562439725.5503],[1758937231503,2181975119077.0417],[1758937592292,2180492888230.0598],[1758937814619,2179919799865.6755],[1758938190744,2180526168399.53],[1758938441448,2181200684881.4185],[1758938792120,2182143607434.7517],[1758939161326,2181777422035.1963],[1758939493319,2182021142800.987],[1758939621587,2182021142800.987],[1758939961460,2180242965895.0503],[1758940330839,2179401490717.394],[1758940682475,2180049064162.6362],[1758940851910,2181299005824.2314],[1758941187723,2181716609521.2065],[1758941424332,2181716609521.2065],[1758941723899,2180981081011.4546],[1758942051332,2181124047015.004],[1758942335197,2181604261111.2312],[1758942636224,2181604261111.2312],[1758943054330,2181914629272.6023],[1758943381364,2182593039380.521],[1758943542601,2182593039380.521],[1758943861130,2183316715519.7754],[1758944126468,2183901825839.5347],[1758944462618,2185062976284.6277],[1758944787299,2186384730064.7993],[1758945141816,2185766224308.0977],[1758945451794,2184545989078.2654],[1758945760858,2183656636270.5867],[1758946044313,2184425573803.8286],[1758946362835,2184607621631.3193],[1758946572037,2183911581165.4492],[1758946912428,2184383564541.1653],[1758947281839,2184433394153.469],[1758947481941,2185024446313.428],[1758947831684,2186665339210.3481],[1758948182554,2185742419871.9526],[1758948332306,2185552968555.2256],[1758948696943,2185444232175.7837],[1758949040652,2185945311398.1648],[1758949381358,2185910723008.57],[1758949581540,2185575758267.578],[1758949920809,2185121379145.5015],[1758950271511,2185159319306.3142],[1758950580791,2185668978956.337],[1758950882104,2185185453620.1106],[1758951162458,2185010788681.3591],[1758951473262,2185303427675.3376],[1758951764280,2186075924771.731],[1758952075322,2186116505333.296],[1758952362527,2185514866187.0327],[1758952631820,2185830504043.3462],[1758952842818,2185830504043.3462],[1758953220607,2185603428436.322],[1758953444208,2185603428436.322],[1758953821809,2185603428436.322],[1758954181260,2183967535904.9373],[1758954362173,2183582588527.207],[1758954660889,2183582588527.207],[1758954950824,2183805636363.3682],[1758955281851,2183398429606.4507],[1758955615746,2182666309302.217],[1758955814438,2182666309302.217],[1758956193185,2183056558406.7656],[1758956581576,2183783827593.7473],[1758956844135,2183939762203.7751],[1758957112307,2183726133634.9026],[1758957312950,2183726133634.9026],[1758957672131,2183967126475.893],[1758958102100,2183331138238.5596],[1758958320957,2183331138238.5596],[1758958672774,2181537273870.2573],[1758958826006,2181537273870.2573],[1758959240924,2180824878199.7708],[1758959568188,2180335195793.092],[1758959820685,2180335195793.092],[1758960182015,2180447403415.4236],[1758960364087,2180447403415.4236],[1758960720936,2178482832386.4502],[1758960932238,2178482832386.4502],[1758961351151,2178815754652.8945],[1758961502615,2178815754652.8945],[1758961872915,2179208579336.198],[1758962204492,2179374844012.841],[1758962481832,2179374844012.841],[1758962842940,2179109348666.2375],[1758963053982,2179109348666.2375],[1758963382791,2178490348946.8918],[1758963823166,2178186899401.8882],[1758964020655,2178186899401.8882],[1758964345609,2176989211233.2456],[1758964570548,2176533113966.9932],[1758964874910,2176275504483.0635],[1758965121822,2175990537266.158],[1758965532244,2175514709154.5898],[1758965864134,2176597403439.5156],[1758966061427,2176597403439.5156],[1758966383605,2178293874765.623],[1758966743289,2178310459428.9941],[1758966922351,2178194905292.3923],
[1758967332130,2179182307695.1145],[1758967663014,2178394242552.6697],[1758967920660,2178693867639.512],[1758968271357,2178692165410.7183],[1758968561656,2178436933555.9382],[1758968721761,2178436933555.9382],[1758969151006,2178801879256.9314],[1758969495541,2180145104886.165],[1758969660746,2180145104886.165],[1758970062502,2180931580087.2104],[1758970231929,2180931580087.2104],[1758970620717,2181317335088.5955],[1758970837235,2181317335088.5955],[1758971220562,2180686725755.405],[1758971423968,2180686725755.405],[1758971842653,2179178925362.5706],[1758972025201,2179178925362.5706],[1758972394721,2179236928260.447],[1758972762558,2179473494474.7202],[1758972942891,2179473494474.7202],[1758973250705,2179462495984.452],[1758973544842,2179174865881.6091],[1758973832630,2178818068610.4214],[1758974160622,2178751745766.7305],[1758974521288,2178902101624.8494],[1758974782577,2178902101624.8494],[1758975100988,2178902101624.8494],[1758975353652,2178902101624.8494],[1758975731045,2178347043345.7603],[1758976042588,2177969104468.003],[1758976292551,2177895800106.7566],[1758976620560,2177740030208.8916],[1758976826289,2177960532945.306],[1758977181656,2180920562347.1667],[1758977581529,2180866378930.0613],[1758977780864,2180270154108.538],[1758978173616,2180870048353.01],[1758978371388,2181011933724.7852],[1758978734245,2179863323481.4358],[1758978954102,2179478414813.572],[1758979321150,2179930069176.6816],[1758979691297,2179578469248.5935],[1758979886991,2180036642170.342],[1758980176986,2179428836939.1572],[1758980408097,2179809280859.524],[1758980772580,2179741995368.5244],[1758981201082,2179810433078.9543],[1758981462629,2179428788829.2634],[1758981685335,2179287423271.8706],[1758981984851,2179161288715.1394],[1758982361239,2179411139139.5225],[1758982661617,2180102322226.7136],[1758982843608,2179208938875.3438],[1758983112017,2179208938875.3438],[1758983561507,2179230743838.898],[1758983820771,2179530346962.0747],[1758984042531,2179530346962.0747],[1758984300722,2179521450452.3235],[1758984713782,2178151423311.6895],[1758984922261,2178151423311.6895],[1758985364906,2178356479369.1267],[1758985633965,2177614921361.7788],[1758985939244,2177497367869.335],[1758986220655,2177925295298.0884],[1758986481011,2177925295298.0884],[1758986723907,2178617644220.921],[1758987141544,2178033501117.8547],[1758987360728,2178033501117.8547],[1758987657431,2178586454770.1482],[1758988032924,2179429184286.924],[1758988320720,2180769092274.6096],[1758988620714,2180769092274.6096],[1758988823253,2180488333514.4604],[1758989342198,2180453896438.8643],[1758989425105,2180453896438.8643],[1758989881185,2180189434769.9475],[1758990092715,2179807137268.7937],[1758990352869,2180550743491.4822],[1758990722240,2180521699319.5713],[1758990976420,2180515144343.682],[1758991381452,2181147349600.4988],[1758991561348,2180637052589.6975],[1758991822254,2180433621237.0752],[1758992262849,2179699155373.459],[1758992520822,2179967391533.375],[1758992880883,2179550164912.4739],[1758993031041,2179626399949.2026],[1758993376270,2179818071992.803],[1758993781240,2179054959426.4807],[1758993994582,2178470523987.6487],[1758994213315,2178316562376.8247],[1758994621742,2179532457856.306],[1758994921206,2179210339562.3953],[1758995272568,2179815008593.4233],[1758995552406,2179574030771.22],[1758995774503,2179393819939.6443],[1758996042722,2180370991547.7986],[1758996432520,2180986751020.5894],[1758996713136,2180986751020.5894],[1758996925462,2180986751020.5894],[1758997271988,2180986751020.5894],[1758997741292,2180921885782.0837],[1758998033667,2180873433552.7827],[1758998163117,2180604839996.0503],[1758998454459,2180604839996.0503],[1758998812368,2180631500848.338],[1758999023946,2180631500848.338],[1758999333201,2181088293752.6377],[1758999962718,2181196856615.7458],[1759000065702,2181095346904.7937],[1759000322734,2181121188312.724],[1759000621602,2181305595692.649],[1759000828908,2181305595692.649],[1759001221370,2181234811753.569],[1759001561650,2181118545316.8894],[1759001951000,2180953835042.6807]]}
""",
        object : TypeToken<MarketChartResponse>(){}.type
    )
    val processedData = chartData.prices?.map { Pair(it?.get(0) ?: 0.0, it?.get(1) ?: 0.0) }

    processedData?.let {
        //FullScreenLineChartScreen(null,it) {}
        LineChartCustom(it) {}
    }
}