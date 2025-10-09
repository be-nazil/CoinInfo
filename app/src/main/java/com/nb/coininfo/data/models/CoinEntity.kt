package com.nb.coininfo.data.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.nb.coininfo.data.local.MoshiTypeConverters
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Keep
@Entity(tableName = "coin_table")
data class CoinEntity(
    @Json(name = "id")
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @Json(name = "name")
    @ColumnInfo(name = "name") val name: String,
    @Json(name = "symbol")
    @ColumnInfo(name = "symbol") val symbol: String,
    @Json(name = "rank")
    @ColumnInfo(name = "rank") val rank: Int,
    @Json(name = "is_new")
    @ColumnInfo(name = "is_new") val isNew: Boolean,
    @Json(name = "is_active")
    @ColumnInfo(name = "is_active") val isActive: Boolean,
    @Json(name = "type")
    @ColumnInfo(name = "type") val type: String,
    @Json(name = "contracts")
    //@TypeConverters(MoshiTypeConverters::class)
    @ColumnInfo(name = "contracts") val contracts: List<ContractEntity>? = emptyList()
)