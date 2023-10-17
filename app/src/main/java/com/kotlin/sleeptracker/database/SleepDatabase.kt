package com.kotlin.sleeptracker.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SleepNight::class], version = 1, exportSchema = true)
abstract class SleepDatabase : RoomDatabase() {

    abstract val SleepDatabaseDao : SleepDatabaseDao
    companion object{
        @Volatile
        private var INSTANCE :SleepDatabase?=null

        fun getInstance(context: Context) :SleepDatabase{
            synchronized(this) {
            var Instance = INSTANCE

            if(Instance ==null){
                Instance = Room.databaseBuilder(
                    context.applicationContext,
                    SleepDatabase::class.java,
                    "sleep_history_database"
                ).fallbackToDestructiveMigration()
                    .build()

            }
            return Instance}
        }
    }
}