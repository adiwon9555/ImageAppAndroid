package com.codinginflow.imagesearchapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.liveData
import com.codinginflow.imagesearchapp.api.UnsplashApi
import javax.inject.Inject

class UnsplashRepository @Inject constructor(
    private val unsplashApi: UnsplashApi
) {
    fun getSearchResult(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                //max size for our recycler view to hold items in memory, post that it will need to load again
                maxSize = 100,
                //enablePlaceholders to show placeholder for data that is about to show
                enablePlaceholders = false
            ),
            pagingSourceFactory =  { UnsplashPagingSource(unsplashApi, query)}
        ).liveData
}