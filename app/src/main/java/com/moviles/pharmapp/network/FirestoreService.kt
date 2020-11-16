package com.moviles.pharmapp.network

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.functions.FirebaseFunctions
import com.moviles.pharmapp.model.Calendar
import com.moviles.pharmapp.model.Medication


class FirestoreService {

    val firebaseFirestore = FirebaseFirestore.getInstance()
    val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build()

    private lateinit var functions: FirebaseFunctions


    init {
        firebaseFirestore.firestoreSettings = settings
        functions = FirebaseFunctions.getInstance()

    }

    fun getMedicine(callback: Callback<List<Medication>>) {

        firebaseFirestore.collection("medicines")
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    val list = result.toObjects(Medication::class.java)
                    callback.onSucces(list)
                    break
                }
            }
    }

    fun getUserMedicine(userId: String, callback: Callback<List<Medication>>) {

        firebaseFirestore.collection("users/$userId/medicine").orderBy("name")
            .addSnapshotListener { result, e ->
                if (e != null) {
                    Log.w("Listener", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (result != null && !result.isEmpty) {
                    for (doc in result) {
                        val list = result.toObjects(Medication::class.java)

                        Log.w("cruce", list[0].warning.isEmpty().toString())

                        Log.w("cruce", list[0].name)
                        callback.onSucces(list)
                        break
                    }
                } else {

                    callback.onFailedMsg("Medicine not found")
                    Log.d("Null data", "Current data: null")
                }
            }
    }


    fun findMedicine(medId: String, callback: Callback<Medication>) {

        firebaseFirestore.collection("medicines").document(medId)
            .get()
            .addOnSuccessListener { result ->
                val medicine = result.toObject(Medication::class.java)
                if (medicine == null) {

                    callback.onFailedMsg("Medicine not found in our DataBase")

                    Log.i("Medicina", "Consulta null")
                }
                if (medicine != null) {

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

                Log.i("addMedicine", "Medicine added")

            }

    }


    fun getUser(): FirebaseUser? {


        var name = ""
        var email = ""

        // The user's ID, unique to the Firebase project. Do NOT use this value to
        // authenticate with your backend server, if you have one. Use
        // FirebaseUser.getToken() instead.
        var uid = ""
        Log.i("usuario", "buscando user")
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url
            name = user.displayName.toString()
            email = user.email.toString()

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            uid = user.uid

            Log.i("usuario", name + email + uid)


        }


        return user
    }

    fun getUserCalendar(callback: Callback<List<Calendar>>) {

        firebaseFirestore.collection("users/dummyUser/calendar")
            .addSnapshotListener { result, e ->
                if (e != null) {
                    Log.w("Listener", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (result != null) {
                    for (doc in result) {
                        val list = result.toObjects(Calendar::class.java)
                        callback.onSucces(list)
                        break
                    }
                } else {
                    Log.d("Null data", "Current data: null")
                }
            }
    }

//    fun medInteractions(
//        uid: String,
//        callback: Callback<String>
//    ) {
//        // Create the arguments to the callable function.
//        val data = hashMapOf(
//            "uid" to uid
//        )
//
//        var result = ""
//         functions
//            .getHttpsCallable("addMedicine")
//            .call(data)
//            .continueWith { task ->
//                // This continuation runs on either success or failure, but if the task
//                // has failed then result will throw an Exception which will be
//                // propagated down.
//                result = task.result?.data as String
//
//                if (result!="") {
//                    Log.e("FuncionSebas", result)
//                    callback.onSucces(result)
//                } else {
//
//                    Log.e("FuncionSebas", result)
//                    callback.onFailedMsg(result)
//                }
//
//                Log.e("FuncionSebas", result)
//
//
//            }
//
//
//
//    }

    fun medInteractions(
        uid: String,
        callback: Callback<String>
    ) {
        // Create the arguments to the callable function.
        val data = hashMapOf(
            "uid" to uid
        )

        var result = ""
        functions
            .getHttpsCallable("addMedicine")
            .call(data)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then result will throw an Exception which will be
                // propagated down.
                result = task.result?.data as String
                if (result != "") {
                    Log.e("FuncionSebas", result)
                    callback.onSucces(result)
                } else {
                    Log.e("FuncionSebas", result)
                    callback.onFailedMsg(result)
                }
                Log.e("FuncionSebas", result)
            }
    }

    fun addMedicine(
        uid: String,
        callback: Callback<String>
    ) {
        // Create the arguments to the callable function.
        val data = hashMapOf(
            "uid" to uid
        )

        var result = ""
        functions
            .getHttpsCallable("addMedicineRisk")
            .call(data)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then result will throw an Exception which will be
                // propagated down.
                result = task.result?.data as String
                if (result != "") {
                    Log.e("FuncionSebas", result)
                    callback.onSucces(result)
                } else {
                    Log.e("FuncionSebas", result)
                    callback.onFailedMsg(result)
                }
                Log.e("FuncionSebas", result)
            }
    }

}