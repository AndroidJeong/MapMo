package com.jeong.mapmo.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jeong.mapmo.data.dao.MemoDao
import com.jeong.mapmo.data.entities.MemoEntity

//import androidx.room.Database
//import androidx.room.RoomDatabase
//import com.jeong.mapmo.data.dao.MemoDao
//import com.jeong.mapmo.data.entities.MemoEntity
//
//@Database(
//    entities = [
//       MemoEntity::class
//    ], version = 1
//)
//
////companion object로 싱글톤 db 만들어서 바로 불러서 썼던 것과 다르게,
////hilt에서는 추상클래스와 추상메소드로 만들고 module에서 구현해 준다음
////실제 사용하는 곳에서는 inject 해서 사용한다.
//abstract class MemoDatabase: RoomDatabase() {
//    abstract fun memoDao(): MemoDao
//}

@Database(entities = [MemoEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getTodoDao() : MemoDao

    companion object {
        val databaseName = "db_memo"
        var appDatabase : AppDatabase? = null

        fun getInstance(context : Context) : AppDatabase? {
            if(appDatabase == null){
                appDatabase = Room.databaseBuilder(context,
                    AppDatabase::class.java,
                    databaseName).
                    //마이그레이션이 실패할 때 db테이블 재생성, 데이터 사라질 수 있음
                fallbackToDestructiveMigration()
                    .build()

            }
            return appDatabase
        }


    }

}



