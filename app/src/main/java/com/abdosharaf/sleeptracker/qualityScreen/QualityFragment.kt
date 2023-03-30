package com.abdosharaf.sleeptracker.qualityScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.abdosharaf.sleeptracker.database.SleepNightsDatabase.Companion.getDatabase
import com.abdosharaf.sleeptracker.databinding.FragmentQualityBinding

class QualityFragment : Fragment() {

    private lateinit var binding: FragmentQualityBinding
    private val args: QualityFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentQualityBinding.inflate(inflater, container, false)

        val database = getDatabase(requireContext())
        val viewModelFactory = QualityViewModelFactory(database)
        val viewModel = ViewModelProvider(this, viewModelFactory)[QualityViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.setNightId(args.nightId)

        // Navigate back to the list when the user choose a quality
        viewModel.navigateBack.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        return binding.root
    }
}