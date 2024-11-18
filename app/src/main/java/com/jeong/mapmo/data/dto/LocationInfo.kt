package com.jeong.mapmo.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationInfo(
    var longitude: Double,
    var latitude: Double
):Parcelable
