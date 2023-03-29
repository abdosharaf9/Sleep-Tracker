package com.abdosharaf.sleeptracker.qualityScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.abdosharaf.sleeptracker.database.SleepNightsDatabase

class QualityViewModelFactory(private val database: SleepNightsDatabase) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QualityViewModel::class.java)) {
            return QualityViewModel(database) as T
        }
        throw java.lang.IllegalArgumentException("Unknown ViewModel class")
    }
}