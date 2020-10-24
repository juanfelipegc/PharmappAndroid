package com.moviles.pharmapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moviles.pharmapp.backend.repositories.RepoMedicines
import com.moviles.pharmapp.model.Medication
import java.lang.Exception

class MedicineViewModel: ViewModel(), BaseViewModel {

    var listMedicine: MutableLiveData<MutableList<Medication>> = MutableLiveData()
    var isLoading = MutableLiveData<Boolean>()
    val repoMedicines:RepoMedicines = RepoMedicines()

    var listm: MutableList<Medication> = mutableListOf()



    fun refresh() {
        getMedicine()
        repoMedicines.getAllMedicines(this, "Prueba")
    }

    fun refresh2() {


        listMedicine.postValue(listm)
    }

    fun getMedicine() {

        var medication1 = Medication()

        medication1.id = "1"
        medication1.name = "Dolex"
        medication1.tag = "Pill"
        medication1.image = ""


        var medication2 = Medication()

        medication2.id = "6"
        medication2.name = "Advil"
        medication2.tag = "Pill"
        medication2.image = ""



        listm.add(medication1)
        listm.add(medication2)
        listMedicine.postValue(listm)


        Log.i("datos",listm.size.toString())

        Log.i("datos",listMedicine.toString())

        isProcessFinished()

    }


    fun isProcessFinished() {
        isLoading.value = true
    }

    override fun stopListener() {

    }

    /**
     * adds Object to Firebase Collection
     */
    fun addMedicine(code: String) {


        var medicationx = Medication()

        medicationx.id=code
        medicationx.image=""
        medicationx.tag="N/A"
        medicationx.name="Medicine added"

        listm.add(medicationx)
        listMedicine.postValue(listm)


    }

    override fun exito(etiqueta: String?, objeto: Any?) {
        val listmed: List<Medication> = objeto as List<Medication>
        listMedicine.postValue(listmed)
    }

    override fun falla(etiqueta: String?) {

    }

    override fun actualizacion(etiqueta: String?, objeto: Any?) {
        val listmed: List<Medication> = objeto as List<Medication>
        listMedicine.postValue(listmed)
    }
}