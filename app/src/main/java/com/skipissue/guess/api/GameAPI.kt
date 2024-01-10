package com.skipissue.guess.api

import com.skipissue.guess.model.VariantResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GameAPI {
    @GET("ac/")
    suspend fun getSuggestion(
        @Query("q") query: String,
        @Query("kl") language: String
    ): Response<List<VariantResponse>>
}