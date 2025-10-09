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
import com.nb.coininfo.ui.screens.coindetails.FullScreenLineChartScreen
import com.nb.coininfo.ui.screens.home.HomeViewModel
import com.nb.coininfo.ui.screens.splash.SplashScreen
import com.nb.coininfo.ui.screens.splash.SplashViewModel
import com.nb.coininfo.ui.screens.walkthrough.WalkthroughViewModel
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
                    SetupNavigation()//navHostController = rememberNavController())
                }
            }
        }
    }
}


@Composable
fun SetupNavigation() {

    val navHostController: NavHostController = rememberNavController()
    NavHost(navController = navHostController, startDestination = Screen.Splash) {
        composable<Screen.Splash> {
            val viewModel = hiltViewModel<SplashViewModel>()
            //Log.i("MainAct", "SetupNavigation: called Splash")
            SplashScreen(viewModel = viewModel) { screen->
                navHostController.navigate(screen) {
                    popUpTo<Screen.Splash> { inclusive = true }
                }
            }
        }

        composable<Screen.Home> { backStackEntry ->
            val context = LocalContext.current
            BackHandler {
                // Cast the context to an Activity and call finish() to close the app
                (context as? Activity)?.onBackPressed()
            }
            //Log.i("MainAct", "SetupNavigation: called Home")

            val viewModel = hiltViewModel<HomeViewModel>()
            val walkthroughViewModel = hiltViewModel<WalkthroughViewModel>()
            HomeScreen(Modifier, viewModel, walkthroughViewModel) {
                navHostController.navigate(Screen.CoinDetail(it))
            }
        }


        composable<Screen.CoinDetail> { backStackEntry ->
            val coin : Screen.CoinDetail = backStackEntry.toRoute()
            if (coin.cryptoId.isNotEmpty()) {
                val viewModel = hiltViewModel<CoinDetailViewModel>()
                val state = viewModel.uiState.collectAsStateWithLifecycle()
                CoinDetailScreen(
                    coin.cryptoId,
                    state.value,
                    viewModel::onEvent,
                    onNavigateUp = { screen ->
                        Log.d("SetupNavigation", "SetupNavigation: clickedd")
                        if (screen == null) {
                            navHostController.popBackStack()
                        } else {
                            navHostController.navigate(screen)
                        }
                    }
                )
            } else {
                // Show a loading or error state
            }
        }

        composable<Screen.CoinDetailGraph> { backStackEntry->
            val coin : Screen.CoinDetailGraph = backStackEntry.toRoute()
            val data = Gson().fromJson<List<Pair<Double, Double>>>(coin.data, object: TypeToken<List<Pair<Double, Double>>>(){}.type)
            FullScreenLineChartScreen(coin.coinName, data) {
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