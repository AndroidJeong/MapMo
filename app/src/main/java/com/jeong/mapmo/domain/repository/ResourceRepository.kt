package com.jeong.mapmo.domain.repository

import android.content.Context

class ResourceRepository(private val context: Context) {
    fun getString(resourceId: Int): String {
        return context.getString(resourceId)
    }
}