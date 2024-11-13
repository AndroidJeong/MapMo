package com.jeong.mapmo.ui.view

import androidx.work.ListenableWorker
import androidx.work.Worker
import com.google.android.gms.location.FusedLocationProviderClient
import com.jeong.mapmo.R
import com.jeong.mapmo.databinding.FragmentLocationPracticeBinding
import com.jeong.mapmo.util.BaseFragment
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import java.util.UUID


class LocationPractice : BaseFragment<FragmentLocationPracticeBinding>(FragmentLocationPracticeBinding::inflate),
    OnMapReadyCallback {

    private lateinit var naverMap: NaverMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationSource: FusedLocationSource

    override fun initView() {
        initMapView()
    }



    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        // 내장 위치 추적 기능
        this.naverMap.locationSource = locationSource
        // 위치를 추적하면서 카메라도 따라 움직인다.
        this.naverMap.locationTrackingMode = LocationTrackingMode.Follow

        naverMap.addOnLocationChangeListener { location ->

        }

    }

    private fun initMapView() {
        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.fl_mappractice) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.fl_mappractice, it).commit()
            }
        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

}