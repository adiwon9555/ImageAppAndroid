package com.codinginflow.imagesearchapp.ui.gallery

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.codinginflow.imagesearchapp.data.UnsplashRepository


class GalleryViewModel @ViewModelInject constructor(
    private val unsplashRepository: UnsplashRepository,
    @Assisted private val state: SavedStateHandle
): ViewModel() {
    companion object{
        private const val DEFAULT_QUERY = "cat"
        private const val CURRENT_QUERY = "currentQuery"
    }
    //This time using livedata but can be done even with flow
//    private val currentQuery = MutableLiveData(DEFAULT_QUERY)
    private val currentQuery = state.getLiveData<String>(CURRENT_QUERY,DEFAULT_QUERY)


    //switchmap gets latest data
    val photos = currentQuery.switchMap {
        unsplashRepository.getSearchResult(it).cachedIn(viewModelScope)
    }

    fun searchPhotos(query : String){
        //setting currentQuery's new data which will trigger switchMap
        currentQuery.value = query
    }




}