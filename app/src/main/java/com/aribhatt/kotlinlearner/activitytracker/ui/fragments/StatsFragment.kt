package com.aribhatt.kotlinlearner.activitytracker.ui.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.aribhatt.kotlinlearner.R
import com.aribhatt.kotlinlearner.activitytracker.ui.viewmodel.StatsViewModel
import com.aribhatt.kotlinlearner.activitytracker.ui.views.CustomMarketView
import com.aribhatt.kotlinlearner.activitytracker.utils.Utility
import com.aribhatt.kotlinlearner.databinding.FragmentStatsBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Math.round

/*
* Question - can layout id be passed as constructor - how does that work???
* */
@AndroidEntryPoint
class StatsFragment : Fragment(R.layout.fragment_stats) {
    lateinit var binding: FragmentStatsBinding
    private val viewModel: StatsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        setupBarChart()
    }

    private fun subscribeToObservers(){
        viewModel.totalTimeRun.observe(viewLifecycleOwner, Observer {
            it?.let {
                val totalTimeRun = Utility.getFormattedStopwatchTime(it)
                binding.tvTotalTime.text = totalTimeRun
            }
        })
        viewModel.totalDistance.observe(viewLifecycleOwner, Observer {
            it?.let {
                //val totalTimeRun = Utility.getFormattedStopwatchTime(it)
                binding.tvTotalDistance.text = "${it}km"
            }
        })
        viewModel.totalAvgSpeed.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.tvAverageSpeed.text = "${round(it * 10f)/10f}km/h"
            }
        })
        viewModel.totalCalories.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.tvTotalCalories.text = "${it}Cal"
            }
        })
        viewModel.runsSortedByDate.observe(viewLifecycleOwner, Observer {
            it?.let {
                val allAvgSpeeds = it.indices.map { i -> BarEntry(i.toFloat(), it[i].avgSpeed) }
                val barDataSet = BarDataSet(allAvgSpeeds, "Avg Speed/Time").apply {
                    valueTextColor = Color.DKGRAY
                    color = Color.WHITE//ContextCompat.getColor(requireContext(), getColor(R.color.teal_700))
                }
                binding.barChart.data = BarData(barDataSet)
                binding.barChart.marker = CustomMarketView(it.reversed(), requireContext(), R.layout.market_view)
                binding.barChart.invalidate()
            }
        })
    }

    private fun setupBarChart(){
        binding.barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.WHITE
            textColor = Color.DKGRAY
            setDrawGridLines(false)
        }
        binding.barChart.axisLeft.apply {
            axisLineColor = Color.WHITE
            textColor = Color.DKGRAY
            setDrawGridLines(false)
        }
        binding.barChart.axisRight.apply {
            axisLineColor = Color.WHITE
            textColor = Color.DKGRAY
            setDrawGridLines(false)
        }
        binding.barChart.apply {
            description.text = "Avg Speed / Time"
            legend.isEnabled = false
        }
    }
}