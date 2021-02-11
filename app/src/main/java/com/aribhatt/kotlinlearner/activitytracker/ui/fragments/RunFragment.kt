package com.aribhatt.kotlinlearner.activitytracker.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aribhatt.kotlinlearner.R
import com.aribhatt.kotlinlearner.activitytracker.data.SortBy
import com.aribhatt.kotlinlearner.activitytracker.ui.adapter.RunAdapter
import com.aribhatt.kotlinlearner.activitytracker.ui.viewmodel.TrackerViewModel
import com.aribhatt.kotlinlearner.databinding.FragmentRunBinding
import com.aribhatt.kotlinlearner.databinding.FragmentSetupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RunFragment : Fragment() {
    private val viewModel: TrackerViewModel by viewModels()
    private lateinit var binding: FragmentRunBinding
    private lateinit var runAdapter: RunAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRunBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        when(viewModel.sortType) {
            SortBy.DATE -> binding.spFilter.setSelection(0, true)
            SortBy.TIME -> binding.spFilter.setSelection(1, true)
            SortBy.DISTANCE-> binding.spFilter.setSelection(2, true)
            SortBy.AVG_SPEED -> binding.spFilter.setSelection(3, true)
            SortBy.CALORIES -> binding.spFilter.setSelection(4, true)
        }

        binding.spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position) {
                    0 -> viewModel.sortRuns(SortBy.DATE)
                    1 -> viewModel.sortRuns(SortBy.TIME)
                    2 -> viewModel.sortRuns(SortBy.DISTANCE)
                    3 -> viewModel.sortRuns(SortBy.AVG_SPEED)
                    4 -> viewModel.sortRuns(SortBy.CALORIES)
                }
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }

        }

        viewModel.runs.observe(viewLifecycleOwner, Observer {
            runAdapter.submitList(it)
        })
        binding.fab.setOnClickListener {
            Log.i("RunFragment", "Clicked Fab")
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }
    }

    private fun setupRecyclerView() = binding?.rvRuns.apply {
        runAdapter = RunAdapter()
        adapter = runAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }
}