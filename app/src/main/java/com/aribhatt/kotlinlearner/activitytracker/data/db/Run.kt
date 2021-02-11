package com.aribhatt.kotlinlearner.activitytracker.data.db

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.sql.Timestamp

@Entity(tableName = "running_table")
data class Run(
    var img:Bitmap? = null,
    var timestamp: Long = 0L,
    var avgSpeed: Float = 0f,
    var distance: Int = 0,
    var time: Long = 0L,
    var caloriesBurnt: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}