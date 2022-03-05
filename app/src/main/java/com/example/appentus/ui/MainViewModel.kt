package com.example.appentus.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.appentus.Data.Database.Database
import com.example.appentus.Data.Dao.ImageEntity
import com.example.appentus.Data.Repository.ImageRemoteMediator
import com.example.appentus.Network.ApiService
import com.example.appentus.Network.ApiService.Companion.QUERY_PER_PAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel  @Inject constructor(private val db: Database, private val apiService: ApiService) : ViewModel() {

    @ExperimentalPagingApi
    fun getAllImages() : Flow<PagingData<ImageEntity>> = Pager(
        config = PagingConfig(pageSize = QUERY_PER_PAGE,prefetchDistance = 2),
        pagingSourceFactory = {db.getDao().getAllImages()},
        remoteMediator = ImageRemoteMediator(db,apiService)
    ).flow.cachedIn(viewModelScope)
}