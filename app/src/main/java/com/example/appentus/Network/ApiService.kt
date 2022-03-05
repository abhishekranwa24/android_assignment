package com.example.appentus.Network

import com.example.appentus.Data.Dao.ImageEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object{
        const val BASE_URL = "https://picsum.photos/"
        const val TOTAL_QUERY = 20
        const val DEFAULT_PAGE_INDEX = 1
        const val QUERY_PER_PAGE = 30
    }

    @GET("v2/list")
    suspend fun getAllImages(
        @Query("page") page:Int = 1,
        @Query("limit") limit: Int = TOTAL_QUERY
    ) : Response<List<ImageEntity>>
}