package com.aribhatt.kotlinlearner.data.api

import com.aribhatt.kotlinlearner.data.entities.Monster
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("/feed/monster_data.json")
    suspend fun getData(): Response<List<Monster>>
}