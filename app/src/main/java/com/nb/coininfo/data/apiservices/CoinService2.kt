package com.nb.coininfo.data.apiservices

import com.nb.coininfo.data.models.CoinGraphData
import com.nb.coininfo.data.models.MarketChartResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate
import java.time.format.DateTimeFormatter

interface CoinService2 {

    @GET("coins/{id}/market_chart")
    suspend fun getMarketChart(
        @Path("id") id: String,
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("days") days: String = "1"
    ): MarketChartResponse

}