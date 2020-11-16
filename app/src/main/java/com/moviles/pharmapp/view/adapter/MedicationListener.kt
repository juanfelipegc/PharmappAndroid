package com.moviles.pharmapp.view.adapter

import com.moviles.pharmapp.model.Medication

interface MedicationListener {

    fun onMedicineClicked(medication: Medication, position: Int)

}