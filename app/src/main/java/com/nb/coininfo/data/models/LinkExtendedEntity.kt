package com.nb.coininfo.data.models

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@Keep
@JsonClass(generateAdapter = true)
data class LinkExtendedEntity(
    val url: String,
    val type: String,
    val stats: Map<String, Int>?
)