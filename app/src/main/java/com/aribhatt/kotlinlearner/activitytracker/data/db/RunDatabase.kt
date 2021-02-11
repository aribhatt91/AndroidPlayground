package com.aribhatt.kotlinlearner.activitytracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Run::class],
    version = 1
)
@TypeConverters(BitmapConverter::class)
abstract class RunDatabase : RoomDatabase() {

    abstract fun getRunDao(): RunDAO
}