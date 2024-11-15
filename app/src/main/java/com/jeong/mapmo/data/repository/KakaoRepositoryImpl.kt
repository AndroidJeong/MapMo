package com.jeong.mapmo.data.repository

import com.jeong.mapmo.data.remote.KakaoSearchService
import com.jeong.mapmo.domain.model.Place
import com.jeong.mapmo.domain.repository.KakaoRepository

class KakaoRepositoryImpl(
    private val apiService: KakaoSearchService,
    private val apiKey: String
) : KakaoRepository {
    override suspend fun searchPlaces(query: String): List<Place> {
        val response = apiService.searchPlaces("KakaoAK $apiKey", query)
        return response.documents.map {
            Place(
                name = it.place_name,
                latitude = it.y.toDouble(),
                longitude = it.x.toDouble()
            )
        }
    }
}