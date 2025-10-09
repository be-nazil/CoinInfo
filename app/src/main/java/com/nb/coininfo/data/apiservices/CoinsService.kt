package com.nb.coininfo.data.apiservices

import com.nb.coininfo.data.models.*
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate
import java.time.format.DateTimeFormatter

interface CoinsService {

    @GET("global")
    suspend fun getGlobalStats(): GlobalStatsEntity?

    @GET("coins/btc-bitcoin/ohlcv/today")
    suspend fun getTodayOHLC(): List<TodayOHLCEntityItem?>?

    @GET("coins/{coin_id}/ohlcv/historical")
    suspend fun getGraphData(
        @Path("coin_id") coin_id: String,
        @Query("start") start: String = LocalDate.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE),
        @Query("interval") interval: String = "1h"
    ): List<CoinGraphData?>?

    @GET("coins/{id}")
    suspend fun getCoin(
        @Path("id") id: String
    ): CoinDetails?

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

    @GET("news/latest/")
    suspend fun getNews(@Query("limit") limit: Int): List<NewsEntity>

    @GET("rankings/top10movers/")
    suspend fun getTop10Movers(
        @Query("type") type: String
    ): TopMoversEntity

    @GET("rankings/top-movers/")
    suspend fun getMovers(
//        @Query("results_number") results: Int,
//        @Query("marketcap_limit") range: String
    ): TopMoversEntity
}