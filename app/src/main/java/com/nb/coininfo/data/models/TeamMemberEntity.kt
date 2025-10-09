package com.nb.coininfo.data.models

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Serializable
@JsonClass(generateAdapter = true)
data class TeamMemberEntity(
    val id: String,
    val name: String,
    @Json(name = "position")
    @SerializedName("position")
    val role: String?
)