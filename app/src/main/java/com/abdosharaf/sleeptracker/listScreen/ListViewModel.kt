package com.abdosharaf.sleeptracker.listScreen

import androidx.lifecycle.*
import androidx.room.util.EMPTY_STRING_ARRAY
import com.abdosharaf.sleeptracker.database.SleepNight
import com.abdosharaf.sleeptracker.database.SleepNightsDatabase
import kotlinx.coroutines.launch

class ListViewModel(private val database: SleepNightsDatabase) : ViewModel() {

    val sleepNights = database.sleepNightsDAO.getAllNights()

    private val _tonight = MutableLiveData<SleepNight?>()

    private val _navigateToQualityScreen = MutableLiveData<Long?>()
    val navigateToQualityScreen: LiveData<Long?>
        get() = _navigateToQualityScreen

    private val _showSnackBar = MutableLiveData<Boolean>()
    val showSnackBar: LiveData<Boolean>
        get() = _showSnackBar


    val showStartButton = Transformations.map(_tonight) {
        it == null
    }

    val showStopButton = Transformations.map(_tonight) {
        it != null
    }

    val showClearButton = Transformations.map(sleepNights) {
        it.isNotEmpty()
    }

    init {
        initializeTonight()
    }

    private suspend fun getTonight() {
        _tonight.value = database.sleepNightsDAO.getTonight()

        _tonight.value?.let {
            if(it.startTime != it.endTime){
                _tonight.value = null
            }
        }
    }

    fun initializeTonight() {
        viewModelScope.launch {
            getTonight()
        }
    }

    private suspend fun insertNewNight() {
        database.sleepNightsDAO.insertNight(SleepNight())
    }

    fun startTracking() {
        viewModelScope.launch {
            insertNewNight()
            getTonight()
        }
    }

    private suspend fun updateNight(night: SleepNight) {
        database.sleepNightsDAO.updateNight(night)
    }

    fun stopTracking() {
        viewModelScope.launch {
            _tonight.value?.let {
                it.endTime = System.currentTimeMillis()
                updateNight(it)
                _navigateToQualityScreen.value = it.id
            }
        }
    }

    private suspend fun clearNights() {
        database.sleepNightsDAO.clearAll()
    }

    fun clearAll() {
        viewModelScope.launch {
            clearNights()
            getTonight()
        }
        _showSnackBar.value = true
    }

    fun doneNavigating() {
        _navigateToQualityScreen.value = null
    }

    fun doneSnackBar() {
        _showSnackBar.value = false
    }

}