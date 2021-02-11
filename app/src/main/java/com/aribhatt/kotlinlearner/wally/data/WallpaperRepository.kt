package com.aribhatt.kotlinlearner.wally.data

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.aribhatt.kotlinlearner.wally.api.Constants
import com.aribhatt.kotlinlearner.wally.api.UnsplashApi
import com.aribhatt.kotlinlearner.wally.data.entity.Wallpaper
import com.aribhatt.kotlinlearner.common.utils.NetworkHelper
import com.squareup.moshi.Types
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class WallpaperRepository(val app: Application) {
    val wallyData = MutableLiveData<List<Wallpaper>>()
    private val listType = Types.newParameterizedType(
        List::class.java, Wallpaper::class.java
    )

    fun parseData(text: String) {

    }
    suspend fun callWebService() {
        if (NetworkHelper.isNetworkAvailable(app)){
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
            val service = retrofit.create(UnsplashApi::class.java)
            val data = service.getData().body() ?: emptyList()
            wallyData.postValue(data)
        }
    }
    /*fun getMonsterData() {
        val text = FileHelper.getTextFromAsset(app, "monster_data.json")
        var moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<List<Monster>> = moshi.adapter(listTypes)
        monsterData.value =  adapter.fromJson(text) ?: emptyList()
    }*/
}