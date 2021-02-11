package com.aribhatt.kotlinlearner.activitytracker.utils

import android.location.Location
import androidx.recyclerview.widget.DiffUtil
import com.aribhatt.kotlinlearner.activitytracker.service.PolyLine
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

object Utility {
    //https://keisan.casio.com/exec/system/1350891527
    fun getFormattedStopwatchTime(ms: Long, includeMillis: Boolean = false): String{
        var millis = ms
        val hrs = TimeUnit.MILLISECONDS.toHours(millis)
        millis -= TimeUnit.HOURS.toMillis(hrs)
        val mins = TimeUnit.MILLISECONDS.toMinutes(millis)
        millis -= TimeUnit.MINUTES.toMillis(mins)
        val secs = TimeUnit.MILLISECONDS.toSeconds(millis)
        millis -= TimeUnit.SECONDS.toMillis(secs)
        var res = "${if(hrs < 10) "0" else ""}$hrs" + ":" + "${if(mins < 10) "0" else ""}$mins" + ":" + "${if(secs < 10) "0" else ""}$secs"

        if(includeMillis) {
            res += ":${if(millis < 10) "0" else ""}$millis"
        }
        return res
    }

    fun calculatePolylineLength(polyLine: PolyLine): Float {
        var distance = 0f;

        for (i in 0..polyLine.size - 2){
            val pos1 = polyLine[i]
            val pos2 = polyLine[i+1]
            val result = FloatArray(1)
            Location.distanceBetween(pos1.latitude, pos1.longitude, pos2.latitude, pos2.longitude, result)
            distance += result[0]
        }

        return distance
    }

    fun calculateBmi(){}

    fun calculateDailyCalorieIntake(){}

    fun caloriesBurnt(distanceInMeters: Float, weight: Float = 72f): Int = ((distanceInMeters/1000f) * weight).toInt()


}