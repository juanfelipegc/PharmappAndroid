package com.moviles.pharmapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviles.pharmapp.R
import com.moviles.pharmapp.model.Calendar
import com.moviles.pharmapp.model.Medication

class CalendarAdapter: RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    var listCalendar = ArrayList<Calendar>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvDay = itemView.findViewById<TextView>(R.id.tvDay)
        val tvTimes = itemView.findViewById<TextView>(R.id.tvTimes)
        val rvDrugs = itemView.findViewById<RecyclerView>(R.id.rvItemsCalendar)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    )= ViewHolder(
        LayoutInflater.from(parent.context).inflate(
        R.layout.holder_calendar,
        parent,
        false)
    )

    fun updateData(data: List<Calendar>) {

        listCalendar.clear()
        listCalendar.addAll(data)
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return listCalendar.size //La semana
    }

    override fun onBindViewHolder(holder: CalendarAdapter.ViewHolder, position: Int) {
        val actualCalendar = listCalendar[position]
        holder.tvDay.text = actualCalendar.day
        holder.tvTimes.text = actualCalendar.times.toString()+" times"

        val recyclerCalendarItemAdapter = holder.rvDrugs

        val calendarItemAdpater = CalendarItemAdapter()

        recyclerCalendarItemAdapter.apply {
            val linear = LinearLayoutManager(context)
           linear.orientation = RecyclerView.HORIZONTAL
            layoutManager = linear
            adapter = calendarItemAdpater
        }
        if (actualCalendar.medicines != null && actualCalendar.medicines!!.size > 0){
            calendarItemAdpater.updateData(actualCalendar.medicines!!)
        }
    }


}