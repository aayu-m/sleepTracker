package com.kotlin.sleeptracker.sleepQuality

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import com.kotlin.sleeptracker.R
import com.kotlin.sleeptracker.database.SleepDatabase
import com.kotlin.sleeptracker.database.SleepDatabaseDao
import com.kotlin.sleeptracker.databinding.FragmentSleepQualityBinding
import com.kotlin.sleeptracker.sleepTracker.SleepTrackerViewModel

/**
 * Fragment that displays a list of clickable icons,
 * each representing a sleep quality rating.
 * Once the user taps an icon, the quality is set in the current sleepNight
 * and the database is updated.
 */
class SleepQualityFragment : Fragment() {

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepQualityBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sleep_quality, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = requireArguments().let { SleepQualityFragmentArgs.fromBundle(it) }
        val dataSource = SleepDatabase.getInstance(application).SleepDatabaseDao
        val viewModelFactory = SleepQualityViewModelFactory(arguments.SleepNightKey, dataSource)
        val SleepQualityViewModel = ViewModelProvider(this, viewModelFactory)
            .get(SleepQualityViewModel::class.java)
        binding.sleepQualityViewModel = SleepQualityViewModel
        SleepQualityViewModel.navigateToSleepTracker.observe(viewLifecycleOwner, Observer{
            if(it == true){
                this.findNavController().navigate(
                    SleepQualityFragmentDirections.actionSleepQualityFragmentToSleepTrackerFragment()
                )
                SleepQualityViewModel.doneNavigating()
            }
        })
        return binding.root
    }
}
