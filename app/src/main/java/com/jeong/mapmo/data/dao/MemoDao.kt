package com.jeong.mapmo.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jeong.mapmo.data.dto.Memo
import com.jeong.mapmo.data.entities.MemoEntity

@Dao
interface MemoDao {

    //get all
    @Query("SELECT * FROM MemoTable")
    fun getAllTodo() : List<MemoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTodo(memo: MemoEntity)

    @Delete
    fun deleteTodo(memo: MemoEntity)

}
