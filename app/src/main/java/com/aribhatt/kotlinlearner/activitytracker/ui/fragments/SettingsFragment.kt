package com.aribhatt.kotlinlearner.activitytracker.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aribhatt.kotlinlearner.R
import com.aribhatt.kotlinlearner.activitytracker.data.Constants.KEY_NAME
import com.aribhatt.kotlinlearner.activitytracker.data.Constants.KEY_WEIGHT
import com.aribhatt.kotlinlearner.databinding.FragmentSettingsBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    @Inject
    lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFieldsFromSharedPrefs()
        binding.btnApplyChanges.setOnClickListener {
            val success = applyChangesToSharedPrefs()
            var message = if(success) "Your changes have been saved" else "Your changes couldn't be saved"
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
        }
    }
    private fun getFieldsFromSharedPrefs() {
        val nameText = sharedPrefs?.getString(KEY_NAME, "")
        val weightText = sharedPrefs?.getFloat(KEY_WEIGHT, 0f)
        binding.etName.setText(nameText)
        binding.etWeight.setText(weightText.toString())
    }
    private fun applyChangesToSharedPrefs(): Boolean {
        val nameText = binding.etName.text?.toString() ?: ""
        val weightText = binding.etWeight.text?.toString() ?: ""

        if(nameText.isEmpty() && weightText.isEmpty()){
            return false
        }
        if(!nameText.isEmpty()) {
            sharedPrefs?.edit().putString(KEY_NAME, nameText).apply()
        }
        if(!weightText.isEmpty()) {
            sharedPrefs?.edit().putFloat(KEY_WEIGHT, weightText.toFloat()).apply()
        }

        return true
    }

}