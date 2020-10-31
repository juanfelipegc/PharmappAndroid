package com.moviles.pharmapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moviles.pharmapp.backend.repositories.RepoMedicines
import com.moviles.pharmapp.model.Medication
import com.moviles.pharmapp.network.Callback
import com.moviles.pharmapp.network.FirestoreService
import java.lang.Exception

class MedicineViewModel: ViewModel() {


    val firestoreService = FirestoreService()
    var listMedicine: MutableLiveData<List<Medication>> = MutableLiveData()
    var isLoading = MutableLiveData<Boolean>()
    var medicine = Medication()



    fun refresh() {
        //getMedicine()
        getUserMedicineFromFireBase()
        //repoMedicines.getAllMedicines(this, "Prueba")
    }

    fun findMedicine(code: String): Medication {


        firestoreService.findMedicine(code, object: Callback<Medication>{

            override fun onSucces(result: Medication?){

                medicine = result!!
                Log.i("Medicina",medicine.name)

                processFinished()
            }

            override fun onFailed(exception: Exception) {


                processFinished()
            }



        })

        Log.i("Medicina",medicine.name)
        return medicine
    }

    fun getUserMedicineFromFireBase() {

        firestoreService.getUserMedicine(object: Callback<List<Medication>> {
            override fun onSucces(result: List<Medication>?) {

                listMedicine.postValue(result)
                processFinished()
            }

            override fun onFailed(exception: Exception) {
                processFinished()
            }
        })

    }


    fun processFinished() {
        isLoading.value = true
    }


    /**
     * adds Object to Firebase Collection
     */
    fun addMedicine(medicine: Medication) {



        firestoreService.addUserMedicine(medicine)
        refresh()


    }

    fun getMedicine() {

        firestoreService.getMedicine(object: Callback<List<Medication>> {
            override fun onSucces(result: List<Medication>?) {
                listMedicine.postValue(result)
                processFinished()
            }

            override fun onFailed(exception: Exception) {
                processFinished()
            }
        })
    }

}