package com.nb.coininfo.data.models

import com.squareup.moshi.Json

enum class CoinType {
    @Json(name = "coin") Coin,
    @Json(name = "token") Token
}