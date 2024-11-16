package com.jeong.mapmo.ui.view.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeong.mapmo.domain.model.Place
import com.jeong.mapmo.domain.usecase.SearchPlacesUseCase
import kotlinx.coroutines.launch

class MapViewModel(
    private val searchPlacesUseCase: SearchPlacesUseCase
) : ViewModel() {
    private val _places = MutableLiveData<List<Place>>()
    val places: LiveData<List<Place>> get() = _places

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun searchPlaces(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            try {
                val result = searchPlacesUseCase(query)
                _places.value = result
            } catch (e: Exception) {
                _error.value = "Failed to load places: ${e.message}"
            }
        }
    }
}