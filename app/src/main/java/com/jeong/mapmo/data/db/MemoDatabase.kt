package com.jeong.mapmo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jeong.mapmo.data.dao.MemoDao
import com.jeong.mapmo.data.entities.MemoEntity

@Database(
    entities = [
       MemoEntity::class
    ], version = 1
)

//companion object로 싱글톤 db 만들어서 바로 불러서 썼던 것과 다르게,
//hilt에서는 추상클래스와 추상메소드로 만들고 module에서 구현해 준다음
//실제 사용하는 곳에서는 inject 해서 사용한다.
abstract class MemoDatabase: RoomDatabase() {
    abstract fun memoDao(): MemoDao
}