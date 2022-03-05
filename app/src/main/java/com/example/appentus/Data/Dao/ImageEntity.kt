package com.example.appentus.Data.Dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.io.Serializable

@Entity(tableName = "image")
data class ImageEntity(
    @PrimaryKey
    val id:String ="1",
    @Json(name = "download_url")
    val download_url:String?
):Serializable
