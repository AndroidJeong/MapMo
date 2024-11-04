package com.jeong.mapmo.ui.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.jeong.mapmo.R
import com.jeong.mapmo.databinding.FragmentMapBinding
import com.jeong.mapmo.databinding.FragmentMemoBinding
import com.jeong.mapmo.ui.adapter.MemoAdapter
import com.jeong.mapmo.util.BaseFragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(FragmentMapBinding::inflate), OnMapReadyCallback {

    private lateinit var naverMap: NaverMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationSource: FusedLocationSource
    private lateinit var mainContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainContext = context
    }

    override fun initView()  {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mainContext)
        if (!hasPermission()) {
            requestLocationPermission() // 권한 요청
        } else {
            initMapView() // 맵뷰 초기화
        }
    }


    // 위치 권한이 있을 경우 true, 없을 경우 false 반환
    private fun hasPermission(): Boolean {
        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(mainContext, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    // 위치 권한 요청 + 그때 뭘 할지
    private fun requestLocationPermission() {
        //액티비티에서는
        //ActivityCompat.requestPermissions(this, permissionsList, REQUEST_CODE)
        val multiplePermissionsLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                permissions.entries.forEach { permission ->
                    when {
                        //모든 권한 취득
                        permission.value -> {
                            Snackbar.make(
                                binding.root,
                                "${permission.value} 권한 허가",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        //권한을 다시 요구
                        shouldShowRequestPermissionRationale(permission.key) -> {
                            Snackbar.make(
                                binding.root,
                                "${permission.value}  권한 요구",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            Snackbar.make(
                                binding.root,
                                "${permission.value}  권한 거부",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

        multiplePermissionsLauncher.launch(PERMISSIONS)

    }

    //공식문서
    private fun initMapView() {
        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.fl_map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.fl_map, it).commit()
            }
        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, REQUEST_CODE_LOCATION_PERMISSION)
    }

    companion object {
        private const val REQUEST_CODE_LOCATION_PERMISSION = 1
        private val PERMISSIONS = arrayOf(
            //Manifest(android)로 import 해야함
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map
        // 내장 위치 추적 기능
        naverMap.locationSource = locationSource
        // 현재 위치 버튼 기능
        naverMap.uiSettings.isLocationButtonEnabled = true
        // 위치를 추적하면서 카메라도 따라 움직인다.
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        //사용자 현재 위치 받아오기
        var currentLocation: Location?
        if (ActivityCompat.checkSelfPermission(
                mainContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mainContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                currentLocation = location
                naverMap.locationOverlay.run {
                    isVisible = true
                    position = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
                }

                val cameraUpdate = CameraUpdate.scrollTo(
                    LatLng(
                        currentLocation!!.latitude,
                        currentLocation!!.longitude
                    )
                )
                naverMap.moveCamera(cameraUpdate)

            }



    }





}