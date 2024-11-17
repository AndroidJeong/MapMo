package com.jeong.mapmo.data.remote

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoSearchService {
    @GET("v2/local/search/keyword.json")
    suspend fun searchPlaces(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String,
        @Query("x") longitude: Double? = null,
        @Query("y") latitude: Double? = null
    ): KakaoSearchResponse
}