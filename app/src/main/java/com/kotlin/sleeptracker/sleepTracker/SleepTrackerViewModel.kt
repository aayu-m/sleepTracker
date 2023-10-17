package com.kotlin.sleeptracker.sleepTracker

import android.app.Application
import android.text.method.TransformationMethod
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.kotlin.sleeptracker.database.SleepDatabaseDao
import com.kotlin.sleeptracker.database.SleepNight
import com.kotlin.sleeptracker.formatNights
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SleepTrackerViewModel(
    val database : SleepDatabaseDao,
    val _application: Application
) : AndroidViewModel(_application ) {
    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel();
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var getTonight = MutableLiveData<SleepNight?>()
    private var nights = database.getAll()

    private val _navigateToSleepQuality = MutableLiveData<SleepNight?>()
    val navigateToSleepQuality : MutableLiveData<SleepNight?>
        get()= _navigateToSleepQuality

    private val _showSnackBar = MutableLiveData<Boolean>()
    val showSnackBar : LiveData<Boolean>
        get() = _showSnackBar

    fun doneShowingSnackBar(){
        _showSnackBar.value = false
    }

    val startVisible = getTonight.map{
        it == null
    }
    val stopVisible = getTonight.map{
        it!=null
    }
    val clearVisible = nights.map {
        it?.isNotEmpty()
    }

    init {
        initialiseTonight()
    }

    val nightString = nights.map {
        formatNights(it, _application.resources)
    }

    private fun initialiseTonight() {
        uiScope.launch {
            getTonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
       return withContext(Dispatchers.IO){
            var tonight = database.getLatestNight()
            if(tonight?.endTime != tonight?.startTime) {
                tonight = null
            }
            tonight
        }
    }

    fun onStartTracking(){
        uiScope.launch {
            var newNight = SleepNight()
            insert(newNight)
            getTonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun insert(newNight: SleepNight) {
        withContext(Dispatchers.IO) {
            database.insert(newNight)
        }
    }

    fun onStopTracking(){
        uiScope.launch {
            var oldNight = getTonight.value?: return@launch
            oldNight?.endTime = System.currentTimeMillis()
            update(oldNight)
            Log.d("stop", oldNight.endTime.toString())
            _navigateToSleepQuality.value = oldNight

        }
    }
    fun doneNavigating(){
        _navigateToSleepQuality.value = null
    }

    private suspend fun update(oldNight: SleepNight) {
        withContext(Dispatchers.IO){
            database.update(oldNight)
            Log.d("stop", "updated endtime")
        }
    }

    fun onClear(){
        uiScope.launch{
            clearNights();
            getTonight.value = null
            _showSnackBar.value = true
        }
    }

    private suspend fun clearNights() {
        withContext(Dispatchers.IO){
            database.deleteAll()
        }
    }



}