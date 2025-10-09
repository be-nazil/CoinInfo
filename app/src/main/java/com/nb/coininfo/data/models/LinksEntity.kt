package com.nb.coininfo.data.models

import androidx.annotation.Keep
import androidx.room.TypeConverters
import com.nb.coininfo.data.local.MoshiTypeConverters
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@Keep
@JsonClass(generateAdapter = true)
data class LinksEntity(
    val explorer: List<String>?,
    val facebook: List<String>?,
    val reddit: List<String>?,
    @Json(name = "source_code") val sourceCode: List<String>?,
    val website: List<String>?,
    val youtube: List<String>?
)