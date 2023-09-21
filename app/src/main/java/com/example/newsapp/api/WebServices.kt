package com.example.newsapp.api

import com.example.newsapp.api.modul.newsResponse.NewsResponse
import com.example.newsapp.api.modul.sourcesRespose.SourcesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {
    @GET("v2/top-headlines/sources")
    fun getSources(
        @Query("apiKey") key: String = ApiConstants.apiKey
    ): Call<SourcesResponse>

    @GET("v2/everything")

    fun getArticals(
        @Query("apiKey") key: String = ApiConstants.apiKey,
        @Query("sources") sources: String?
    ): Call<NewsResponse>
}