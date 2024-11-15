package com.jeong.mapmo.data.common

import android.Manifest
import android.app.Notification
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.jeong.mapmo.data.dto.LocationInfo

class LocationWorkManager(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: LocationInfo = LocationInfo(0.0, 0.0)



    override suspend fun doWork(): Result {

        Log.d("location", "워크매니저 시작")

        //val foregroundInfo = ForegroundInfo(1, createNotification())
        //setForegroundAsync(foregroundInfo)


        return if (getLocation(applicationContext)) {
            Log.d("location", "return if: $currentLocation")

            //work가 끝난후 반환할 데이터, 경도 위도 반환 예정
            val outPutData = Data.Builder()
                .putInt("location", 1)
                .build()

            Handler(Looper.getMainLooper()).postDelayed({
                val workRequest = OneTimeWorkRequestBuilder<LocationWorkManager>().build()
                WorkManager.getInstance(applicationContext).enqueue(workRequest)
            }, 5 * 60 * 1000)

            Result.success(outPutData)
        } else {
            Result.failure()
        }

    }

    fun getLocation(context: Context): Boolean {

       // while (!Thread.currentThread().isInterrupted) {

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

            //주기적으로 위치정보 받기
            val locationRequest =
                LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3 * 60 * 1000).apply {
                    //위치 업데이트 사이의 최소 거리
                    setMinUpdateDistanceMeters(0F)
                    //요청이 Priority.PRIORITY_HIGH_ACCURACY라면 정확도 높은 위치를 대신 전달할 수 있을 때까지 정확도가 낮은 위치 전달은 지연
                    setWaitForAccurateLocation(true)
                }.build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationAvailability(p0: LocationAvailability) {
                    super.onLocationAvailability(p0)
                }

                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                }

            }

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
                 fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
                    if (location != null){
                        Log.d("location", "location: ${location.latitude}, ${location.longitude}")
                        currentLocation.latitude = location.latitude
                        currentLocation.longitude = location.longitude
                    }
                }

                Log.d("location", "currentLocation에 담긴 데이터: $currentLocation")
                //return false
            } else {
                Log.d("location", "권한 문제로 받아올 수 없음")
               //Thread.currentThread().interrupt()
                return false
            }
            //수정 테스트 모두 끝나면 시간 조정하기
            //Thread.sleep(5 * 60 * 1000)
      //  }

        return true
    }



}
/*
작업이 쌓이는 문제(10분)
-> 이유알기 thread sleep?

sleep 안주면 계속 실행, 주면 새로운 객체 생성?
 */


/*
내 위치 가져오기
https://krrong.tistory.com/295

10분
https://developer.android.com/develop/background-work/background-tasks/persistent?hl=ko

워크매니저 vs 서비스
https://android-developer.tistory.com/16
https://velog.io/@uuranus/Android-WorkManager


https://developer.android.com/develop/background-work/background-tasks/persistent/getting-started/define-work?hl=ko#work-constraints
https://developer.android.com/develop/background-work/background-tasks/persistent/threading/coroutineworker?hl=ko

 */