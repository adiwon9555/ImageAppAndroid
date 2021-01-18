package com.codinginflow.imagesearchapp.ui.gallery

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.codinginflow.imagesearchapp.data.UnsplashRepository


class GalleryViewModel @ViewModelInject constructor(
    private val unsplashRepository: UnsplashRepository
): ViewModel() {
    companion object{
        private const val DEFAULT_QUERY = "cat"
    }
    //This time using livedata but can be done even with flow
    private val currentQuery = MutableLiveData(DEFAULT_QUERY)

    //switchmap gets latest data
    val photos = currentQuery.switchMap {
        unsplashRepository.getSearchResult(it).cachedIn(viewModelScope)
    }

    fun searchPhotos(query : String){
        //setting currentQuery's new data which will trigger switchMap
        currentQuery.value = query
    }



}