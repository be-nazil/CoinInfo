package com.nb.coininfo.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PersonSocialMedia(
    val url: String,
    val followers: Int?
)