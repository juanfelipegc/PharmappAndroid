package com.moviles.pharmapp.network

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

        firebaseFirestore.collection("speakers")
            .orderBy("category")
            .get()
            .addOnSuccessListener { result ->
                for (doc in result){
                    val list = result.toObjects(Medication::class.java)
                    callback.onSucces(list)
                    break
                }
            }
    }

    fun getUserMedicine(callback: Callback<MutableList<Medication>>) {

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

    fun FindMedicine(callback: Callback<MutableList<Medication>>, medId: String) {

        firebaseFirestore.collection("medicine/$medId")
            .get()
            .addOnSuccessListener { result ->
                for (doc in result){
                    val list = result.toObjects(Medication::class.java)
                    callback.onSucces(list)
                    break
                }
            }
    }

}