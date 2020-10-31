package com.moviles.pharmapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.moviles.pharmapp.R

class CalendarItemAdapter: RecyclerView.Adapter<CalendarItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    )= CalendarItemAdapter.ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.holder_calendar_item,
            parent,
            false)
    )


    override fun getItemCount(): Int {
        return 4 //Los medicamentos
    }

    override fun onBindViewHolder(holder: CalendarItemAdapter.ViewHolder, position: Int) {
        holder.timeStamp2Text.text = "12:30"
        holder.tvDrugs.text = "Voltaren"
        holder.tvVitamins.text = "Vitamins"

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val timeStamp2Text = itemView.findViewById<TextView>(R.id.timeStamp2Text)
        val tvDrugs = itemView.findViewById<TextView>(R.id.drugs)
        val tvVitamins = itemView.findViewById<TextView>(R.id.vitamins)
    }
}