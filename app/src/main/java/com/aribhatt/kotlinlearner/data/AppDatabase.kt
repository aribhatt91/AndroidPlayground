package com.aribhatt.kotlinlearner.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aribhatt.kotlinlearner.data.dao.NoteDao
import com.aribhatt.kotlinlearner.data.entities.NoteEntity
import com.aribhatt.kotlinlearner.utils.DateConverter

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao?

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if(INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "plainnotes.db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}