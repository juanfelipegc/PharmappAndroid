package com.moviles.pharmapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviles.pharmapp.R
import kotlinx.android.synthetic.main.fragment_home.*

class CalendarAdapter: RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    )= ViewHolder(
        LayoutInflater.from(parent.context).inflate(
        R.layout.holder_calendar,
        parent,
        false)
    )


    override fun getItemCount(): Int {
        return 7 //La semana
    }

    override fun onBindViewHolder(holder: CalendarAdapter.ViewHolder, position: Int) {
        holder.tvDay.text = "Today"
        holder.tvTimes.text = "2 Times"

        val recyclerCalendarItemAdapter = holder.rvDrugs

        val calendarItemAdpater = CalendarItemAdapter()

        recyclerCalendarItemAdapter.apply {
            val linear = LinearLayoutManager(context)
           linear.orientation = RecyclerView.HORIZONTAL
            layoutManager = linear
            adapter = calendarItemAdpater
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvDay = itemView.findViewById<TextView>(R.id.tvDay)
        val tvTimes = itemView.findViewById<TextView>(R.id.tvTimes)
        val rvDrugs = itemView.findViewById<RecyclerView>(R.id.rvItemsCalendar)
    }

}