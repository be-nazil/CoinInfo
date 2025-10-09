package com.nb.coininfo.ui.screens.splash

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.nb.coininfo.R
import com.nb.coininfo.ui.components.RoundOverlayLoadingWheel
import com.nb.coininfo.ui.screens.Screen
import com.nb.coininfo.ui.theme.CoinInfoTheme

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel<SplashViewModel>(),
    onEvent: (Screen) -> Unit
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    SplashContent(
        isLoading = state.value.isLoading
    ) {
        //Log.i("Splash", "SplashScreen: splashTimeOut")
        onEvent.invoke(Screen.Home)
    }
}

@Composable
fun SplashContent(
    isLoading: Boolean,
    splashTimeOut:( () -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {

        //BitcoinSplashScreen()
        AnimatedVisibility(
            visible = isLoading,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> -(fullHeight/2) },
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> -(fullHeight/2) },
            ) + fadeOut(),
        ) {
            val loadingContentDescription = stringResource(id = R.string.data_loading)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp)
                    .background(color = Color.Transparent),
            ) {
                RoundOverlayLoadingWheel(
                    modifier = Modifier
                        .align(Alignment.Center),
                    contentDesc = loadingContentDescription,
                )
                if (!isLoading)
                    splashTimeOut?.invoke()
            }
        }
    }
}

@Composable
fun BitcoinSplashScreen() {

    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF2C2C2C), Color(0xFF1A1A1A)),
                    radius = 800f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_bitcoin_logo),
            contentDescription = "Rotating Bitcoin Logo",
            modifier = Modifier
                .size(200.dp)
                .graphicsLayer {
                    rotationY = rotation
                    rotationX = 15f
                    cameraDistance = 32f * density
                }
                .alpha(alpha)
        )
    }
}


@Preview
@Composable
fun Prev(modifier: Modifier = Modifier) {
    CoinInfoTheme {
        SplashContent(isLoading = false)
    }
}