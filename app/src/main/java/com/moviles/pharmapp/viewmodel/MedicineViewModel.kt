package com.moviles.pharmapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moviles.pharmapp.backend.repositories.RepoMedicines
import com.moviles.pharmapp.model.Medication
import java.lang.Exception

class MedicineViewModel(context: Context): ViewModel(), BaseViewModel {

    var listMedicine: MutableLiveData<List<Medication>> = MutableLiveData()
    var isLoading = MutableLiveData<Boolean>()
    val repoMedicines:RepoMedicines = RepoMedicines(context)

    fun refresh() {
        getMedicine()
        repoMedicines.getAllMedicines(this, "Prueba")
    }

    fun getMedicine() {

        var medication1 = Medication()

        medication1.id = "1"
        medication1.name = "x"
        medication1.tag = "sd"
        medication1.image = ""


        var medication2 = Medication()

        medication2.id = "6"
        medication2.name = "YYYY"
        medication2.tag = "gfdhnn"
        medication2.image = ""



        var listm: List<Medication> = mutableListOf(medication1)
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

    override fun exito(etiqueta: String?, objeto: Any?) {

    }

    override fun falla(etiqueta: String?) {

    }

    override fun actualizacion(etiqueta: String?, objeto: Any?) {

    }
}