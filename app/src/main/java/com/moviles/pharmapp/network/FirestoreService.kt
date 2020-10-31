package com.moviles.pharmapp.network

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.moviles.pharmapp.model.Medication


class FirestoreService {
    val firebaseFirestore = FirebaseFirestore.getInstance()
    val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build()

    init {
        firebaseFirestore.firestoreSettings = settings

    }

    fun getMedicine(callback: Callback<List<Medication>>) {

        firebaseFirestore.collection("medicines")
            .get()
            .addOnSuccessListener { result ->
                for (doc in result){
                    val list = result.toObjects(Medication::class.java)
                    callback.onSucces(list)
                    break
                }
            }
    }

    fun getUserMedicine(callback: Callback<List<Medication>>) {

        firebaseFirestore.collection("users/dummyUser/medicine")
            .get()
            .addOnSuccessListener { result ->
                for (doc in result){
                    val list = result.toObjects(Medication::class.java)
                    callback.onSucces(list)
                    break
                }
            }
    }

    fun findMedicine( medId: String, callback: Callback<Medication>) {

        firebaseFirestore.collection("medicines").document(medId)
            .get()
            .addOnSuccessListener { result ->
                val medicine = result.toObject(Medication::class.java)
                if (medicine==null){

                    Log.i("Medicina","Consulta null")
                }
                if (medicine!=null){

                    callback.onSucces(medicine)
                }
            }
    }

    fun addUserMedicine(medicine: Medication) {


        val medToAdd = hashMapOf(
            "name" to medicine.name,
            "id" to medicine.id,
            "tag" to medicine.tag,
            "description" to medicine.description
            )

        firebaseFirestore.collection("users/dummyUser/medicine").document(medicine.id)
            .set(medToAdd).addOnSuccessListener {

                Log.i("addMedicine","Medicine added")

            }

    }



}