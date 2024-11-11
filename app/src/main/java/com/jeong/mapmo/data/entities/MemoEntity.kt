package com.jeong.mapmo.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeong.mapmo.data.common.PriorityColor

@Entity(tableName = "MemoTable")
data class MemoEntity (
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null,

    var title: String,
    var longitude: Double = 0.0,
    var latitude: Double = 0.0,
    var detail: String,
    var priority: PriorityColor = PriorityColor.RED,
    //텍스트 완료처리
    var checked: Boolean = false,
    )
