package com.moviles.pharmapp.network

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.moviles.pharmapp.model.Calendar
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

    fun getUserMedicine(userId: String, callback: Callback<List<Medication>>) {

        firebaseFirestore.collection("users/$userId/medicine")
            .addSnapshotListener { result, e ->
                if (e != null) {
                    Log.w("Listener", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (result != null && !result.isEmpty) {
                    for (doc in result){
                    val list = result.toObjects(Medication::class.java)
                    callback.onSucces(list)
                    break
                }
                } else {

                    callback.onFailedMsg()
                    Log.d("Null data", "Current data: null")
                }
            }
    }

    fun getUserCalendar(callback: Callback<List<Calendar>>) {

        firebaseFirestore.collection("users/dummyUser/calendar")
            .addSnapshotListener { result, e ->
                if (e != null) {
                    Log.w("Listener", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (result != null) {
                    for (doc in result){
                        val list = result.toObjects(Calendar::class.java)
                        callback.onSucces(list)
                        break
                    }
                } else {
                    Log.d("Null data", "Current data: null")
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

    fun addUserMedicine(userId: String, medicine: Medication) {


        val medToAdd = hashMapOf(
            "name" to medicine.name,
            "id" to medicine.id,
            "tag" to medicine.tag,
            "description" to medicine.description
            )

        firebaseFirestore.collection("users/$userId/medicine").document(medicine.id)
            .set(medToAdd).addOnSuccessListener {

                Log.i("addMedicine","Medicine added")

            }

    }


    fun getUser(): String? {


        var name = ""
        var email = ""

        // The user's ID, unique to the Firebase project. Do NOT use this value to
        // authenticate with your backend server, if you have one. Use
        // FirebaseUser.getToken() instead.
        var uid = ""
        Log.i("usuario","buscando user")
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url
            name = user.displayName.toString()
            email = user.email.toString()

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
             uid = user.uid

            Log.i("usuario",name+email+uid)


        }


        return uid
    }



}