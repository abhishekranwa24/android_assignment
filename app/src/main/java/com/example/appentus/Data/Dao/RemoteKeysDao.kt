package com.example.appentus.Data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRemote(list: List<RemoteKeys>)

    @Query("SELECT * FROM remoteKey WHERE id LIKE :id")
    fun getRemoteKeys(id:String) : RemoteKeys?

    @Query("DELETE FROM remoteKey")
    fun clearAll()
}