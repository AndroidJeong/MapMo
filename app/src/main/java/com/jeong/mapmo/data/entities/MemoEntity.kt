package com.jeong.mapmo.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeong.mapmo.data.common.PriorityColor

@Entity(tableName = "MemoTable")
data class MemoEntity (
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null,
    //@ColumnInfo(name = "title")
    var title: String,
    var longitude: Double = 0.0,
    var latitude: Double = 0.0,
    var detail: String,
    var priority: PriorityColor = PriorityColor.RED,
    var checked: Boolean = false,
    var expand: Boolean = false,
    var isClamped: Boolean = false
    )
//수정? expand, isClamped는 필요없는 부분인데
