package com.moviles.pharmapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moviles.pharmapp.backend.repositories.BaseBackend
import com.moviles.pharmapp.backend.repositories.RepoMedicines
import com.moviles.pharmapp.model.Medication
import com.moviles.pharmapp.network.Callback
import com.moviles.pharmapp.network.FirestoreService
import java.lang.Exception

class MedicineBackend: ViewModel(),
    BaseBackend {


    val firestoreService = FirestoreService()
    var listMedicine: MutableLiveData<MutableList<Medication>> = MutableLiveData()
    var isLoading = MutableLiveData<Boolean>()
    val repoMedicines:RepoMedicines = RepoMedicines()
    var listm: MutableList<Medication> = mutableListOf()

    fun refresh() {
        repoMedicines.getAllMedicines(this, "Prueba")
    }

    fun refresh2() {


        listMedicine.postValue(listm)
        getUserMedicineFromFireBase()
        //repoMedicines.getAllMedicines(this, "Prueba")
    }

    fun refresh2(listMedicine: MutableList<Medication>) {


        var medicationx = Medication()

        medicationx.id="11"
        medicationx.image=""
        medicationx.tag="N/A"
        medicationx.name="Medicine added"

        listm.add(medicationx)
    }

    fun getUserMedicineFromFireBase() {

        firestoreService.getUserMedicine(object: Callback<MutableList<Medication>> {
            override fun onSucces(result: MutableList<Medication>?) {
                listMedicine.postValue(result)
                processFinished()
            }

            override fun onFailed(exception: Exception) {
                processFinished()
            }
        })

//        var medication1 = Medication()
//
//        medication1.id = "1"
//        medication1.name = "Dolex"
//        medication1.tag = "Pill"
//        medication1.image = ""
//
//
//        var medication2 = Medication()
//
//        medication2.id = "6"
//        medication2.name = "Advil"
//        medication2.tag = "Pill"
//        medication2.image = ""
//
//
//
//        listm.add(medication1)
//        listm.add(medication2)
//        listMedicine.postValue(listm)
//
//
//        Log.i("datos",listm.size.toString())
//
//        Log.i("datos",listMedicine.toString())



    }


    fun processFinished() {
        isLoading.value = true
    }

    override fun stopListener() {

    }

    /**
     * adds Object to Firebase Collection
     */
    fun addMedicine(code: Medication) {


        var medicationx = Medication()

        medicationx.id=code.id
        medicationx.image=""
        medicationx.tag="N/A"
        medicationx.name="Medicine added"

        listm.add(medicationx)
        listMedicine.postValue(listm)


    }

    override fun exito(etiqueta: String?, objeto: Any?) {
        val listmed: MutableList<Medication> = objeto as MutableList<Medication>
        listMedicine.postValue(listmed)
    }

    override fun falla(etiqueta: String?) {

    }

    override fun actualizacion(etiqueta: String?, objeto: Any?) {
        val listmed: MutableList<Medication> = objeto as MutableList<Medication>
        listMedicine.postValue(listmed)
    }
}