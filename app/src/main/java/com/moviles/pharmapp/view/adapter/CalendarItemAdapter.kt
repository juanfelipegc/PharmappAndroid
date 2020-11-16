package com.moviles.pharmapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.moviles.pharmapp.R
import com.moviles.pharmapp.model.Calendar
import com.moviles.pharmapp.model.Medication
import com.moviles.pharmapp.model.MedicineInCalendar
import com.moviles.pharmapp.model.User

class CalendarItemAdapter: RecyclerView.Adapter<CalendarItemAdapter.ViewHolder>() {

    var listMedicine = ArrayList<MedicineInCalendar>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val timeStamp2Text = itemView.findViewById<TextView>(R.id.timeStamp2Text)
        val drug = itemView.findViewById<TextView>(R.id.drugs)
        val tvVitamins = itemView.findViewById<TextView>(R.id.vitamins)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    )= CalendarItemAdapter.ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.holder_calendar_item,
            parent,
            false)
    )

    fun updateData(data: List<MedicineInCalendar>) {

        listMedicine.clear()
        listMedicine.addAll(data)
        notifyDataSetChanged()

    }


    override fun getItemCount(): Int {
        return listMedicine.size //Los medicamentos
    }

    override fun onBindViewHolder(holder: CalendarItemAdapter.ViewHolder, position: Int) {

        val medicine = listMedicine[position]

        holder.timeStamp2Text.text = medicine.hours
        holder.drug.text = medicine.name
        holder.tvVitamins.text = medicine.tag

    }

}