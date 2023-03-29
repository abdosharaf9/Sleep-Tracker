package com.abdosharaf.sleeptracker.qualityScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdosharaf.sleeptracker.database.SleepNight
import com.abdosharaf.sleeptracker.database.SleepNightsDatabase
import kotlinx.coroutines.launch

class QualityViewModel(private val database: SleepNightsDatabase): ViewModel() {

    private var nightId = -1L

    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack

    fun setNightId(id: Long) {
        nightId = id
    }

    private suspend fun getNight(id: Long): SleepNight? {
        return database.sleepNightsDAO.getNight(id)
    }

    private suspend fun updateNight(night: SleepNight) {
        database.sleepNightsDAO.updateNight(night)
    }

    fun setSleepQuality(quality: Int){
        viewModelScope.launch {
            val night = getNight(nightId)
            night?.let {
                night.quality = quality
                updateNight(night)
                _navigateBack.value = true
            }
        }
    }
}