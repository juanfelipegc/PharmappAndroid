package com.moviles.pharmapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moviles.pharmapp.backend.repositories.BaseBackend
import com.moviles.pharmapp.backend.repositories.RepoMedicines
import com.moviles.pharmapp.model.Medication
import com.moviles.pharmapp.model.User
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
    lateinit var userId: String
    var answer = String()
    var liveinteractionRes: MutableLiveData<String> = MutableLiveData()
    var liveMedicineAdded: MutableLiveData<String> = MutableLiveData()

    fun refresh() {
        repoMedicines.getAllMedicines(this, "Prueba")
    }

    fun refreshUserMedicines() {

        getUser()
        getUserMedicineFromFireBase(userId)
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


            override fun onFailedMsg(msg: String) {

                processFinished()
            }


        })

        Log.i("Medicina", medicine.name)
        return medicine
    }

    fun x(s: String) {


        answer = s
        Log.i("FuncionSebas2",answer+"xxxx")

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

            override fun onFailedMsg(msg: String) {
                processFinished()
            }


        })

    }


    fun processFinished() {
        isLoading.value = true
    }

    override fun stopListener() {

        userId = firestoreService.getUser().toString()
    }

    /**
     * adds Object to Firebase Collection
     */
    fun addMedicine(medicine: Medication) {
//        getUser()
//        firestoreService.addUserMedicine(userId, medicine)
        firestoreService.medInteractions(medicine.id, object : Callback<String> {
            override fun onSucces(result: String?) {
                liveinteractionRes.postValue(result)
            }

            override fun onFailed(exception: Exception) {
            }


            override fun onFailedMsg(msg: String) {
                TODO("Not yet implemented")
            }
        })
        refresh()
    }

    fun addMedicineRisky(medicine: Medication) {
        firestoreService.addMedicine(medicine.id, object : Callback<String> {
            override fun onSucces(result: String?) {
                liveMedicineAdded.postValue(result)
            }

            override fun onFailed(exception: Exception) {
            }


            override fun onFailedMsg(msg: String) {
                TODO("Not yet implemented")
            }
        })
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


            override fun onFailedMsg(msg: String) {
                TODO("Not yet implemented")
            }
        })
    }



    fun getUser(): String {

        userId = firestoreService.getUser()?.uid.toString()

        return userId

    }

    fun getUserMail(): String {

        var userMail = firestoreService.getUser()?.email.toString()

        return userMail

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