package com.jeong.mapmo.data.repository

import com.jeong.mapmo.data.common.MemoResult
import com.jeong.mapmo.data.entities.MemoEntity
import kotlinx.coroutines.flow.Flow

interface MemoRepository {

    // Room
    fun insertMemo(memo: MemoEntity)

    fun deleteMemo(title: String)

    fun updateMemo(checked: Boolean, title: String)

    fun getAllMemo(): Flow<List<MemoEntity>>

}
