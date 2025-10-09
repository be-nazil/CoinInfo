package com.nb.coininfo.data.apiservices

import com.nb.coininfo.data.models.SearchEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("search/?c=currencies,icos,people,tags")
    suspend fun getSearches(@Query("q") query: String): SearchEntity
}