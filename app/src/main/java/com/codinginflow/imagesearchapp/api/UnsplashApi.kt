package com.codinginflow.imagesearchapp.api

import com.codinginflow.imagesearchapp.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

//Retrofit creates the implementation of this interface
interface UnsplashApi {

    //companion object is like static keyword
    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
        const val CLIENT_ID = BuildConfig.UNSPLASH_ACCESS_KEY
    }

    @Headers("Accept-Version: v1", "Authorization: Client-ID $CLIENT_ID")
    @GET
    fun searchPhotos(
        //Inside @Query is actually the real parameter send in api
        @Query("query") query: String,
        @Query("page") page : Int,
        @Query("per_page") perPage : Int
    ) : UnsplashResponse
}