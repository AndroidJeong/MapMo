package com.jeong.mapmo.data.common

import android.app.Application
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager


class MapmoApplication: Application() {

    companion object{
        private lateinit var application: MapmoApplication
        fun getApplication() = application
    }
    override fun onCreate() {
        super.onCreate()
        application = this
    }




    //워크매니저 실행
    private fun workExample() {
        WorkManager.getInstance(applicationContext).cancelAllWork()

        val workerConstraints = Constraints.Builder().build()
        val workRequest = OneTimeWorkRequestBuilder<LocationWorkManager>()
            .setConstraints(workerConstraints)
            .build()
        val workManager = WorkManager.getInstance(applicationContext)

        workManager.enqueue(workRequest)
    }

}
