package com.nb.coininfo.data.models

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
data class TagEntity(
    val id: String,
    val name: String,
    val description: String? = "",
    val type: String? = "",
    @Json(name = "coin_counter")
    @SerializedName("coin_counter")
    val coinCounter: Int? = 0,
    @Json(name = "ico_counter")
    @SerializedName("ico_counter")
    val icoCounter: Int? = 0,
    val coins: List<String>? = emptyList()
)