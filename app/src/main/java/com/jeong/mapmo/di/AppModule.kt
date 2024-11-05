package com.jeong.mapmo.di

import android.content.Context
import androidx.room.Room
import com.jeong.mapmo.data.db.MemoDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    //room
    @Provides
    @Singleton
    fun memoRoomdb(@ApplicationContext context: Context): MemoDatabase =
        Room.databaseBuilder(
            context,
            MemoDatabase::class.java,
            "memo_db"
        ).fallbackToDestructiveMigration()
            .build()

}