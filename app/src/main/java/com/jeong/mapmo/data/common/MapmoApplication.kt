package com.jeong.mapmo.data.common

import android.app.Application


class MapmoApplication: Application() {

    companion object{
        private lateinit var application: MapmoApplication
        fun getApplication() = application
    }
    override fun onCreate() {
        super.onCreate()
        application = this
    }

}
