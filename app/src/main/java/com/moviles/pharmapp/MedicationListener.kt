package com.moviles.pharmapp

import com.moviles.pharmapp.model.Medication

interface MedicationListener {

    fun onMedicineClicked(medication: Medication, position: Int)

}