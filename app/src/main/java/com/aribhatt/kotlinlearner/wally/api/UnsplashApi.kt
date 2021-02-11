package com.aribhatt.kotlinlearner.wally.api

import com.aribhatt.kotlinlearner.wally.data.entity.Wallpaper
import retrofit2.Response
import retrofit2.http.GET

interface UnsplashApi {
    @GET("/photos")
    suspend fun getData(): Response<List<Wallpaper>>
}