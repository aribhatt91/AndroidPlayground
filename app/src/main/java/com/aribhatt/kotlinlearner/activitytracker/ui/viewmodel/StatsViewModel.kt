package com.aribhatt.kotlinlearner.activitytracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.aribhatt.kotlinlearner.activitytracker.repository.TrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val trackerRepository: TrackerRepository
) : ViewModel() {
    val totalTimeRun = trackerRepository.getTotalTimeInMilis()
    val totalDistance = trackerRepository.getTotalDistance()
    val totalCalories = trackerRepository.getTotalCalBurnt()
    val totalAvgSpeed = trackerRepository.getAvgSpeed()

    val runsSortedByDate = trackerRepository.getAllRunSortedByDate()



}