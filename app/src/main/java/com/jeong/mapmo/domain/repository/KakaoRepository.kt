package com.jeong.mapmo.domain.repository

import com.jeong.mapmo.domain.model.Place

interface KakaoRepository {
    suspend fun searchPlaces(query: String): List<Place>
}