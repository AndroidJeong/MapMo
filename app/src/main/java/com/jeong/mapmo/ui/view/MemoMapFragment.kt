package com.jeong.mapmo.ui.view

import android.os.Build
import androidx.navigation.fragment.findNavController
import com.jeong.mapmo.R
import com.jeong.mapmo.data.common.PriorityColor
import com.jeong.mapmo.data.dto.Memo
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

    override fun initView() {

        val editData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("editData", Memo::class.java)
        } else {
            arguments?.getParcelable("editData") as? Memo
        }

        initMapView()

        binding.fbMapLocationButton.setOnClickListener {
            //수정!! 위도 경도 받아와서 수정, api를 통한 지역이름 넣어주기
            var memo  = Memo("",126.8973382, 37.4701, "", PriorityColor.RED, "",false, false, false)
            if (editData != null) {
              memo = editData
            }
            val action = MemoMapFragmentDirections.actionMemoMapFragmentToMemoAddFragment(memo)
            findNavController().navigate(action)

        }
    }

    override fun onMapReady(p0: NaverMap) {
        //editData가 not null -> Marker + 데이터 표시

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