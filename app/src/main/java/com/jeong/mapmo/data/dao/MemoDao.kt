package com.jeong.mapmo.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jeong.mapmo.data.entities.MemoEntity

@Dao
interface MemoDao {

    //get all
    @Query("SELECT * FROM MemoTable")
    fun getAllMemo() : MutableList<MemoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMemo(memo: MemoEntity)

    @Delete
    fun deleteMemo(memo: MemoEntity)

    @Update
    fun updateMemo(memo: MemoEntity)

}
