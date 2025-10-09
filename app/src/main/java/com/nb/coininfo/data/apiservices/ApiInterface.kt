package com.nb.coininfo.data.apiservices

import com.nb.coininfo.data.models.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("global")
    suspend fun getGlobalStats(): GlobalStatsEntity

    @GET("coins/{id}")
    suspend fun getCoin(
        @Path("id") id: String
    ): CoinDetails

    @GET("coins")
    suspend fun getCoins(
        @Query("additional_fields") additionalFields: String? = null
    ): List<CoinEntity>

    @GET("coins/{id}/events/")
    suspend fun getEvents(
        @Path("id") id: String
    ): List<EventEntity>

    @GET("coins/{id}/exchanges/")
    suspend fun getExchanges(
        @Path("id") id: String
    ): List<ExchangeEntity>

    @GET("coins/{id}/markets/")
    suspend fun getMarkets(
        @Path("id") id: String,
        @Query("quotes") quotes: String
    ): List<MarketEntity>

    @GET("coins/{id}/twitter/")
    suspend fun getTweets(
        @Path("id") id: String
    ): List<TweetEntity>

    @POST("coins/{id}/events")
    suspend fun addEvent(
        @Path("id") cryptoId: String,
        @Body event: EventEntity
    )
}