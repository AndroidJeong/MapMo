package com.jeong.mapmo.domain.usecase

import com.jeong.mapmo.domain.model.Place
import com.jeong.mapmo.domain.repository.KakaoRepository

class SearchPlacesUseCase(private val repository: KakaoRepository) {
    suspend operator fun invoke(query: String): List<Place> {
        return repository.searchPlaces(query)
    }
}