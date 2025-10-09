package com.nb.coininfo.ui.screens

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object Splash: Screen()
    @Serializable
    data object Home: Screen()
    @Serializable
    data class CoinDetail(val cryptoId: String): Screen()
    @Serializable

    data class CoinDetailGraph(val coinName: String?, val data: String): Screen()
    @Serializable
    data object You: Screen()
    @Serializable
    data class WebView(val url: String, val name: String): Screen()
}

