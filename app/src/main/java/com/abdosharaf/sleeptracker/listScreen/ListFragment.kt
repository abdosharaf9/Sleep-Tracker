package com.abdosharaf.sleeptracker.listScreen

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.abdosharaf.sleeptracker.R
import com.abdosharaf.sleeptracker.database.SleepNight
import com.abdosharaf.sleeptracker.database.SleepNightsDatabase.Companion.getDatabase
import com.abdosharaf.sleeptracker.databinding.FragmentListBinding
import com.abdosharaf.sleeptracker.databinding.ItemCurrentBinding
import com.abdosharaf.sleeptracker.databinding.ItemNightBinding
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

        // Go to quality screen when the user click stop tracking
        viewModel.navigateToQualityScreen.observe(viewLifecycleOwner) { id ->
            id?.let {
                findNavController().navigate(ListFragmentDirections.actionListFragmentToQualityFragment(id))
                viewModel.doneNavigating()
            }
        }

        // Observe the nights list to inflate items or empty state
        viewModel.sleepNights.observe(viewLifecycleOwner) { nights ->
            binding.loader.isVisible = false

            if(nights.isEmpty()){
                binding.emptyState.isVisible = true
                binding.nsv.isVisible = false
            } else {
                binding.emptyState.isVisible = false
                binding.nsv.isVisible = true

                binding.list.removeAllViews()
                nights.forEach { night ->
                    if (night.startTime == night.endTime) {
                        addCurrentView(night)
                    } else {
                        addNightView(night)
                    }
                }
            }
        }

        // Set up the top options menu
        setHasOptionsMenu(true)

        return binding.root
    }

    // Get tonight when the user get back from the quality screen
    override fun onResume() {
        super.onResume()
        viewModel.initializeTonight()
    }

    // Inflate night item and pass the data to it
    private fun addNightView(night: SleepNight) {
        val view = ItemNightBinding.inflate(layoutInflater, binding.list, false)
        view.tvStartTime.text = getTime(night.startTime)
        view.tvEndTime.text = getTime(night.endTime)
        view.tvTotalTime.text = getTotalTime(night.startTime, night.endTime)
        view.tvSleepQuality.text = getQuality(night.quality)
        binding.list.addView(view.root)
    }

    // Inflate tonight item and pass data to it
    private fun addCurrentView(night: SleepNight) {
        val view = ItemCurrentBinding.inflate(layoutInflater, binding.list, false)
        view.tvStartTime.text = getTime(night.startTime)
        binding.list.addView(view.root)
    }

    // Get quality string from the number
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

    // Inflate the options menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.app_menu, menu)
    }

    // What to do if the options menu item is selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_exit -> requireActivity().finishAffinity()
            // TODO: Return the language icon
//            R.id.mi_language -> changeLang()
        }
        return super.onOptionsItemSelected(item)
    }
}