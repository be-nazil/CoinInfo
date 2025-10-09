package com.nb.coininfo.data.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json


@Keep
data class MarketChartResponse(
    @Json(name = "market_caps")
    @SerializedName("market_caps")
    val marketCaps: List<List<Double?>?>?,
    @Json(name = "prices")
    @SerializedName("prices")
    val prices: List<List<Double?>?>?,
    @Json(name = "total_volumes")
    @SerializedName("total_volumes")
    val totalVolumes: List<List<Double?>?>?
)



