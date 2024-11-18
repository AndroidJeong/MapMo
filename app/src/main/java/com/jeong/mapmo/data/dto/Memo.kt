package com.jeong.mapmo.data.dto

import android.os.Parcelable
import com.jeong.mapmo.data.common.PriorityColor
import kotlinx.parcelize.Parcelize

//수정 상수 수정하기
@Parcelize
data class Memo(
    var title: String,
    var location: String,
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,
    var detail: String,
    var priority: PriorityColor = PriorityColor.RED,
    var locationName: String = "",
    var checked: Boolean = false,
    var expand: Boolean = false,
    var isClamped: Boolean = false
) : Parcelable
