package com.nb.coininfo.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nb.coininfo.ui.theme.Gray24

/**
 * Creates a reusable shimmer effect using a linear gradient brush.
 *
 * @param showShimmer Controls whether the shimmer animation is active.
 * @param targetValue The end value for the animation, determining the size of the shimmer area.
 * @return A `Brush` that can be used as a background for composables.
 */
@Composable
fun shimmerBrush(showShimmer: Boolean = true, targetValue: Float = 1000f): Brush {
    if (!showShimmer) {
        return Brush.linearGradient(listOf(Color.Transparent))
    }

    // This is the core of the animation. It creates a transition that repeats infinitely.
    val transition = rememberInfiniteTransition(label = "ShimmerTransition")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(800), // Duration of one shimmer cycle
            repeatMode = RepeatMode.Restart
        ),
        label = "ShimmerAnimation"
    )

    // Defines the colors of the shimmer gradient. A lighter color is swept across a darker one.
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    // Creates the linear gradient brush. The animation value is used to move the gradient.
    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnimation.value, y = translateAnimation.value)
    )
}

/**
 * A composable that displays a placeholder card with a shimmer effect.
 * This is useful for loading states in a list.
 */
@Composable
fun ShimmerCardItem(brush: Brush) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gray24)
            .padding(all = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Placeholder for an avatar or image
        Spacer(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(brush)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(verticalArrangement = Arrangement.Center) {
            // Placeholder for a title line
            Spacer(
                modifier = Modifier
                    .height(20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(0.7f)
                    .background(brush)
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Placeholder for a subtitle line
            Spacer(
                modifier = Modifier
                    .height(20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(0.9f)
                    .background(brush)
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Placeholder for a subtitle line
            Spacer(
                modifier = Modifier
                    .height(20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth(0.9f)
                    .background(brush)
            )
        }
    }
}

@Composable
fun ShimmerCard(brush: Brush) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Gray24,
            disabledContainerColor = Color.Gray,
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            Column(verticalArrangement = Arrangement.Center) {
                Spacer(modifier = Modifier.height(20.dp))
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth(0.4f)
                        .background(brush)
                )
                Spacer(modifier = Modifier.height(10.dp))
                // Placeholder for a subtitle line
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth(0.9f)
                        .background(brush)
                )
                Spacer(modifier = Modifier.height(10.dp))
                // Placeholder for a subtitle line
                Spacer(
                    modifier = Modifier
                        .height(20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth(0.9f)
                        .background(brush)
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

/**
 * A preview function to display the shimmer card in Android Studio.
 */
@Preview(showBackground = true)
@Composable
fun ShimmerCardPreview() {
    Column {
        ShimmerCard(brush = shimmerBrush())
        // You can create a list of shimmer items for a realistic loading preview.
        ShimmerCardItem(brush = shimmerBrush())
//        ShimmerCardItem(brush = shimmerBrush())
//        ShimmerCardItem(brush = shimmerBrush())
    }
}
