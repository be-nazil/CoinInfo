package com.nb.coininfo.ui.screens.walkthrough

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

fun Modifier.walkthroughTarget(
    target: WalkthroughTarget,
    boundsMap: MutableMap<WalkthroughTarget, Rect>
): Modifier = this.onGloballyPositioned { layoutCoordinates ->
    boundsMap[target] = layoutCoordinates.boundsInWindow()
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WalkthroughOverlay(
    viewModel: WalkthroughViewModel,
    highlightBounds: Map<WalkthroughTarget, Rect>
) {
    val currentStep = viewModel.currentStep
    val showWalkthrough = viewModel.showWalkthrough

    val color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
    AnimatedVisibility(
        visible = showWalkthrough,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = Modifier
            .fillMaxSize()
            .zIndex(100f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val backgroundColor = Color.Black.copy(alpha = 0.5f)
                drawRect(color = backgroundColor)

                currentStep?.let { step ->
                    highlightBounds[step.target]?.let { rect ->
                        clipPath(Path().apply {
                            addRect(rect)
                        }) {
                            drawRect(color = Color.Transparent, style = Fill)
                        }
                        drawRect(
                            color = color,
                            topLeft = rect.topLeft,
                            size = rect.size,
                            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx())
                        )
                    }
                }
            }


            currentStep?.let { step ->
                highlightBounds[step.target]?.let { targetRect ->
                    val density = LocalDensity.current
                    val configuration = LocalConfiguration.current

                    val screenHeightInPx = with(density) { configuration.screenHeightDp }
                    val cardHeightInPx = with(density) { 200.dp.toPx() }
                    val marginInPx = with(density) { 16.dp.toPx() }

                    val spaceBelowInPx = screenHeightInPx - targetRect.bottom
                    val yPositionAboveInPx = targetRect.top - cardHeightInPx - marginInPx
                    val yPositionBelowInPx = targetRect.bottom + marginInPx

                    val isAboveValid = yPositionAboveInPx >= 0
                    val isBelowValid = (yPositionBelowInPx + cardHeightInPx) <= screenHeightInPx

                    val yPositionInPx = if (isAboveValid) {
                        yPositionAboveInPx
                    } else if (isBelowValid) {
                        yPositionBelowInPx
                    } else {
                        yPositionBelowInPx
                    }

                    val yPosDp = with(density) { yPositionInPx.toDp() }

                    AnimatedContent(
                        targetState = step,
                        transitionSpec = {
                            (slideInVertically { h -> h } + fadeIn()) with
                                    (slideOutVertically { h -> -h } + fadeOut())
                        }
                    ) { currentWalkthroughStep ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .offset(y = yPosDp)
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Center)
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = currentWalkthroughStep.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = currentWalkthroughStep.description,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${viewModel.currentStepIndex + 1}/${viewModel.allWalkthroughSteps.size}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                        )
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            if (viewModel.currentStepIndex > 0) {
                                                Button(
                                                    onClick = { viewModel.previousStep() },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                                        contentColor = MaterialTheme.colorScheme.primary
                                                    )
                                                ) {
                                                    Text("Back")
                                                }
                                            }
                                            Button(
                                                onClick = { viewModel.nextStep() },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.primary
                                                )
                                            ) {
                                                Text(if (viewModel.currentStepIndex == viewModel.allWalkthroughSteps.size - 1) "Finish" else "Next")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}