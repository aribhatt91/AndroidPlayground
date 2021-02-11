package com.aribhatt.kotlinlearner.activitytracker.repository

import com.aribhatt.kotlinlearner.activitytracker.data.db.Run
import com.aribhatt.kotlinlearner.activitytracker.data.db.RunDAO
import javax.inject.Inject

/*
* Repository provides functions of our DAO object
* */
class TrackerRepository @Inject constructor(
    val runDAO: RunDAO
){
    suspend fun insertRun(run: Run) = runDAO.insertRun(run)

    suspend fun deleteRun(run: Run) = runDAO.deleteRun(run)

    fun getAllRunSortedByDate() = runDAO.getAllRunSortedByDate()

    fun getAllRunSortedByCalBurnt() = runDAO.getAllRunSortedByCalBurnt()

    fun getAllRunSortedByDistance() = runDAO.getAllRunSortedByDistance()

    fun getAllRunSortedByAvgSpeed() = runDAO.getAllRunSortedByAvgSpeed()

    fun getAllRunSortedByTimeTaken() = runDAO.getAllRunSortedByTimeTaken()

    fun getTotalCalBurnt() = runDAO.getTotalCalBurnt()

    fun getTotalDistance() = runDAO.getTotalDistance()

    fun getAvgSpeed() = runDAO.getAvgSpeed()

    fun getTotalTimeInMilis() = runDAO.getTotalTimeInMilis()
}