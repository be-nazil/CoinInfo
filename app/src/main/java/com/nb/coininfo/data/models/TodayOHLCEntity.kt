package com.nb.coininfo.data.models
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

import com.squareup.moshi.Json

data class TodayOHLCList(
    val data: List<TodayOHLCEntityItem>
)

@Keep
@Entity(tableName = "today_ohlc")
data class TodayOHLCEntityItem(
    @Json(name = "close")
    @ColumnInfo(name = "close")
    val close: Double?,

    @Json(name = "high")
    @ColumnInfo(name = "high")
    val high: Double?,

    @Json(name = "low")
    @ColumnInfo(name = "low")
    val low: Double?,

    @ColumnInfo(name = "market_cap")
    @Json(name = "market_cap")
    val marketCap: Long?,

    @Json(name = "open")
    @ColumnInfo(name = "open")
    val open: Double?,

    @Json(name = "time_close")
    @ColumnInfo(name = "time_close")
    val timeClose: String?,

    @ColumnInfo(name = "time_open")
    @Json(name = "time_open")
    val timeOpen: String?,

    @Json(name = "volume")
    @ColumnInfo(name = "volume")
    val volume: Long?,

    @Json(name = "id")
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val error: String? = null
)


