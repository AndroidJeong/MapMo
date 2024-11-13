package com.jeong.mapmo.data.common

import java.math.RoundingMode
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

object DistanceManager {
    fun distance(preLat: Double?, preLng: Double?, postLat: Double?, postLng: Double?): String {
        if (preLat == null || preLng == null || postLat == null || postLng == null) {
            return ""
        }

        val result =  (6371000 * acos(
            cos(compareRadians(preLat))
                    * cos(compareRadians(postLat))
                    * cos(compareRadians(postLng) - compareRadians(preLng))
                    + sin(compareRadians(preLat)) * sin(compareRadians(postLat))
        )).toBigDecimal().setScale(1, RoundingMode.HALF_UP)

        return "$result m"
    }

    private fun compareRadians(degrees: Double): Double {
        return Math.toRadians(degrees)
    }
}
// val distance = DistanceManager.distance(preLat, preLng, postLat, postLng)