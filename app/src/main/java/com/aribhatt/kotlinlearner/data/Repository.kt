package com.aribhatt.kotlinlearner.data

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.aribhatt.kotlinlearner.data.api.ApiService
import com.aribhatt.kotlinlearner.data.entities.Monster
import com.aribhatt.kotlinlearner.utils.FileHelper
import com.aribhatt.kotlinlearner.utils.NetworkHelper
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/*
* Pass application context not activity*/
class Repository(val app: Application) {
    val monsterData = MutableLiveData<List<Monster>>()
    private val listTypes = Types.newParameterizedType(List::class.java, Monster::class.java)
    init {
        CoroutineScope(Dispatchers.IO).launch {
            callWebService()
        }
        //getMonsterData()
    }
    @WorkerThread
    suspend fun callWebService() {
        if (NetworkHelper.isNetworkAvailable(app)){
            val retrofit = Retrofit.Builder()
                .baseUrl(MONSTER_API_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
            val service = retrofit.create(ApiService::class.java)
            val data = service.getData().body() ?: emptyList()
            monsterData.postValue(data)
        }
    }
    fun getMonsterData() {
        val text = FileHelper.getTextFromAsset(app, "monster_data.json")
        var moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<List<Monster>> = moshi.adapter(listTypes)
        monsterData.value =  adapter.fromJson(text) ?: emptyList()
    }
}