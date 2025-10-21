package com.nb.coininfo.ui.screens.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nb.coininfo.ui.components.AnimationType
import com.nb.coininfo.ui.components.AppAnimatedVisibility
import com.nb.coininfo.ui.components.CardAction
import com.nb.coininfo.ui.components.CombinedCryptoSummaryCard
import com.nb.coininfo.ui.components.ShimmerCard
import com.nb.coininfo.ui.components.shimmerBrush
import com.nb.coininfo.ui.screens.Screen
import com.nb.coininfo.ui.screens.search.SearchScreen
import com.nb.coininfo.ui.screens.walkthrough.WalkthroughOverlay
import com.nb.coininfo.ui.screens.walkthrough.WalkthroughTarget
import com.nb.coininfo.ui.screens.walkthrough.WalkthroughViewModel
import com.nb.coininfo.ui.screens.walkthrough.walkthroughTarget
import com.nb.coininfo.ui.theme.CardDarkBackground
import com.nb.coininfo.ui.theme.ScreenBackground
import kotlinx.coroutines.delay


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    walkthroughViewModel: WalkthroughViewModel = hiltViewModel(),
    navigate: ((Screen) -> Unit)?
) {

    val state = viewModel.homeUiState.collectAsStateWithLifecycle()
    HomeScreenContent(modifier = modifier, state.value, walkthroughViewModel, navigate)

    /*LaunchedEffect(homeViewModel.event) {
        homeViewModel.effect.collect { action->
            Log.i("MainAct", "MainAct: action")
            when(action) {
                is HomeReducer.HomeEffect.NavigateTo -> {
                    onClick.invoke(action.screen)
                }
            }
        }
    }*/

}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    homeUiState: HomeUiState = HomeUiState(),
    walkthroughViewModel: WalkthroughViewModel = viewModel(),
    navigate: ((Screen)-> Unit)? = null,
) {
    val highlightBounds = remember { mutableStateMapOf<WalkthroughTarget, Rect>() }
    val currentStep = walkthroughViewModel.currentStep

    val coinsList = homeUiState.coinsList.filterNot { it?.rank == 0 }.sortedBy { it?.rank }
    val topGainers = homeUiState.topMovers?.gainers ?: emptyList()
    val topLosers = homeUiState.topMovers?.losers ?: emptyList()

    var animationType by remember { mutableStateOf(AnimationType.FADE) }
    val lazyListState = rememberLazyListState()

    LaunchedEffect(currentStep) {
        currentStep ?: return@LaunchedEffect

        val targetIndex = when (currentStep.target) {
            WalkthroughTarget.PRICE_OVERVIEW -> 0
            WalkthroughTarget.TOP_COINS_SECTION -> 1
            WalkthroughTarget.TOP_MOVERS_TAB -> 2
            //WalkthroughTarget.COIN_LIST_ITEM -> 3
        }

        if (targetIndex >= 0) {
            lazyListState.animateScrollToItem(targetIndex)

            delay(500)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        containerColor = ScreenBackground
    ) {

        Box {
            LazyColumn (modifier = Modifier.padding(it).padding(10.dp), state = lazyListState) {
                item {
                    Spacer(modifier = Modifier
                        .height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            modifier = Modifier.padding(4.dp),
                            text = "Overview",
                            style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.W400),
                            letterSpacing = TextUnit(5f, TextUnitType.Unspecified),
                            color = Color.White
                        )

                        IconButton(
                            colors = IconButtonDefaults.iconButtonColors(containerColor = CardDarkBackground),
                            onClick = {
                                navigate?.invoke(Screen.SearchScreen)
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "search", tint = Color.White)
                        }
                    }
                }
                item {
                    Spacer(Modifier.height(10.dp))

                    if (homeUiState.isSummaryLoading) {
                        ShimmerCard(shimmerBrush())
                    }
                    AppAnimatedVisibility(
                        visible = !homeUiState.isSummaryLoading,
                        animationType = animationType
                    ) {
                        CombinedCryptoSummaryCard(
                            summary = homeUiState.todaySummary,
                            modifier = Modifier
                                .walkthroughTarget(WalkthroughTarget.PRICE_OVERVIEW, highlightBounds)
                        ) { action ->
                            when(action) {
                                CardAction.RECEIVE , CardAction.EXPAND , CardAction.SWAP ,
                                CardAction.MORE -> {}
                                CardAction.HELP -> {
                                    walkthroughViewModel.startWalkthrough()
                                    Log.d("HomeScreen", "HomeScreenContent: $action")
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = "Top Coins by Market Cap",
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.W400),
                        letterSpacing = TextUnit(5f, TextUnitType.Unspecified),
                        color = Color.White
                    )
                    Spacer(Modifier.height(10.dp))
                    if (homeUiState.isCoinListLoading) {
                        ShimmerCard(shimmerBrush())
                    }
                    AppAnimatedVisibility(
                        visible = !homeUiState.isCoinListLoading,
                        animationType = AnimationType.SLIDE_VERTICALLY
                    ) {
                        TopCoinsSection(coinsList,
                            modifier = Modifier
                                .walkthroughTarget(WalkthroughTarget.TOP_COINS_SECTION, highlightBounds)) {
                            Log.d("HomeScreenContent", "HomeScreenContent: clicked $it")
                            navigate?.invoke(Screen.CoinDetail(it))
                        }
                    }
                }

                stickyHeader {
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = "Top Gainers/Losers",
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.W400),
                        letterSpacing = TextUnit(5f, TextUnitType.Unspecified),
                        color = Color.White
                    )
                    Spacer(Modifier.height(10.dp))
                    if (homeUiState.isMoversListLoading) {
                        ShimmerCard(shimmerBrush())
                    }

                    AppAnimatedVisibility(
                        visible = !homeUiState.isMoversListLoading,
                        animationType = AnimationType.EXPAND_VERTICALLY
                    ) {
                        TopMoversSection(topGainers, topLosers,
                            modifier = Modifier.walkthroughTarget(WalkthroughTarget.TOP_MOVERS_TAB, highlightBounds)) { id ->
                            navigate?.invoke(Screen.CoinDetail(id))
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }

            }

            WalkthroughOverlay(
                viewModel = walkthroughViewModel,
                highlightBounds = highlightBounds
            )
        }
    }
}

@Composable
fun SearchTextField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text("Search...", color = Color.White) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon"
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear search query"
                    )
                }
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
            }
        )
    )
}