package com.moviles.pharmapp.view.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.moviles.pharmapp.model.Medication

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.moviles.pharmapp.R

class MedicationAdapter (val medicationListener: MedicationListener): RecyclerView.Adapter<MedicationAdapter.ViewHolder>() {

    var listMedicine = ArrayList<Medication>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivMedication = itemView.findViewById<ImageView>(R.id.ivItemMedicationimage)
        val tvMedicationName = itemView.findViewById<TextView>(R.id.tvItemMedicationName)
        val tvMedicationTag = itemView.findViewById<TextView>(R.id.tvItemMedicationTag)

        val lHolder = itemView.findViewById<LinearLayout>(R.id.lHolder)

    }

    fun updateData(data: List<Medication>) {

        listMedicine.clear()
        listMedicine.addAll(data)
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MedicationAdapter.ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_medication,
                parent,
                false
            )
        )

    override fun getItemCount() = listMedicine.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val medicine = listMedicine[position] as Medication

        Glide.with(holder.itemView.context)
            .load(R.drawable.ic_medicina_negro)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.ivMedication)


        Log.w("cruce", "antes del cruce")
        Log.w("cruce", medicine.warning.size.toString())
        for (i in medicine.warning) {

            Log.w("cruce", i)
            for (j in listMedicine) {

                Log.w("cruce", j.id)
                if (i == j.id) {


                    holder.lHolder.setBackgroundColor(Color.parseColor("#3461CF"))
                    holder.itemView.setBackgroundColor(Color.parseColor("#3461CF"))
                }
            }
        }
        holder.tvMedicationName.text = medicine.name
        holder.tvMedicationTag.text = medicine.tag

        holder.itemView.setOnClickListener {
            medicationListener.onMedicineClicked(medicine,position)
        }
    }

}