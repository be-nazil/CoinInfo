package com.nb.coininfo.data.local

import androidx.annotation.NonNull
import androidx.room.TypeConverter
import com.nb.coininfo.data.models.ContractEntity
import com.nb.coininfo.data.models.LinkExtendedEntity
import com.nb.coininfo.data.models.LinksEntity
import com.nb.coininfo.data.models.TagEntity
import com.nb.coininfo.data.models.TeamMemberEntity
import com.nb.coininfo.data.models.WhitepaperEntity
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MoshiTypeConverters {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @TypeConverter
    fun fromContractEntityList(list: List<ContractEntity>): String {
        return moshi.toJson(list)
    }
    @TypeConverter
    fun toContractEntityList(json: String): List<ContractEntity>? {
        return moshi.fromJson(json)
    }

    @TypeConverter
    fun fromTagEntityList(list: List<TagEntity>): String {
        return moshi.toJson(list)
    }
    @TypeConverter
    fun toTagEntityList(json: String): List<TagEntity>? {
        return moshi.fromJson(json)
    }


    @TypeConverter
    fun fromTeamMemberEntityList(list: List<TeamMemberEntity>): String {
        return moshi.toJson(list)
    }
    @TypeConverter
    fun toTeamMemberEntityList(json: String): List<TeamMemberEntity>? {
        return moshi.fromJson(json)
    }

    @TypeConverter
    fun fromLinkExtendedEntityList(list: List<LinkExtendedEntity>): String {
        return moshi.toJson(list)
    }
    @TypeConverter
    fun toLinkExtendedEntityList(json: String): List<LinkExtendedEntity>? {
        return moshi.fromJson(json)
    }

    private val linksAdapter = moshi.adapter(LinksEntity::class.java)

    @TypeConverter
    fun fromLinksEntityList(list: LinksEntity): String {
        return linksAdapter.toJson(list)
    }

    @TypeConverter
    fun toLinksEntityList(json: String): LinksEntity? {
        return linksAdapter.fromJson(json)
    }

    private val paperAdapter = moshi.adapter(WhitepaperEntity::class.java)
    @TypeConverter
    fun fromWhitePaperEntity(list: WhitepaperEntity): String {
        return paperAdapter.toJson(list)
    }
    @TypeConverter
    fun toWhitePaperEntity(json: String): WhitepaperEntity? {
        return paperAdapter.fromJson(json)
    }

    @TypeConverter
    fun fromListString(list: List<String>): String? {
        return moshi.toJson(list)
    }

    @TypeConverter
    fun toListString(json: String?): List<String>? {
        if (json.isNullOrEmpty()) return null
        return moshi.fromJson(json)
    }

}

inline fun <reified E> Moshi.toJson(list: E): String {
    val adapter: JsonAdapter<E> = this.adapter(
        Types.newParameterizedType(E::class.java, E::class.java)
    )
    return adapter.toJson(list)

}

inline fun <reified E> Moshi.fromJSON(json: String): E {
    val adapter: JsonAdapter<E> = this.adapter(
        Types.newParameterizedType(String::class.java, E::class.java)
    )

    return adapter.fromJson(json) as E

}

inline fun <reified E> Moshi.toJson(list: List<E>): String {
    val adapter: JsonAdapter<List<E>> = this.adapter(
        Types.newParameterizedType(List::class.java, E::class.java)
    )
    return adapter.toJson(list)

}

inline fun <reified E> Moshi.fromJson(json: String): List<E> {
    val adapter: JsonAdapter<List<E>> = this.adapter(
        Types.newParameterizedType(List::class.java, E::class.java)
    )

    return adapter.fromJson(json) as List<E>

}