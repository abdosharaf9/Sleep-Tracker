package com.abdosharaf.sleeptracker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SleepNightsDAO {

    @Insert
    suspend fun insertNight(night: SleepNight)

    @Update
    suspend fun updateNight(night: SleepNight)

    @Query("SELECT * from daily_sleep_nights_table WHERE id = :key")
    suspend fun getNight(key: Long): SleepNight?

    @Query("SELECT * FROM daily_sleep_nights_table ORDER BY id DESC LIMIT 1")
    suspend fun getTonight(): SleepNight?

    @Query("SELECT * FROM daily_sleep_nights_table ORDER BY id DESC")
    fun getAllNights(): LiveData<List<SleepNight>>

    @Query("DELETE FROM daily_sleep_nights_table")
    suspend fun clearAll()
}