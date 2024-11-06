package com.jeong.mapmo.di

import android.content.Context
import androidx.room.Room
import com.jeong.mapmo.data.db.MemoDatabase
import kotlin.text.Typography.dagger
//
//@InstallIn(SingletonComponent::class)
//@Module
//object AppModule {
//
//    //room
//    @Provides
//    @Singleton
//    fun memoRoomdb(@ApplicationContext context: Context): MemoDatabase =
//        Room.databaseBuilder(
//            context,
//            MemoDatabase::class.java,
//            "memo_db"
//        ).fallbackToDestructiveMigration()
//            .build()
//
//}