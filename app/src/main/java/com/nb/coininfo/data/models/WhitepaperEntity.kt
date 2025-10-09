package com.nb.coininfo.data.models

import androidx.room.TypeConverters
import com.nb.coininfo.data.local.MoshiTypeConverters
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class WhitepaperEntity(
    val link: String?,
    val thumbnail: String?
)