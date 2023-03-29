package com.abdosharaf.sleeptracker.listScreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.abdosharaf.sleeptracker.R
import com.abdosharaf.sleeptracker.database.SleepNight
import com.abdosharaf.sleeptracker.database.SleepNightsDatabase.Companion.getDatabase
import com.abdosharaf.sleeptracker.databinding.FragmentListBinding
import com.abdosharaf.sleeptracker.databinding.ItemCurrentBinding
import com.abdosharaf.sleeptracker.databinding.ItemNightBinding
import com.abdosharaf.sleeptracker.utils.Constants.TAG
import com.abdosharaf.sleeptracker.utils.getTime
import com.abdosharaf.sleeptracker.utils.getTotalTime

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var viewModel: ListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentListBinding.inflate(inflater, container, false)

        val database = getDatabase(requireContext())
        val viewModelFactory = ListViewModelFactory(database)
        viewModel = ViewModelProvider(this, viewModelFactory)[ListViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.navigateToQualityScreen.observe(viewLifecycleOwner) { id ->
            id?.let {
                findNavController().navigate(ListFragmentDirections.actionListFragmentToQualityFragment(id))
                viewModel.doneNavigating()
            }
        }

        viewModel.sleepNights.observe(viewLifecycleOwner) { nights ->
            Log.d(TAG, "List is updated, ${nights.size} ===> $nights")
            binding.list.removeAllViews()
            nights.forEach { night ->
                if(night.startTime == night.endTime) {
                    addCurrentView(night)
                } else {
                    addNightView(night)
                }
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.initializeTonight()
    }

    private fun addNightView(night: SleepNight) {
        val view = ItemNightBinding.inflate(layoutInflater, binding.list, false)
        view.tvStartTime.text = getTime(night.startTime)
        view.tvEndTime.text = getTime(night.endTime)
        view.tvTotalTime.text = getTotalTime(night.startTime, night.endTime)
        view.tvSleepQuality.text = getQuality(night.quality)
        binding.list.addView(view.root)
    }

    private fun addCurrentView(night: SleepNight) {
        val view = ItemCurrentBinding.inflate(layoutInflater, binding.list, false)
        view.tvStartTime.text = getTime(night.startTime)
        binding.list.addView(view.root)
    }

    private fun getQuality(quality: Int): String {
        return when(quality) {
            0 -> getString(R.string.very_poor)
            1 -> getString(R.string.poor)
            2 -> getString(R.string.ok)
            3 -> getString(R.string.good)
            4 -> getString(R.string.very_good)
            5 -> getString(R.string.excellent)
            else -> "--"
        }
    }
}