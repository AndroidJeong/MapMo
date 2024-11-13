package com.jeong.mapmo.data.repository

import android.util.Log
import com.jeong.mapmo.data.common.MapmoApplication
import com.jeong.mapmo.data.common.MemoResult
import com.jeong.mapmo.data.db.AppDatabase
import com.jeong.mapmo.data.entities.MemoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow


class MemoRepositoryImpl(
    //private val db: MemoDatabase
) : MemoRepository {

    //수정
    val db = AppDatabase.getInstance(MapmoApplication.getApplication())!!
    val memoDao = db.getTodoDao()

    override fun insertMemo(memo: MemoEntity) {
        memoDao.insertMemo(memo)
        //db.memoDao().insertMemo(memo)
    }

    override fun deleteMemo(title: String) {
        memoDao.deleteMemo(title)
        //db.memoDao().deleteMemo(memo)
    }

    override fun updateMemo(checked: Boolean, title: String) {
        memoDao.updateMemo(checked, title)
        //db.memoDao().updateMemo(memo)
    }

    override fun getAllMemo(): Flow<List<MemoEntity>> = flow {
        emit(memoDao.getAllMemo())
    }.catch { exception -> exception }

}


