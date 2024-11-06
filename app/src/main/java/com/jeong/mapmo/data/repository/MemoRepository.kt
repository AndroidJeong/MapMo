package com.jeong.mapmo.data.repository

import com.jeong.mapmo.data.entities.MemoEntity

interface MemoRepository {

    // Room
    suspend fun insertMemo(memo: MemoEntity)

    suspend fun deleteMemo(memo: MemoEntity)

    suspend fun getAllMemo(): List<MemoEntity>
}