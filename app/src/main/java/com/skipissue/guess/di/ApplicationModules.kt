package com.skipissue.guess.di

import com.skipissue.guess.api.GameAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModules {
    @Provides
    @Singleton
    fun provideGameApi(okHttpClient: OkHttpClient): GameAPI {
        return Retrofit.Builder()
            .baseUrl("https://duckduckgo.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create()
    }
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient()
    }

}