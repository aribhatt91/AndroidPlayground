package com.aribhatt.kotlinlearner.activitytracker.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.aribhatt.kotlinlearner.R
import com.aribhatt.kotlinlearner.activitytracker.data.Constants
import com.aribhatt.kotlinlearner.databinding.FragmentSetupBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment : Fragment() {
    private lateinit var binding: FragmentSetupBinding

    @Inject
    lateinit var sharedPref: SharedPreferences

    @set:Inject
    var isFirstRun: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetupBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!isFirstRun){
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment, true)
                .build()
            findNavController().navigate(R.id.action_setupFragment_to_runFragment,
            savedInstanceState, navOptions)
        }
        binding.tvContinue.setOnClickListener {
            val success = writePersonalDataToPref()
            if(success){
                findNavController().navigate(R.id.action_setupFragment_to_runFragment)
            }else {
                Snackbar.make(requireView(), "Please enter your name and weight to continue", Snackbar.LENGTH_LONG).show()
            }

        }
    }

    private fun writePersonalDataToPref(): Boolean {
        val name = binding.etName.text.toString()
        val weight = binding.etWeight.text.toString()
        if(name.isEmpty() || weight.isEmpty()){
            //Snackbar.make(this, )
            return false
        }
        sharedPref?.edit()
            .putString(Constants.KEY_NAME, name)
            .putFloat(Constants.KEY_WEIGHT, weight.toFloat())
            .putBoolean(Constants.KEY_FIRST_TIME_TOGGLE, false)
            .apply()
        val toolbarText = "Let's go $name"

        activity?.actionBar?.title = toolbarText

        return true
    }
}