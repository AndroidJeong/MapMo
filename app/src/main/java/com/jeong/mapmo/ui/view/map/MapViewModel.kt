package com.jeong.mapmo.ui.view.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.jeong.mapmo.domain.model.Place
import com.jeong.mapmo.domain.usecase.SearchPlacesUseCase
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MapViewModel(
    private val searchPlacesUseCase: SearchPlacesUseCase
) : ViewModel() {
    private val _places = MutableLiveData<List<Place>>()
    val places: LiveData<List<Place>> get() = _places

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _fabVisible = MutableLiveData<Boolean>(false)
    val fabVisible: LiveData<Boolean> get() = _fabVisible

    private val _selectedMarkerName = MutableLiveData<String?>()
    val selectedMarkerName: LiveData<String?> get() = _selectedMarkerName

    private val _selectedLongitude = MutableLiveData<Double?>()
    val selectedLongitude: LiveData<Double?> get() = _selectedLongitude

    private val _selectedLatitude = MutableLiveData<Double?>()
    val selectedLatitude: LiveData<Double?> get() = _selectedLatitude

    private val searchQuery = MutableLiveData<String>()

    init {
        searchQuery.asFlow()
            .debounce(300)
            .distinctUntilChanged()
            .onEach { query ->
                searchPlaces(query)
            }
            .launchIn(viewModelScope)
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun clearPlaces() {
        _places.value = emptyList()
    }

    private fun searchPlaces(query: String) {
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

    fun onMarkerClicked(place: Place) {
        _selectedMarkerName.value = place.name
        _selectedLongitude.value = place.longitude
        _selectedLatitude.value = place.latitude
        _fabVisible.value = true
    }

    fun onFabClicked() {
        _fabVisible.value = false
    }
}