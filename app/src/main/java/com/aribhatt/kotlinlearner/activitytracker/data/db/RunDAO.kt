package com.aribhatt.kotlinlearner.activitytracker.data.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RunDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: Run)

    @Delete
    suspend fun deleteRun(run: Run)

    @Query("SELECT * from running_table ORDER BY timestamp DESC")
    fun getAllRunSortedByDate(): LiveData<List<Run>>

    @Query("SELECT * from running_table ORDER BY distance DESC")
    fun getAllRunSortedByDistance(): LiveData<List<Run>>

    @Query("SELECT * from running_table ORDER BY time DESC")
    fun getAllRunSortedByTimeTaken(): LiveData<List<Run>>

    @Query("SELECT * from running_table ORDER BY caloriesBurnt DESC")
    fun getAllRunSortedByCalBurnt(): LiveData<List<Run>>

    @Query("SELECT * from running_table ORDER BY avgSpeed DESC")
    fun getAllRunSortedByAvgSpeed(): LiveData<List<Run>>

    @Query("SELECT SUM(time) from running_table")
    fun getTotalTimeInMilis(): LiveData<Long>

    @Query("SELECT SUM(caloriesBurnt) from running_table")
    fun getTotalCalBurnt(): LiveData<Int>

    @Query("SELECT SUM(distance) from running_table")
    fun getTotalDistance(): LiveData<Int>

    @Query("SELECT AVG(avgSpeed) from running_table")
    fun getAvgSpeed(): LiveData<Float>

}