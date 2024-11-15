package com.jeong.mapmo.data.common

import android.app.Application
import android.util.Log
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager


class MapmoApplication: Application() {

    companion object{
        private lateinit var application: MapmoApplication
        fun getApplication() = application
    }
    override fun onCreate() {
        super.onCreate()
        application = this
        Log.d("location", "application onCreate")

        //workExample()
    }




    //워크매니저 실행
    private fun workExample() {
        //WorkManager.getInstance(applicationContext).cancelAllWork()
        Log.d("location", "applicationd에서 실행")

        val workerConstraints = Constraints.Builder().build()
        val workRequest = OneTimeWorkRequestBuilder<LocationWorkManager>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(workerConstraints)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }

}
