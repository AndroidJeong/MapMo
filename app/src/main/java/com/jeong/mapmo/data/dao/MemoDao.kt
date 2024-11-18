package com.jeong.mapmo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jeong.mapmo.data.entities.MemoEntity


@Dao
interface MemoDao {

    //get all
    @Query("SELECT * FROM MemoTable")
    fun getAllMemo(): MutableList<MemoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMemo(memo: MemoEntity)

    //Delete
    @Query("DELETE FROM MemoTable WHERE title = :title")
    fun deleteMemo(title: String)

    //Update
    @Query("UPDATE MemoTable SET checked = :checked WHERE title = :title")
    fun updateMemo(checked: Boolean, title: String)
}
