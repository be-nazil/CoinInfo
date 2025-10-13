package com.nb.coininfo

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nb.coininfo.ui.screens.home.HomeScreen
import com.nb.coininfo.ui.screens.Screen
import com.nb.coininfo.ui.screens.coindetails.CoinDetailScreen
import com.nb.coininfo.ui.screens.coindetails.CoinDetailViewModel
import com.nb.coininfo.ui.screens.coindetails.CoinEventsScreen
import com.nb.coininfo.ui.screens.coindetails.FullScreenLineChartScreen
import com.nb.coininfo.ui.screens.home.HomeViewModel
import com.nb.coininfo.ui.screens.splash.SplashScreen
import com.nb.coininfo.ui.screens.splash.SplashViewModel
import com.nb.coininfo.ui.screens.walkthrough.WalkthroughViewModel
import com.nb.coininfo.ui.screens.webview.WebViewScreen
import com.nb.coininfo.ui.theme.CoinInfoTheme
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Type

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoinInfoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupNavigation()
                }
            }
        }
    }
}


@Composable
fun SetupNavigation() {

    val navHostController: NavHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = Screen.Splash) {
        composable<Screen.Splash> { backStackEntry->
            val viewModel = hiltViewModel<SplashViewModel>(backStackEntry)
            SplashScreen(viewModel = viewModel) { screen ->
                navHostController.navigate(screen) {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(Screen.Splash) { inclusive = true }
                }
            }
        }

        composable<Screen.Home> { backStackEntry ->
            BackHandler(enabled = navHostController.previousBackStackEntry != null) {
                navHostController.popBackStack()
            }

            val viewModel = hiltViewModel<HomeViewModel>(backStackEntry)
            val walkthroughViewModel = hiltViewModel<WalkthroughViewModel>(backStackEntry)
            HomeScreen(Modifier, viewModel, walkthroughViewModel) { coinId ->
                navHostController.navigate(Screen.CoinDetail(coinId)) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }


        composable<Screen.CoinDetail> { backStackEntry ->
            val coin = backStackEntry.arguments?.getString("cryptoId") ?: return@composable
            val viewModel = hiltViewModel<CoinDetailViewModel>(backStackEntry)
            val state = viewModel.uiState.collectAsStateWithLifecycle()
            CoinDetailScreen(
                coin,
                state.value,
                viewModel::onEvent,
                onNavigateUp = { screen ->
                    if (screen == null) navHostController.popBackStack()
                    else navHostController.navigate(screen) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable<Screen.CoinDetailGraph> { backStackEntry->
            val coin : Screen.CoinDetailGraph = backStackEntry.toRoute()
            val data = Gson().fromJson<List<Pair<Double, Double>>>(coin.data, object: TypeToken<List<Pair<Double, Double>>>(){}.type)
            FullScreenLineChartScreen(coin.coinName, data) {
                navHostController.popBackStack()
            }
        }

        composable<Screen.WebView> { backStackEntry ->
            val coin: Screen.WebView = backStackEntry.toRoute()
            WebViewScreen(coin.url, title = coin.name) {
                navHostController.popBackStack()
            }
        }

        composable<Screen.CoinEventsScreen> { backStackEntry ->
            val coin: Screen.CoinEventsScreen = backStackEntry.toRoute()
            val viewModel = hiltViewModel<CoinDetailViewModel>(backStackEntry)
            CoinEventsScreen(
                coinId = coin.cryptoId, coinName = coin.coinName, viewModel = viewModel,
                onEvent = { navHostController.navigate(it) }
            ) {
                navHostController.popBackStack()
            }
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CoinInfoTheme {
        Greeting("Android")
    }
}