package com.nb.coininfo.data.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.nb.coininfo.data.local.MoshiTypeConverters
import com.nb.coininfo.ui.utils.convertDataClassToJsonString
import com.nb.coininfo.ui.utils.convertJsonStringToDataClass
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@Keep
@Serializable
@JsonClass(generateAdapter = true)
data class CoinDetails(

    @Json(name = "id")
    @SerializedName("id")
    val id: String,

    @Json(name = "name")
    @SerializedName("name")
    val name: String,

    @Json(name = "symbol")
    @SerializedName("symbol")
    val symbol: String,

    @Json(name = "rank")
    @SerializedName("rank")
    val rank: Int,

    @Json(name = "is_new")
    @SerializedName("is_new")
    val isNew: Boolean? = null,

    @Json(name = "is_active")
    @SerializedName("is_active")
    val isActive: Boolean? = null,

    @Json(name = "description")
    @SerializedName("description")
    val description: String? = null,

    @Json(name = "message")
    @SerializedName("message")
    val message: String? = null,

    @Json(name = "open_source")
    @SerializedName("open_source")
    val openSource: Boolean? = null,

    @Json(name = "started_at")
    @SerializedName("started_at")
    val startedAt: String? = null,

    @Json(name = "development_status")
    @SerializedName("development_status")
    val developmentStatus: String? = null,

    @Json(name = "hardware_wallet")
    @SerializedName("hardware_wallet")
    val hardwareWallet: Boolean? = null,

    @Json(name = "proof_type")
    @SerializedName("proof_type")
    val proofType: String? = null,

    @Json(name = "org_structure")
    @SerializedName("org_structure")
    val organizationStructure: String? = null,

    @Json(name = "hash_algorithm")
    @SerializedName("hash_algorithm")
    val algorithm: String? = null,

    @Json(name = "contracts")
    @SerializedName("contracts")
    val contracts: List<ContractEntity>? = null,

    @Json(name = "tags")
    @SerializedName("tags")
    val tags: List<TagEntity>? = null,

    @Json(name = "type")
    @SerializedName("type")
    val type: String? = null,

    @Json(name = "team")
    @SerializedName("team")
    val team: List<TeamMemberEntity>? = null,

    @Json(name = "links")
    @SerializedName("links")
    val links: LinksEntity? = null,

    @Json(name = "links_extended")
    @SerializedName("links_extended")
    val linksExtended: List<LinkExtendedEntity>?,

    @Json(name = "whitepaper")
    @SerializedName("whitepaper")
    val whitepaper: WhitepaperEntity? = null,

    @Json(name = "last_data_at")
    @SerializedName("last_data_at")
    val lastDataUpdate: String? = null
) {

    fun toCoinDetailsEntity(): CoinDetailsEntity {
        return CoinDetailsEntity(
            id = this.id,
            name = this.name,
            symbol = this.symbol,
            rank = this.rank,
            isNew = this.isNew,
            isActive = this.isActive,
            description = this.description,
            message = this.message,
            openSource = this.openSource,
            startedAt = this.startedAt,
            developmentStatus = this.developmentStatus,
            hardwareWallet = this.hardwareWallet,
            proofType = this.proofType,
            organizationStructure = this.organizationStructure,
            algorithm = this.algorithm,
            contracts = convertDataClassToJsonString( this.contracts),
            tags = (this.tags),
            type = this.type,
            team = (this.team),
            links = (this.links),
            linksExtended = (this.linksExtended),
            whitepaper = (this.whitepaper),
            lastDataUpdate = this.lastDataUpdate
        )
    }

}



@Entity(tableName = "coin_details_table")
data class CoinDetailsEntity(

   @ColumnInfo(name = "id")
    @PrimaryKey
    val id: String,

   @ColumnInfo(name = "name")
    val name: String,

   @ColumnInfo(name = "symbol")
    val symbol: String,

   @ColumnInfo(name = "rank")
    val rank: Int,

   @ColumnInfo(name = "is_new")
    val isNew: Boolean? = null,

   @ColumnInfo(name = "is_active")
    val isActive: Boolean? = null,

   @ColumnInfo(name = "description")
    val description: String? = null,

   @ColumnInfo(name = "message")
    val message: String? = null,

   @ColumnInfo(name = "open_source")
    val openSource: Boolean? = null,

   @ColumnInfo(name = "started_at")
    val startedAt: String? = null,

   @ColumnInfo(name = "development_status")
    val developmentStatus: String? = null,

   @ColumnInfo(name = "hardware_wallet")
    val hardwareWallet: Boolean? = null,

   @ColumnInfo(name = "proof_type")
    val proofType: String? = null,

   @ColumnInfo(name = "org_structure")
    val organizationStructure: String? = null,

   @ColumnInfo(name = "hash_algorithm")
    val algorithm: String? = null,

   @ColumnInfo(name = "contracts")
    val contracts: String? = null,

   @Json(name = "tags")
   @ColumnInfo(name = "tags")
    val tags: List<TagEntity>? = null,

   @ColumnInfo(name = "type")
    val type: String? = null,

   @Json(name = "team")
   @ColumnInfo(name = "team")
    val team: List<TeamMemberEntity>? = null,

   @Json(name = "links")
   @ColumnInfo(name = "links")
   val links: LinksEntity? = null,

   @Json(name = "links_extended")
   @ColumnInfo(name = "links_extended")
    val linksExtended: List<LinkExtendedEntity>?,

   @Json(name = "whitepaper")
   @ColumnInfo(name = "whitepaper")
    val whitepaper: WhitepaperEntity? = null,

   @ColumnInfo(name = "last_data_at")
    val lastDataUpdate: String? = null
) {

    fun toCoinDetails(): CoinDetails {
        return CoinDetails(
            id = this.id,
            name = this.name,
            symbol = this.symbol,
            rank = this.rank,
            isNew = this.isNew,
            isActive = this.isActive,
            description = this.description,
            message = this.message,
            openSource = this.openSource,
            startedAt = this.startedAt,
            developmentStatus = this.developmentStatus,
            hardwareWallet = this.hardwareWallet,
            proofType = this.proofType,
            organizationStructure = this.organizationStructure,
            algorithm = this.algorithm,
            contracts = convertJsonStringToDataClass(this.contracts.orEmpty()) as List<ContractEntity>?,
            tags = this.tags,//convertJsonStringToDataClass(this.tags.orEmpty()) as List<TagEntity>?,
            type = this.type,
            team = this.team,//convertJsonStringToDataClass(this.team.orEmpty()) as List<TeamMemberEntity>?,
            links = this.links,//convertJsonStringToDataClass(this.links.orEmpty()) as LinksEntity?,
            linksExtended = this.linksExtended,//convertJsonStringToDataClass(this.linksExtended.orEmpty()) as List<LinkExtendedEntity>?,
            whitepaper = this.whitepaper,//convertJsonStringToDataClass(this.whitepaper.orEmpty()) as WhitepaperEntity?,
            lastDataUpdate = this.lastDataUpdate
        )
    }
}