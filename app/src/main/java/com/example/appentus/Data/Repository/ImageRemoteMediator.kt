package com.example.appentus.Data.Repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.appentus.Data.Database.Database
import com.example.appentus.Data.Dao.ImageEntity
import com.example.appentus.Data.Dao.RemoteKeys
import com.example.appentus.Network.ApiService
import com.example.appentus.Network.ApiService.Companion.DEFAULT_PAGE_INDEX

@ExperimentalPagingApi
class ImageRemoteMediator constructor(
        private val db: Database,
        private val apiService: ApiService
)  : RemoteMediator<Int, ImageEntity>() {
    private val imageListDao = db.getDao()
    private val pageKeyDao = db.remoteKeyDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, ImageEntity>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }


        }
        try {
            val apiResponse = apiService.getAllImages(page)
            val resBody = apiResponse.body()
            val images = resBody
            var endOfPaginationReached = false
            images?.let {
                endOfPaginationReached = it.isEmpty()
            }
                db.withTransaction {
                    // clear all tables in the database
                    if (loadType == LoadType.REFRESH) {
                        pageKeyDao.clearAll()
                        imageListDao.clearAll()
                    }
                    val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1
                    val keys = images?.map {
                        RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                    }
                    // inserting the keys into ROOM DB
                    if (keys != null) {
                        pageKeyDao.insertRemote(keys)
                    }
                    // inserting the Images into ROOM DB
                    if (images != null) {
                        imageListDao.insert(images)
                    }
                }
                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } catch (e: Exception) {
                return MediatorResult.Error(e)
            }
        }
    private  fun getRemoteKeyForLastItem(state: PagingState<Int, ImageEntity>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { item ->
                // Get the remote keys of the last item retrieved
                pageKeyDao.getRemoteKeys(item.id)
            }
    }

    private  fun getRemoteKeyForFirstItem(state: PagingState<Int, ImageEntity>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { item ->
                // Get the remote keys of the first items retrieved
                pageKeyDao.getRemoteKeys(item.id)
            }
    }

    private  fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, ImageEntity>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                pageKeyDao.getRemoteKeys(id)

            }
        }
    }
}
