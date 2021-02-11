package com.aribhatt.kotlinlearner.activitytracker.ui.fragments

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.aribhatt.kotlinlearner.R
import com.aribhatt.kotlinlearner.activitytracker.data.Constants
import com.aribhatt.kotlinlearner.activitytracker.data.Constants.ACTION_PAUSE_SERVICE
import com.aribhatt.kotlinlearner.activitytracker.data.Constants.ACTION_START_OR_RESUME_SERVICE
import com.aribhatt.kotlinlearner.activitytracker.data.Constants.ACTION_STOP_SERVICE
import com.aribhatt.kotlinlearner.activitytracker.data.db.Run
import com.aribhatt.kotlinlearner.activitytracker.service.PolyLine
import com.aribhatt.kotlinlearner.activitytracker.service.TrackingService
import com.aribhatt.kotlinlearner.activitytracker.ui.viewmodel.StatsViewModel
import com.aribhatt.kotlinlearner.activitytracker.ui.viewmodel.TrackerViewModel
import com.aribhatt.kotlinlearner.activitytracker.utils.Utility
import com.aribhatt.kotlinlearner.databinding.FragmentTrackingBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import java.util.*
import javax.inject.Inject
import kotlin.math.round

@AndroidEntryPoint
class TrackingFragment : Fragment() {
    private val viewModel: TrackerViewModel by viewModels()
    private lateinit var binding: FragmentTrackingBinding
    private var isTracking: Boolean = false
    private var pathPoints = mutableListOf<PolyLine>()
    private var currentTimeInMillis = 0L

    private var menu: Menu? = null
    private var map: GoogleMap? = null
    private val CANCEL_TRACKING_DIALOG = "CANCEL_TRACKING_DIALOG"

    @set:Inject
    var  weight: Float = 72f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrackingBinding.inflate(inflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)

        if(savedInstanceState != null) {
            val cancelDialog = parentFragmentManager.findFragmentByTag(CANCEL_TRACKING_DIALOG) as CancelTrackingDialog?
            cancelDialog?.setYesListener { stopRun() }
        }

        binding.btnToggleRun.setOnClickListener {
            toggleRun()
        }
        binding.btnFinishRun.setOnClickListener {
            //https://www.youtube.com/watch?v=mwft7l9Y3nc&list=PLQkwcJG4YTCQ6emtoqSZS2FVwZR9FT3BV&index=18
            zoomToSeeWholeTrack()
            endRunAndSaveToDB()
        }
        binding.mapView.getMapAsync {
            map = it
            addAllPolyline()
        }
        subscribeToObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.tracker_menu, menu)
        this.menu = menu
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if(this.currentTimeInMillis > 0) {
            this.menu?.getItem(0)?.isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.cancel_tracking_action -> showCancelDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun stopRun(){
        binding.tvTimer.text = "00:00:00"
        sendCommandToService(ACTION_STOP_SERVICE)
        findNavController().navigate(R.id.action_trackingFragment_to_runFragment)
    }

    private fun showCancelDialog(){
        CancelTrackingDialog().apply {
            setYesListener { stopRun() }
        }.show(parentFragmentManager, CANCEL_TRACKING_DIALOG)
    }

    private fun sendCommandToService(action: String){
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }
    }
    private fun subscribeToObserver(){
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })
        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
        })

        TrackingService.timeRunInMillis.observe(viewLifecycleOwner, Observer {
            currentTimeInMillis = it
            val formattedTime = Utility.getFormattedStopwatchTime(currentTimeInMillis)
            binding.tvTimer?.text = formattedTime
        })
    }
    private fun toggleRun(){
        if(isTracking){
            sendCommandToService(ACTION_PAUSE_SERVICE)
        }else {
            menu?.getItem(0)?.isVisible = true
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun updateTracking(isTracking: Boolean){
        this.isTracking = isTracking
        if(!this.isTracking && currentTimeInMillis > 0L) {
            binding.btnToggleRun.text = "Start"
            binding.btnFinishRun.visibility = View.VISIBLE
        }else if(this.isTracking){
            binding.btnToggleRun.text = "Stop"
            menu?.getItem(0)?.isVisible = true
            binding.btnFinishRun.visibility = View.GONE
        }
    }

    private fun moveCameraToUser(){
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()){
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    15F
                )
            )
        }
    }

    private fun addAllPolyline() {
        for (polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(Color.RED)
                .width(8f)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline() {
        if(pathPoints.isNotEmpty() && pathPoints.last().size > 1){
            val secLast = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(Color.RED)
                .width(8f)
                .add(secLast)
                .add(lastLatLng)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun zoomToSeeWholeTrack(){
        val bounds = LatLngBounds.Builder()
        for (polyline in pathPoints){
            for (position in polyline){
                bounds.include(position)
            }
        }
        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                binding.mapView.width,
                binding.mapView.height,
                (binding.mapView.height * 0.05f).toInt()
            )
        )
    }
    private fun endRunAndSaveToDB(){
        map?.snapshot { bmp ->
            var distance = 0f
            for (polyline in pathPoints){
                distance += Utility.calculatePolylineLength(polyline).toFloat()
            }
            val avgSpeed = round((distance/1000) / (currentTimeInMillis / 1000f / 60f / 60f) * 10)/10f
            val dateTimeStamp = Calendar.getInstance().timeInMillis
            val caloriesBurnt = Utility.caloriesBurnt(distance, weight)
            val run = Run(bmp, dateTimeStamp, avgSpeed, distance.toInt(), currentTimeInMillis, caloriesBurnt)
            viewModel.insertRun(run)
            Snackbar.make(requireActivity().findViewById(R.id.rootView), "Run saved successfully", Snackbar.LENGTH_LONG).show()
            stopRun()
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView?.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        binding.mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView?.onDestroy()
    }

}