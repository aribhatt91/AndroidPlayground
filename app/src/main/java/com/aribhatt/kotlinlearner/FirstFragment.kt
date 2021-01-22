package com.aribhatt.kotlinlearner

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aribhatt.kotlinlearner.activitytracker.ui.TrackerActivity
import com.aribhatt.kotlinlearner.alarm.ui.AlarmActivity
import com.aribhatt.kotlinlearner.viewmodel.MainViewModel
import com.aribhatt.kotlinlearner.databinding.FragmentFirstBinding
import com.aribhatt.kotlinlearner.ui.activities.DetailActivity
import com.aribhatt.kotlinlearner.firebase.ui.LoginActivity
import com.aribhatt.kotlinlearner.permissionslearner.PermissionsTutorialActivity
import com.aribhatt.kotlinlearner.ui.adapters.NoteListAdapter
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), NoteListAdapter.ListItemListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentFirstBinding
    private lateinit var adapter: NoteListAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        //return inflater.inflate(R.layout.fragment_first, container, false)
        with(binding.mainList) {
            setHasFixedSize(true)
            val divider = DividerItemDecoration(context, LinearLayoutManager(context).orientation)
            addItemDecoration(divider)
        }
        viewModel.notesList?.observe(viewLifecycleOwner, Observer {
            Log.i("noteList ->", it.toString())
            adapter = NoteListAdapter(it, this)
            binding.mainList.adapter = adapter
            binding.mainList.layoutManager = LinearLayoutManager(activity)
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    fun addSampleData(): Boolean {
        viewModel?.addSampleData()

        return true
    }
    fun deleteSelectedNotes(): Boolean {
        viewModel?.deleteNotes(adapter.selectedNotes)
        Handler(Looper.getMainLooper()).postDelayed({
            adapter.selectedNotes.clear()
            requireActivity().invalidateOptionsMenu()
        }, 400)
        return true
    }
    fun deleteAll(): Boolean {
        viewModel?.deleteAll()
        Handler(Looper.getMainLooper()).postDelayed({
            adapter.selectedNotes.clear()
            requireActivity().invalidateOptionsMenu()
        }, 400)
        return true
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuId =
            if(this::adapter.isInitialized && adapter.selectedNotes.isNotEmpty()){
                R.menu.menu_items_selected
            }else {
                R.menu.menu_main
            }
        inflater.inflate(menuId, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                //Snackbar.make(binding.root, "Settings item clicked", Snackbar.LENGTH_LONG).show()
                val intent = Intent(activity, DetailActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_add -> {
                Snackbar.make(binding.root, "Sample data added", Snackbar.LENGTH_LONG).show()
                addSampleData()
                return true
            }
            R.id.action_delete -> {
                Snackbar.make(binding.root, "Sample data deleted", Snackbar.LENGTH_LONG).show()
                deleteSelectedNotes()
                return true
            }
            R.id.action_delete_all -> {
                Snackbar.make(binding.root, "Sample data deleted", Snackbar.LENGTH_LONG).show()
                deleteAll()
                return true
            }
            R.id.action_firebase -> {
                startActivity(Intent(activity, LoginActivity::class.java))
                return true
            }
            R.id.action_alarm -> {
                startActivity(Intent(activity, AlarmActivity::class.java))
                return true
            }
            R.id.action_perm -> {
                startActivity(Intent(activity, PermissionsTutorialActivity::class.java))
                return true
            }
            R.id.action_track -> {
                startActivity(Intent(activity, TrackerActivity::class.java))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onItemClick(noteId: Int) {
        Log.i("noteList", noteId.toString())
        val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(noteId)
        findNavController().navigate(action)
    }

    override fun onItemSelectionChange() {
        requireActivity().invalidateOptionsMenu()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}