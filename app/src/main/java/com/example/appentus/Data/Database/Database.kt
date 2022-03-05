package com.example.appentus.Data.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appentus.Data.Dao.Dao
import com.example.appentus.Data.Dao.RemoteKeysDao
import com.example.appentus.Data.Dao.ImageEntity
import com.example.appentus.Data.Dao.RemoteKeys

@Database(entities = [ImageEntity::class, RemoteKeys::class],version = 1,exportSchema = false)
abstract class Database : RoomDatabase(){

    abstract fun getDao(): Dao
    abstract fun remoteKeyDao(): RemoteKeysDao
}