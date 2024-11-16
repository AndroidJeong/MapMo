package com.jeong.mapmo.ui.view.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jeong.mapmo.R
import com.jeong.mapmo.data.remote.KakaoSearchService
import com.jeong.mapmo.data.remote.RetrofitClient
import com.jeong.mapmo.data.repository.KakaoRepositoryImpl
import com.jeong.mapmo.databinding.FragmentMapBinding
import com.jeong.mapmo.domain.model.Place
import com.jeong.mapmo.domain.usecase.SearchPlacesUseCase
import com.jeong.mapmo.ui.view.map.component.SearchResultAdapter
import com.jeong.mapmo.util.BaseFragment
import com.jeong.mapmo.util.Constants.API_KEY
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons

class MapFragment : BaseFragment<FragmentMapBinding>(FragmentMapBinding::inflate),
    OnMapReadyCallback {

    private val viewModel: MapViewModel by viewModels {
        val apiService = RetrofitClient.create<KakaoSearchService>()
        val repository = KakaoRepositoryImpl(apiService, API_KEY)
        val useCase = SearchPlacesUseCase(repository)
        MapViewModelFactory(useCase)
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var naverMap: NaverMap
    private val markers = mutableListOf<Marker>()
    private var locationCircle: CircleOverlay? = null
    private val searchAdapter by lazy { SearchResultAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        observeViewModel()
    }

    override fun initView() {
        setupRecyclerView()
        setupSearchView()
        setupMapFragment()
    }

    private fun setupRecyclerView() {
        binding.searchResultsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }
        searchAdapter.onItemClick = { place ->
            val cameraUpdate = CameraUpdate.scrollTo(LatLng(place.latitude, place.longitude))
            naverMap.moveCamera(cameraUpdate)
            binding.searchResultsRecyclerView.visibility = View.GONE

        }
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun setupSearchView() {
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.setSearchQuery(it)
                }
                hideKeyboard()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.setSearchQuery(it)
                }
                return false
            }
        })

        viewModel.places.observe(viewLifecycleOwner) { places ->
            if (places.isNullOrEmpty()) {
                binding.searchResultsRecyclerView.visibility = View.GONE
            } else {
                binding.searchResultsRecyclerView.visibility = View.VISIBLE
                searchAdapter.submitList(places)
            }
        }
    }

    private fun setupMapFragment() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.container_map) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.container_map, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map
        naverMap.uiSettings.isLocationButtonEnabled = true
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
        moveToUserLocation()
    }

    private fun moveToUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val userLatLng = LatLng(it.latitude, it.longitude)
                updateUserLocationCircle(userLatLng)
                val cameraUpdate = CameraUpdate.scrollAndZoomTo(userLatLng, 16.0)
                naverMap.moveCamera(cameraUpdate)
            }
        }
    }

    private fun updateUserLocationCircle(location: LatLng) {
        val yellow = ContextCompat.getColor(requireContext(), R.color.yellow)
        if (locationCircle == null) {
            locationCircle = CircleOverlay().apply {
                center = location
                color = ColorUtils.setAlphaComponent(yellow, 30)
                outlineColor = yellow
                outlineWidth = 3
                radius = 50.0
                map = naverMap
            }
        } else {
            locationCircle?.center = location
        }
    }

    private fun displayMarkers(places: List<Place>) {
        clearMarkers()
        places.forEach { place ->
            val marker = Marker().apply {
                position = LatLng(place.latitude, place.longitude)
                icon = MarkerIcons.BLACK
                iconTintColor = ContextCompat.getColor(requireContext(), R.color.yellow)
                captionText = place.name
                map = naverMap
            }
            markers.add(marker)
        }
    }

    private fun clearMarkers() {
        markers.forEach { it.map = null }
        markers.clear()
    }

    private fun observeViewModel() {
        viewModel.places.observe(viewLifecycleOwner) { places ->
            displayMarkers(places)
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}