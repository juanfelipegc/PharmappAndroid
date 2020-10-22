package com.moviles.pharmapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moviles.pharmapp.backend.repositories.RepoMedicines
import com.moviles.pharmapp.model.Medication
import java.lang.Exception

class MedicineViewModel: ViewModel(), BaseViewModel {

    var listMedicine: MutableLiveData<List<Medication>> = MutableLiveData()
    var isLoading = MutableLiveData<Boolean>()
    val repoMedicines:RepoMedicines = RepoMedicines()

    fun refresh() {
        getMedicine()
        repoMedicines.getAllMedicines(this, "Prueba")
    }

    fun getMedicine() {

        isProcessFinished()

    }


    fun isProcessFinished() {
        isLoading.value = true
    }

    override fun stopListener() {

    }

    override fun exito(etiqueta: String?, objeto: Any?) {
        val listm: List<Medication> = objeto as List<Medication>
        listMedicine.postValue(listm)
    }

    override fun falla(etiqueta: String?) {

    }

    override fun actualizacion(etiqueta: String?, objeto: Any?) {
        val listm: List<Medication> = objeto as List<Medication>
        listMedicine.postValue(listm)
    }
}