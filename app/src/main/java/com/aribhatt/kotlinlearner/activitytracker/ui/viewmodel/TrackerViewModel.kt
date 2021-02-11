package com.aribhatt.kotlinlearner.activitytracker.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aribhatt.kotlinlearner.activitytracker.data.SortBy
import com.aribhatt.kotlinlearner.activitytracker.data.db.Run
import com.aribhatt.kotlinlearner.activitytracker.repository.TrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

//https://proandroiddev.com/whats-new-in-hilt-and-dagger-2-31-c46b7abbc64a
@HiltViewModel
class TrackerViewModel @Inject constructor(
    private val trackerRepository: TrackerRepository
) : ViewModel() {
    private val runSortedByDate: LiveData<List<Run>> = trackerRepository?.getAllRunSortedByDate()
    private val runSortedByDistance: LiveData<List<Run>> = trackerRepository?.getAllRunSortedByDistance()
    private val runSortedByTime: LiveData<List<Run>> = trackerRepository?.getAllRunSortedByTimeTaken()
    private val runSortedByAvgSpeed: LiveData<List<Run>> = trackerRepository?.getAllRunSortedByAvgSpeed()
    private val runSortedByCals: LiveData<List<Run>> = trackerRepository?.getAllRunSortedByCalBurnt()
    val runs: MediatorLiveData<List<Run>> = MediatorLiveData<List<Run>>()
    var sortType = SortBy.DATE

    init {
        runs.addSource(runSortedByDate)  { res ->
            if(sortType == SortBy.DATE){
                res?.let {
                    runs.value = it
                }
            }
        }
        runs.addSource(runSortedByAvgSpeed)  { res ->
            if(sortType == SortBy.AVG_SPEED){
                res?.let {
                    runs.value = it
                }
            }
        }
        runs.addSource(runSortedByCals)  { res ->
            if(sortType == SortBy.CALORIES){
                res?.let {
                    runs.value = it
                }
            }
        }
        runs.addSource(runSortedByTime)  { res ->
            if(sortType == SortBy.TIME){
                res?.let {
                    runs.value = it
                }
            }
        }
        runs.addSource(runSortedByDistance)  { res ->
            if(sortType == SortBy.DISTANCE){
                res?.let {
                    runs.value = it
                }
            }
        }
    }


    fun insertRun(run: Run) = viewModelScope.launch{
        trackerRepository.insertRun(run)
    }

    fun sortRuns(sortBy: SortBy) = when(sortBy) {
        SortBy.DATE -> runSortedByDate.value?.let { runs.value = it }
        SortBy.TIME -> runSortedByTime.value?.let { runs.value = it }
        SortBy.AVG_SPEED -> runSortedByAvgSpeed.value?.let { runs.value = it }
        SortBy.DISTANCE -> runSortedByDistance.value?.let { runs.value = it }
        SortBy.CALORIES -> runSortedByCals.value?.let { runs.value = it }
    }.also {
        this.sortType = sortBy
    }
}