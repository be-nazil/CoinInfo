package com.nb.coininfo.data.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Keep
data class CoinGraphData(
    @Json(name = "close")
    @SerializedName("close")
    val close: Double?,
    @Json(name = "high")
    @SerializedName("high")
    val high: Double?,
    @Json(name = "low")
    @SerializedName("low")
    val low: Double?,
    @Json(name = "market_cap")
    @SerializedName("market_cap")
    val marketCap: Long?,
    @Json(name = "open")
    @SerializedName("open")
    val `open`: Double?,
    @Json(name = "time_close")
    @SerializedName("time_close")
    val timeClose: String?,
    @Json(name = "time_open")
    @SerializedName("time_open")
    val timeOpen: String?,
    @Json(name = "volume")
    @SerializedName("volume")
    val volume: Long?,
    val error: String? = null
)

data class OhlcData(
    val timeClose: String,
    val close: Double
) {
    // Helper to convert the timestamp string to a more usable format if needed
    val date: LocalDateTime
        get() = Instant.parse(timeClose).atZone(ZoneId.systemDefault()).toLocalDateTime()
}