package com.nb.coininfo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nb.coininfo.R
import com.nb.coininfo.data.models.TodayOHLCEntityItem
import com.nb.coininfo.ui.theme.CardActionGray
import com.nb.coininfo.ui.theme.Gray24
import com.nb.coininfo.ui.theme.MutedText
import java.text.NumberFormat
import java.util.Locale


// --- An enum to represent card actions for clarity ---
enum class CardAction {
    RECEIVE, EXPAND, SWAP, MORE, HELP
}


/**
 * A card that displays a combined summary of crypto data, featuring one
 * primary metric and several secondary metrics.
 *
 * @param summary The data object containing all crypto information.
 * @param modifier A Modifier for this composable.
 * @param onActionClick A callback triggered when an action icon is clicked.
 */
@Composable
fun CombinedCryptoSummaryCard(
    summary: TodayOHLCEntityItem?,
    modifier: Modifier = Modifier,
    onActionClick: (CardAction) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Gray24,
            disabledContainerColor = Color.Gray,
            )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {

            Image(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.TopEnd)
//                                .fadingEdge(leftRightFade)
//                                .fadingEdge(topBottomFade)
                ,
                contentScale = ContentScale.FillHeight,
                painter = painterResource(id = R.drawable.ic_cube), contentDescription = "",
            )

            Icon(
                modifier = Modifier
                    .padding(10.dp)
                    .size(30.dp)
                    .align(Alignment.TopEnd)
                    .clickable {
                        onActionClick.invoke(CardAction.HELP)
                    },
                imageVector = Icons.Outlined.Info,
                tint = Color.White,
                contentDescription = "",
            )

            // Main content area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Price (Close)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatMetricValue(summary?.close),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = CardActionGray)
                Spacer(modifier = Modifier.height(16.dp))

                // --- Secondary Metrics Grid ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricItem("High 24H", formatMetricValue(summary?.high))
                    MetricItem("Low 24H", formatMetricValue(summary?.low), alignment = Alignment.End)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricItem("Volume", formatMetricValue(summary?.volume))
                    MetricItem("Market Cap", formatMetricValue(summary?.marketCap), alignment = Alignment.End)
                }
            }

            // Bottom action bar
           // ActionToolbar(onActionClick = onActionClick)
        }
    }
}

/**
 * A small, reusable composable for displaying a label and its value.
 */
@Composable
private fun MetricItem(
    label: String,
    value: String,
    alignment: Alignment.Horizontal = Alignment.Start
) {
    Column(horizontalAlignment = alignment) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MutedText,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// Helper function to format values (same as before)
private fun formatMetricValue(value: Number?): String {
    if (value == null) return "N/A"
    val format = NumberFormat.getCurrencyInstance(Locale.US)
    return when (value) {
        is Long -> {
            format.maximumFractionDigits = 0
            format.format(value)
        }
        is Double -> {
            format.maximumFractionDigits = 2
            format.format(value)
        }
        else -> value.toString()
    }
}

// ActionToolbar and ActionButton composables remain the same...

// --- Preview Function ---
@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun CombinedCryptoSummaryCardPreview() {
    val sampleData = TodayOHLCEntityItem(
        volume = 1_234_567_890L,
        marketCap = 98_765_432_100L,
        high = 45_123.45,
        low = 42_987.65,
        open = 0.0, timeClose = null, timeOpen = null,
        close = 44_500.12
    )

    Box(modifier = Modifier.padding(16.dp)) {
        CombinedCryptoSummaryCard(summary = sampleData)
    }
}
