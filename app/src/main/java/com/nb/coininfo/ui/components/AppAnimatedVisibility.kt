package com.nb.coininfo.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset


@Composable
fun AppAnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    animationType: AnimationType = AnimationType.FADE,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    val enterTransition: EnterTransition
    val exitTransition: ExitTransition

    val enterDuration = 220
    val enterDelay = 90
    val exitDuration = 90

    when (animationType) {
        AnimationType.FADE -> {
            enterTransition = fadeIn(animationSpec = tween(durationMillis = enterDuration, delayMillis = enterDelay))
            exitTransition = fadeOut(animationSpec = tween(durationMillis = exitDuration))
        }
        AnimationType.SLIDE_VERTICALLY -> {
            enterTransition = slideInVertically(
                initialOffsetY = { -it },
                // CORRECT: No <Int> type parameter for the tween spec
                animationSpec = tween(durationMillis = enterDuration, delayMillis = enterDelay)
            ) + fadeIn(animationSpec = tween(durationMillis = enterDuration, delayMillis = enterDelay))

            exitTransition = slideOutVertically(
                targetOffsetY = { -it },
                // CORRECT: No <Int> type parameter for the tween spec
                animationSpec = tween(durationMillis = exitDuration)
            ) + fadeOut(animationSpec = tween(durationMillis = exitDuration))
        }
        AnimationType.EXPAND_VERTICALLY -> {
            enterTransition = expandVertically(
                expandFrom = Alignment.Top,
                // CORRECT: No <Int> type parameter for the tween spec
                animationSpec = tween(durationMillis = enterDuration, delayMillis = enterDelay)
            ) + fadeIn(animationSpec = tween(durationMillis = enterDuration, delayMillis = enterDelay))

            exitTransition = shrinkVertically(
                shrinkTowards = Alignment.Top,
                // CORRECT: No <Int> type parameter for the tween spec
                animationSpec = tween(durationMillis = exitDuration)
            ) + fadeOut(animationSpec = tween(durationMillis = exitDuration))
        }
    }

    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = enterTransition,
        exit = exitTransition,
        content = content
    )
}

enum class AnimationType {
    /** Fades in and out. */
    FADE,

    /** Slides in from the top and slides out to the top, with a fade. */
    SLIDE_VERTICALLY,

    /** Expands from the top and shrinks to the top, with a fade. */
    EXPAND_VERTICALLY
}