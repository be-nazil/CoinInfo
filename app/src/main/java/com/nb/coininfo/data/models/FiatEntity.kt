package com.nb.coininfo.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FiatEntity(
    val id: String,
    val name: String,
    val symbol: String
)