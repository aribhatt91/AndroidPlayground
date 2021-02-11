package com.aribhatt.kotlinlearner.activitytracker.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aribhatt.kotlinlearner.R
import com.aribhatt.kotlinlearner.activitytracker.data.db.Run
import com.aribhatt.kotlinlearner.activitytracker.utils.Utility
import com.aribhatt.kotlinlearner.databinding.ItemRunBinding
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class RunAdapter: RecyclerView.Adapter<RunAdapter.RunViewHolder>() {
    private lateinit var binding: ItemRunBinding
    //https://www.youtube.com/watch?v=i2G0c4ylEnk&list=PLQkwcJG4YTCQ6emtoqSZS2FVwZR9FT3BV&index=19
    val diffCallback = object : DiffUtil.ItemCallback<Run>() {
        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Run>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        binding = ItemRunBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RunViewHolder(
            binding.root
            //LayoutInflater.from(parent.context).inflate(R.layout.item_run, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val run = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(run.img).into(binding.ivRunImage)
            val calendar = Calendar.getInstance().apply {
                timeInMillis = run.timestamp
            }
            val dateFormat = SimpleDateFormat("dd:mm:yyyy", Locale.getDefault())
            binding.tvDate.text = dateFormat.format(calendar.time)
            binding.tvAvgSpeed.text = "${run.avgSpeed}km/h"
            binding.tvDistance.text = "${run.distance / 1000}km"
            binding.tvTime.text = Utility.getFormattedStopwatchTime(run.time)
            binding.tvCalories.text = "${run.caloriesBurnt}Cal"

        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class RunViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }
}