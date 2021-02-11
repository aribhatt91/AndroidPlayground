package com.aribhatt.kotlinlearner.activitytracker.ui.views

import android.content.Context
import android.widget.TextView
import com.aribhatt.kotlinlearner.R
import com.aribhatt.kotlinlearner.activitytracker.data.db.Run
import com.aribhatt.kotlinlearner.activitytracker.utils.Utility
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.text.SimpleDateFormat
import java.util.*

class CustomMarketView(
    val runs:List<Run>,
    context: Context,
    layoutId: Int
) : MarkerView(context, layoutId) {

    override fun getOffset(): MPPointF {
        return MPPointF(-width/2f, -height.toFloat())
    }
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        if(e== null){
            return
        }
        val currRunId = e.x.toInt()
        val run = runs.get(currRunId)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = run.timestamp
        }
        val dateFormat = SimpleDateFormat("dd:mm:yyyy", Locale.getDefault())
        findViewById<TextView>(R.id.tvDate)?.text = dateFormat.format(calendar.time)
        //tvDate.text = dateFormat.format(calendar.time)
        findViewById<TextView>(R.id.tvAvgSpeed)?.text = "${run.avgSpeed}km/h"
        findViewById<TextView>(R.id.tvDistance)?.text = "${run.distance / 1000}km"
        findViewById<TextView>(R.id.tvDuration)?.text = Utility.getFormattedStopwatchTime(run.time)
        findViewById<TextView>(R.id.tvCaloriesBurned)?.text = "${run.caloriesBurnt}Cal"
    }

}