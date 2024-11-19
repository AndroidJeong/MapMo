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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jeong.mapmo.R
import com.jeong.mapmo.data.common.PriorityColor
import com.jeong.mapmo.data.dto.Memo
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
import com.naver.maps.map.util.FusedLocationSource
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
    private lateinit var locationSource: FusedLocationSource
    private val markers = mutableListOf<Marker>()
    private var naverMap: NaverMap? = null
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
        setupFab()
    }

    private fun setupRecyclerView() {
        binding.searchResultsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }

        searchAdapter.onItemClick = { place ->
            val map = naverMap
            if (map != null) {
                val cameraUpdate = CameraUpdate.scrollTo(LatLng(place.latitude, place.longitude))
                map.moveCamera(cameraUpdate)
                binding.searchResultsRecyclerView.visibility = View.GONE
                hideKeyboard()
            } else {
                Toast.makeText(requireContext(), "지도가 초기화되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
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
                    binding.searchResultsRecyclerView.visibility = View.GONE
                }
                hideKeyboard()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    if (it.isBlank()) binding.searchResultsRecyclerView.visibility = View.GONE
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
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map.apply {
            locationSource = this@MapFragment.locationSource
            uiSettings.isLocationButtonEnabled = true
            locationTrackingMode = LocationTrackingMode.Follow
        }

        moveToUserLocation()
    }

    private fun moveToUserLocation() {
        val map = naverMap ?: return

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
            if (location != null) {
                val userLatLng = LatLng(location.latitude, location.longitude)

                map.locationOverlay.apply {
                    isVisible = true
                    position = userLatLng
                }

                location.let {
                    updateUserLocationCircle(userLatLng)
                    val cameraUpdate = CameraUpdate.scrollAndZoomTo(userLatLng, 16.0)
                    map.moveCamera(cameraUpdate)
                }
            } else {
                Toast.makeText(requireContext(), "현재 위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUserLocationCircle(location: LatLng) {
        val yellow = ContextCompat.getColor(requireContext(), R.color.yellow)

        locationCircle = CircleOverlay().apply {
            center = location
            color = ColorUtils.setAlphaComponent(yellow, 30)
            outlineColor = yellow
            outlineWidth = 3
            radius = 100.0
            map = naverMap
        }
    }

    private fun displayMarkers(places: List<Place>) {
        clearMarkers()
        places.forEach { place ->
            val marker = Marker().apply {
                position = LatLng(place.latitude, place.longitude)
                icon = MarkerIcons.BLACK
                iconTintColor = ContextCompat.getColor(requireContext(), R.color.blue)
                captionText = place.name
                captionTextSize = 16f
                map = naverMap
                setOnClickListener {
                    viewModel.onMarkerClicked(place)
                    hideKeyboard()
                    Toast.makeText(requireContext(), "메모를 작성해주세요.", Toast.LENGTH_SHORT).show()
                    true
                }
            }
            markers.add(marker)
        }
    }

    private fun clearMarkers() {
        markers.forEach { it.map = null }
        markers.clear()
    }

    private fun setupFab() {
        binding.fabMemoAdd.setOnClickListener {
            viewModel.onFabClicked()
            val memo = Memo(
                "",
                viewModel.selectedMarkerName.value.toString(),
                viewModel.selectedLongitude.value ?: 0.0,
                viewModel.selectedLatitude.value ?: 0.0,
                "",
                PriorityColor.RED,
                viewModel.selectedMarkerName.value.toString(),
                false,
                false
            )
            val action = MapFragmentDirections.actionMapFragmentToMemoAddFragment(memo)
            findNavController().navigate(action)
        }
    }

    private fun observeViewModel() {
        viewModel.places.observe(viewLifecycleOwner) { places ->
            displayMarkers(places)
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        viewModel.fabVisible.observe(viewLifecycleOwner) {
            binding.fabMemoAdd.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        resetSearchState()
    }

    private fun resetSearchState() {
        binding.searchResultsRecyclerView.visibility = View.GONE
        binding.searchBar.setQuery("", false)
        binding.searchBar.clearFocus()
        searchAdapter.submitList(emptyList())
        viewModel.clearPlaces()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}