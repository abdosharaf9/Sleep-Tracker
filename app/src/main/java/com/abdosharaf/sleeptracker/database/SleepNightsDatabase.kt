package com.abdosharaf.sleeptracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SleepNight::class], version = 1, exportSchema = false)
abstract class SleepNightsDatabase : RoomDatabase() {

    abstract val sleepNightsDAO: SleepNightsDAO

    companion object {

        @Volatile
        private var INSTANCE: SleepNightsDatabase? = null

        fun getDatabase(context: Context): SleepNightsDatabase {
            synchronized(this) {

                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SleepNightsDatabase::class.java,
                        "sleep_database"
                    ).build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}