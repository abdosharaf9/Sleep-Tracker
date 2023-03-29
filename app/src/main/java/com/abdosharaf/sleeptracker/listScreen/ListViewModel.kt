package com.abdosharaf.sleeptracker.listScreen

import androidx.lifecycle.*
import com.abdosharaf.sleeptracker.database.SleepNight
import com.abdosharaf.sleeptracker.database.SleepNightsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel(private val database: SleepNightsDatabase) : ViewModel() {

    val sleepNights = database.sleepNightsDAO.getAllNights()
    private val tonight = MutableLiveData<SleepNight?>()

    private val _navigateToSleepQuality = MutableLiveData<SleepNight?>()
    val navigateToSleepQuality: LiveData<SleepNight?>
        get() = _navigateToSleepQuality

    val startButtonVisible = Transformations.map(tonight) {
        null == it
    }

    val stopButtonVisible = Transformations.map(tonight) {
        null != it
    }

    val clearButtonVisible = Transformations.map(sleepNights) {
        it?.isNotEmpty()
    }

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        viewModelScope.launch {
            tonight.value = database.sleepNightsDAO.getTonight()
            if (tonight.value?.endTime != tonight.value?.startTime) {
                tonight.value = null
            }
        }
    }

    fun insertNight() {
        viewModelScope.launch {
            database.sleepNightsDAO.insertNight(SleepNight())
            initializeTonight()
        }
    }

    fun stopTracking() {
        viewModelScope.launch {
            val oldNight = tonight.value ?: return@launch
            oldNight.endTime = System.currentTimeMillis()
            database.sleepNightsDAO.updateNight(oldNight)
            _navigateToSleepQuality.value = oldNight
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            database.sleepNightsDAO.clearAll()
        }
        tonight.value = null
    }

    fun doneNavigating() {
        _navigateToSleepQuality.value = null
    }

}