package com.kotlin.sleeptracker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "sleep_night_table")
data class SleepNight(
    @PrimaryKey(autoGenerate = true)
    var nightId : Long = 0L,

    @ColumnInfo(name = "start_time_milli")
    var startTime: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "end_time_milli")
    var endTime : Long = startTime,

    @ColumnInfo(name = "sleep_quality_rating")
    var sleepQuality : Int = -1

)