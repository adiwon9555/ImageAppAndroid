package com.codinginflow.imagesearchapp.di

import android.app.Application
import com.codinginflow.imagesearchapp.api.UnsplashApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit =
        Retrofit.Builder()
            .baseUrl(UnsplashApi.BASE_URL)
                //This is for conversion of the json received
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun provideUnsplashApi(retrofit : Retrofit) : UnsplashApi =
        retrofit.create(UnsplashApi::class.java)
}