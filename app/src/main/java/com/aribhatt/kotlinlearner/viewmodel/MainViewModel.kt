package com.aribhatt.kotlinlearner.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aribhatt.kotlinlearner.data.AppDatabase
import com.aribhatt.kotlinlearner.data.entities.NoteEntity
import com.aribhatt.kotlinlearner.data.SampleDataProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(app: Application): AndroidViewModel(app) {
    private lateinit var job: Job
    private val database = AppDatabase.getInstance(app)
    val notesList = database?.noteDao()?.getAll()


    fun addSampleData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val sampleData = SampleDataProvider.getNotes()
                database?.noteDao()?.insertAll(sampleData)
            }
        }
    }
    fun deleteNotes(notes: List<NoteEntity>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                database?.noteDao()?.deleteNotes(notes)
            }
        }
    }
    fun deleteAll() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                database?.noteDao()?.deleteAllNotes()
            }
        }
    }

    fun cancelJob(){
        try {
            job.cancel()
        } catch (e: UninitializedPropertyAccessException) {
            Log.i("MainViewModel", e.message ?: "")
        }
    }
}