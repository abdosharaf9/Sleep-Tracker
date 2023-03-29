package com.abdosharaf.sleeptracker.qualityScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.abdosharaf.sleeptracker.databinding.FragmentQualityBinding

class QualityFragment : Fragment() {

    private lateinit var binding: FragmentQualityBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentQualityBinding.inflate(inflater, container, false)

        return binding.root
    }
}