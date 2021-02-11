package com.aribhatt.kotlinlearner.common.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aribhatt.kotlinlearner.R
import com.aribhatt.kotlinlearner.data.entities.NoteEntity
import com.aribhatt.kotlinlearner.databinding.ListItemBinding

class NoteListAdapter(private val noteList: List<NoteEntity>, private val listener: ListItemListener):
    RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {
    val selectedNotes = arrayListOf<NoteEntity>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = noteList[position]
        with(holder.binding) {
            listText.text = note.text
            Log.i("noteList ->", note.text)
            root.setOnClickListener {
                listener.onItemClick(note.id)
            }
            listThumb.setOnClickListener {
                if(selectedNotes.contains(note)){
                    selectedNotes.remove(note)
                    listThumb.setImageResource(R.drawable.ic_note)
                }else {
                    selectedNotes.add(note)
                    listThumb.setImageResource(R.drawable.ic_check)
                }
                listener.onItemSelectionChange()
            }
            listThumb.setImageResource(
                if(selectedNotes.contains(note)){
                    R.drawable.ic_check
                }else {
                    R.drawable.ic_note
                }
            )
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val binding = ListItemBinding.bind(itemView)
    }

    interface ListItemListener {
        fun onItemClick(noteId: Int)
        fun onItemSelectionChange()
    }
}