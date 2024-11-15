package com.jeong.mapmo.data.common

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.jeong.mapmo.R
import com.jeong.mapmo.data.dto.LocationInfo
import com.jeong.mapmo.data.dto.Memo
import com.jeong.mapmo.data.repository.MemoRepositoryImpl
import com.jeong.mapmo.ui.view.NotificationActivity
import com.jeong.mapmo.util.Constants.CHANNEL_ID
import com.jeong.mapmo.util.Constants.CHANNEL_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class LocationService : Service() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: LocationInfo = LocationInfo(0.0, 0.0)

    //질문 요런식으로 가져와도 되는지, 생성자로?
    private val repository = MemoRepositoryImpl()
    var memoList: MutableList<Memo> = mutableListOf()

    //서비스가 원격일때 구현
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("location2", "service onCreate()")
        getMemo()
        Log.d("location2", "onCreate, memoList: $memoList")

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("location2", "service onStartCommand()")
        Log.d("location2", "onStartCommand, memoList: $memoList")
        //SupervisorJob 을 쓴다면 에러발생시 부모까지 전파가 안되고 자식까지만
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            while (isActive) {
                getLocation(applicationContext)
                isSameLocation(memoList, currentLocation.latitude, currentLocation.longitude)
                //수정 시간수정하기
                delay(1 * 30 * 1000)
            }
        }
        //시스템에 의해 서비스가 종료된 경우 다시 시작
        return START_STICKY
    }


    fun getMemo() {
        Log.d("location2", "getMemo()")
        CoroutineScope(Dispatchers.IO).launch {
            repository.getAllMemo().catch {
                Log.d("location2", "room error: $it")
            }
                .collectLatest { it ->
                    it.forEach {
                        //Log.d("location2", "it: $it")
                        memoList.add(
                            Memo(
                                title = it.title,
                                longitude = it.longitude,
                                latitude = it.latitude,
                                detail = it.detail,
                                priority = it.priority,
                                checked = it.checked
                            )
                        )
                        Log.d("location2", "memo: $memoList")
                    }
                }
        }
    }


    private suspend fun getLocation(context: Context) {
        Log.d("location2", "service getLocation()")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        //런타임 권한
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //콜백이 호출될 때까지 코루틴을 일시 중단
            suspendCancellableCoroutine { continuation ->
                fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener { location ->
                        Log.d(
                            "location2",
                            "location: lat:${location.latitude}, lon:${location.longitude}"
                        )
                        currentLocation.latitude = location.latitude
                        currentLocation.longitude = location.longitude

                        continuation.resume(location, null)

                    }
                    .addOnFailureListener { e ->
                        Log.d("location2", "getLocation error: $e")
                    }
            }

        }

    }

    private fun isSameLocation(memo: MutableList<Memo>?, postLat: Double, postLng: Double) {
        memo?.forEach { memo ->
            Log.d(
                "location2",
                "메모와 거리계산:${
                    calculateDistance(
                        memo.latitude,
                        memo.longitude,
                        postLat,
                        postLng
                    )
                }, ${memo}"
            )

            displayNotification(
                calculateDistance(memo.latitude, memo.longitude, postLat, postLng),
                memo
            )

        }
    }


    private fun displayNotification(isSameDistance: Boolean, memo: Memo?) {
        Log.d("location2", "displayNotification()")

        //통지를 사용자가 터치하면 돌아갈 컴포넌트
        val notifyIntent = Intent(this, NotificationActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable("memo", memo)
        notifyIntent.putExtra("memoBundle", bundle)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        //NotificationManager: 알림을 시스템에 발생시키는 SystemService
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //android 8.0
            //NotificationChannel : 알림의 관리 단위(Android Oreo에서 추가)
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply {
                enableVibration(true)
            }
            //채널을 NotificationManager에 등록
            manager.createNotificationChannel(channel)
        }

        val nowLocationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, CHANNEL_ID)

        nowLocationBuilder.setSmallIcon(R.mipmap.ic_launcher)
            //진동 비활성화
            //.setDefaults(0)
            //.setVibrate(longArrayOf(0))
            .setContentTitle("실시간 위치를 가져오는 중")
            .setContentText("현재 위도:${currentLocation.latitude}, 경도:${currentLocation.longitude}")

        val sameLocationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, CHANNEL_ID)

        sameLocationBuilder.setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("메모에 적은 위치에 도착했습니다")
            .setContentText("${memo?.title}, 자세히 보려면 눌러주세요")
            .setContentIntent(pendingIntent)
            // 진동 활성화 (예: 2000ms 진동, 0ms 대기, 2000ms 진동)
            .setVibrate(longArrayOf(2000, 0, 2000))



        if (isSameDistance) {
            Log.d("location2", "isSameDistance")
            Log.d("location2", "알림으로 표시되는 죄표: ${currentLocation}")
            startForeground(56789, sameLocationBuilder.build())
        } else {
            Log.d("location2", "알림으로 표시되는 죄표: ${currentLocation}")
            startForeground(12345, nowLocationBuilder.build())
        }
    }

    fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Boolean {
        val R = 6371 // 지구 반지름 (단위: km)

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distance = R * c

        Log.d("location2", "거리계산: ${distance}, ${lat1}, ${lon1}, ${lat2}, ${lon2}")

        return (distance <= 1)
    }

}
/*
문제점
1. 에러??? -> 룸이 비어있거나 isEmpty()코드
2 진동을 다르게 주고 싶은데 안됨 -> 진동이 안옴...

질문
1. 권한 얻어야하는게
정확한 위치, 알람 및 리마인더 권한 ?

 */


/*


유형에서 shortservice?
https://developer.android.com/develop/background-work/services/fg-service-types?hl=ko

pending intent
https://zibro.tistory.com/2

notification
https://seedpotato.tistory.com/333


https://velog.io/@woga1999/Android-Foreground-Service-%ED%8F%AC%EA%B7%B8%EB%9D%BC%EC%9A%B4%EB%93%9C-%EC%84%9C%EB%B9%84%EC%8A%A4
 */