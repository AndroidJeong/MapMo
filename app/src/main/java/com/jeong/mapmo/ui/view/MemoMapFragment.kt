package com.jeong.mapmo.ui.view

import androidx.navigation.fragment.findNavController
import com.jeong.mapmo.R
import com.jeong.mapmo.data.dto.Location
import com.jeong.mapmo.databinding.FragmentMemoMapBinding
import com.jeong.mapmo.util.BaseFragment
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource

//메모 추가 버튼 누르면 나오는 지도맵
class MemoMapFragment : BaseFragment<FragmentMemoMapBinding>(FragmentMemoMapBinding::inflate),
    OnMapReadyCallback {

    private lateinit var locationSource: FusedLocationSource
    val exampleLongtitude = 1.1
    val exampleLatitude = 1.2

    override fun initView() {

        initMapView()

        binding.fbMapLocationButton.setOnClickListener {
            val action = MemoMapFragmentDirections.actionMemoMapFragmentToMemoAddFragment(Location(1.1, 1.2))
            findNavController().navigate(action)
        }
    }

    override fun onMapReady(p0: NaverMap) {

    }

    private fun initMapView() {
        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.cl_map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.cl_map, it).commit()
            }
        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, 1)
    }


}