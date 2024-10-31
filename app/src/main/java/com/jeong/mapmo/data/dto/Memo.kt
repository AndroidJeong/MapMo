package com.jeong.mapmo.data.dto

import com.jeong.mapmo.data.PriorityColor

data class Memo(val id: String, var title:String, val longitude:Double, val latitude:Double, var detail: String, var priority: PriorityColor, var checked: Boolean = false, var expand: Boolean = false, var isClamped: Boolean = false)
