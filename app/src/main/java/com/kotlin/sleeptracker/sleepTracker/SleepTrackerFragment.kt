package com.kotlin.sleeptracker.sleepTracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kotlin.sleeptracker.R
import com.kotlin.sleeptracker.database.SleepDatabase
import com.kotlin.sleeptracker.databinding.FragmentSleepTrackerBinding

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class SleepTrackerFragment : Fragment() {

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sleep_tracker, container, false)
        val application = requireNotNull(this.activity).application
        val datasource = SleepDatabase.getInstance(application).SleepDatabaseDao
        val viewModelFactory = SleepTrackerViewModelFactory(datasource, application)
        val sleepTrackerviewModel = ViewModelProvider(this, viewModelFactory).get(SleepTrackerViewModel::class.java)

        binding.sleepTrackerViewModel = sleepTrackerviewModel
        binding.lifecycleOwner = this

        sleepTrackerviewModel.navigateToSleepQuality.observe(viewLifecycleOwner,Observer{night ->
            night?.let {
                this.findNavController().navigate(
                    SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToSleepQualityFragment(night.nightId))
                    sleepTrackerviewModel.doneNavigating()
            }
        })

        sleepTrackerviewModel.showSnackBar.observe(viewLifecycleOwner, Observer {
            if(it == true){
                Snackbar.make(
                    requireView(),
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_SHORT
                ).show()
                sleepTrackerviewModel.doneShowingSnackBar()
            }
        })

        return binding.root
    }
}
