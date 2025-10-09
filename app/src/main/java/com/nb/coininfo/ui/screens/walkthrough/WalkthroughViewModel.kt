package com.nb.coininfo.ui.screens.walkthrough

import androidx.annotation.Keep
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


enum class WalkthroughTarget {
    PRICE_OVERVIEW,
    TOP_COINS_SECTION,
    TOP_MOVERS_TAB,
    //COIN_LIST_ITEM
}

@Keep
data class WalkthroughStep(
    val target: WalkthroughTarget,
    val title: String,
    val description: String
)

data class WalkthroughUiState(
    val currentStepIndex: Int?,
    val currentStep: WalkthroughStep?,

)

@HiltViewModel
class WalkthroughViewModel  @Inject constructor(): ViewModel() {

    val allWalkthroughSteps = listOf(
        WalkthroughStep(
            target = WalkthroughTarget.PRICE_OVERVIEW,
            title = "Market Overview",
            description = "See the current price and key 24-hour metrics for the selected cryptocurrency."
        ),
        WalkthroughStep(
            target = WalkthroughTarget.TOP_COINS_SECTION,
            title = "Top Coins",
            description = "Explore major cryptocurrencies ranked by market capitalization."
        ),
        WalkthroughStep(
            target = WalkthroughTarget.TOP_MOVERS_TAB,
            title = "Top Movers",
            description = "Discover the cryptocurrencies with the biggest price changes today."
        )/*,
        WalkthroughStep(
            target = WalkthroughTarget.COIN_LIST_ITEM,
            title = "Individual Coin Details",
            description = "Tap on any coin to view its detailed chart, news, and historical data."
        )*/
    )

    var currentStepIndex by mutableStateOf(0)
        private set

    val currentStep: WalkthroughStep?
        get() = allWalkthroughSteps.getOrNull(currentStepIndex)

    var showWalkthrough by mutableStateOf(false)
        private set

    fun startWalkthrough() {
        currentStepIndex = 0
        showWalkthrough = true
    }

    fun nextStep() {
        if (currentStepIndex < allWalkthroughSteps.size - 1) {
            currentStepIndex++
        } else {
            endWalkthrough()
        }
    }

    fun previousStep() {
        if (currentStepIndex > 0) {
            currentStepIndex--
        }
    }

    fun endWalkthrough() {
        showWalkthrough = false
        currentStepIndex = 0
    }

}