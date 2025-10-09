package com.nb.coininfo.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsEntity(
    val title: String,
    val lead: String,
    val url: String,
    @Json(name = "news_date") val newsDate: String
)