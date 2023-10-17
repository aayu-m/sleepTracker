package com.kotlin.sleeptracker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Dao
interface SleepDatabaseDao{
 @Insert
 fun insert(sleepNight: SleepNight)

 @Update
 fun update(sleepNight: SleepNight)

 @Query ("SELECT * FROM sleep_night_table WHERE nightId = :key")
 fun get(key: Long) : SleepNight

 @Query ("DELETE FROM sleep_night_table")
 fun deleteAll()

 @Query("SELECT * FROM sleep_night_table ORDER BY nightId DESC")
 fun getAll() : LiveData<List<SleepNight>>

 @Query ("SELECT * FROM sleep_night_table ORDER BY nightId DESC LIMIT 1")
 fun getLatestNight() : SleepNight?
}