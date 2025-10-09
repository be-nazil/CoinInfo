package com.nb.coininfo.data.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Keep
@Entity(tableName = "movers_table")
data class MoverEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "symbol")
    val symbol: String,
    @ColumnInfo(name = "rank")
    val rank: Int,
    @Json(name = "percent_change")
    @SerializedName("percent_change")
    @ColumnInfo(name = "percent_change")
    val percentChange: Double
)