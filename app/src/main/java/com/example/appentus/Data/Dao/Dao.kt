package com.example.appentus.Data.Dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<ImageEntity>)

    @Query("SELECT * FROM image")
    fun getAllImages():PagingSource<Int, ImageEntity>

    @Query("DELETE FROM image")
    fun clearAll()
}