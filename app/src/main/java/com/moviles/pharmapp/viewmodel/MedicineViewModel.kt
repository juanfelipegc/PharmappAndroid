package com.moviles.pharmapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moviles.pharmapp.backend.repositories.BaseBackend
import com.moviles.pharmapp.backend.repositories.RepoMedicines
import com.moviles.pharmapp.model.Medication
import com.moviles.pharmapp.network.Callback
import com.moviles.pharmapp.network.FirestoreService
import java.lang.Exception

class MedicineViewModel : ViewModel(), BaseBackend {


    val firestoreService = FirestoreService()
    var listMedicine: MutableLiveData<List<Medication>> = MutableLiveData()
    var isLoading = MutableLiveData<Boolean>()
    val repoMedicines: RepoMedicines = RepoMedicines()
    var listm: MutableList<Medication> = mutableListOf()
    var medicine = Medication()
    lateinit var userMail: String


    fun refresh() {


        repoMedicines.getAllMedicines(this, "Prueba")

    }

    fun refreshUserMedicines() {

        getUser()
        getUserMedicineFromFireBase(userMail)
    }

    fun findMedicine(code: String): Medication {


        firestoreService.findMedicine(code, object : Callback<Medication> {

            override fun onSucces(result: Medication?) {

                medicine = result!!
                Log.i("Medicina", medicine.name)

                processFinished()
            }

            override fun onFailed(exception: Exception) {


                processFinished()
            }


            override fun onFailedMsg() {
                processFinished()
            }


        })

        Log.i("Medicina", medicine.name)
        return medicine
    }

    fun getUserMedicineFromFireBase(userMail: String) {

        firestoreService.getUserMedicine(userMail, object : Callback<List<Medication>> {
            override fun onSucces(result: List<Medication>?) {


                listMedicine.postValue(result)
                processFinished()

            }

            override fun onFailed(exception: Exception) {
                processFinished()
            }

            override fun onFailedMsg() {
                processFinished()
            }


        })

    }


    fun processFinished() {
        isLoading.value = true
    }

    override fun stopListener() {

       userMail = firestoreService.getUser().toString()
    }

    /**
     * adds Object to Firebase Collection
     */
    fun addMedicine(medicine: Medication) {


        getUser()
        firestoreService.addUserMedicine(userMail, medicine)
        refresh()


    }

    fun getMedicine() {
        firestoreService.getMedicine(object : Callback<List<Medication>> {
            override fun onSucces(result: List<Medication>?) {
                listMedicine.postValue(result)
                processFinished()
            }

            override fun onFailed(exception: Exception) {
                processFinished()
            }


            override fun onFailedMsg() {
                TODO("Not yet implemented")
            }
        })
    }

    fun getUser() {

         userMail = firestoreService.getUser().toString()

    }

    override fun exito(etiqueta: String, objeto: Any?) {
        val listmed: MutableList<Medication> = objeto as MutableList<Medication>
        listMedicine.postValue(listmed)
    }

    override fun falla(etiqueta: String) {

    }

    override fun actualizacion(etiqueta: String, objeto: Any?) {
        val listmed: MutableList<Medication> = objeto as MutableList<Medication>
        listMedicine.postValue(listmed)
    }
}