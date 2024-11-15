package com.jeong.mapmo.ui.view.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jeong.mapmo.domain.usecase.SearchPlacesUseCase

class MapViewModelFactory(
    private val searchPlacesUseCase: SearchPlacesUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(searchPlacesUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}