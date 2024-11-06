package com.jeong.mapmo.data.repository

import com.jeong.mapmo.data.db.MemoDatabase
import com.jeong.mapmo.data.entities.MemoEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemoRepositoryImpl @Inject constructor(
    private val db: MemoDatabase
): MemoRepository{

    override suspend fun insertMemo(memo: MemoEntity) {
        db.memoDao().insertTodo(memo)
    }

    override suspend fun deleteMemo(memo: MemoEntity) {
        db.memoDao().deleteTodo(memo)
    }

    override suspend fun getAllMemo() =
        db.memoDao().getAllTodo()



}