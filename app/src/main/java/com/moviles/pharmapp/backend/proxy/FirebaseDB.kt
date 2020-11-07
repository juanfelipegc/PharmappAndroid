package com.moviles.pharmapp.backend.proxy

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ListenerRegistration
import com.moviles.pharmapp.model.BaseModel
import com.moviles.pharmapp.utilities.Constants
import com.moviles.pharmapp.backend.repositories.BaseBackend
import java.util.*
import kotlin.reflect.KClass

class FirebaseDB {
    private val firebase: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val documents = ArrayList<String>()
    private val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build()
    private var lr: ListenerRegistration? = null

    init {
        firebase.firestoreSettings = settings
    }

    fun <T : BaseModel?> updateValue(
        rutaDocumento: Map<String, String?>,
        valorModificar: Map<String?, Any?>?,
        listener: BaseBackend,
        etiqueta: String,
        clase: Any?
    ) {
        var ruta = ""
        for ((key, value) in rutaDocumento) {
            ruta += "$key/"
            if (value == "") break
            ruta += "$value/"
        }
        val rFinal = ruta.substring(0, ruta.length - 1)
        val listaDocumento = firebase.document(rFinal)
        listaDocumento.update(valorModificar!!)
            .addOnSuccessListener { listener.exito(etiqueta, clase) }
            .addOnFailureListener { listener.falla(etiqueta) }
    }

    fun addDocumentAutoID(
        rutaDocumento: Map<String, String?>,
        nuevoDoc: Map<String?, Any?>?,
        listener: BaseBackend,
        etiqueta: String,
        clase: Any
    ) {
        var ruta = ""
        for ((key, value) in rutaDocumento) {
            ruta += "$key/"
            if (value == "") break
            ruta += "$value/"
        }
        val rFinal = ruta.substring(0, ruta.length - 1)
        val coleccion = firebase.collection(rFinal)
        coleccion.add(nuevoDoc!!).addOnSuccessListener(OnSuccessListener { documentReference ->
            if (documentReference != null) {
                val result = documentReference.id
                Log.e("ID", documentReference.id)
                listener.exito(etiqueta, result)
            }
            listener.exito(etiqueta, clase)
        }).addOnFailureListener { listener.falla(etiqueta) }
    }

    fun addDocument(
        rutaDocumento: Map<String, String?>,
        nuevoDoc: Map<String?, Any?>,
        listener: BaseBackend,
        etiqueta: String,
        clase: Any?
    ) {
        var ruta = ""
        for ((key, value) in rutaDocumento) {
            ruta += "$key/"
            if (value == "") break
            ruta += "$value/"
        }
        val rFinal = ruta.substring(0, ruta.length - 1)
        val listaDocumento =
            firebase.document(rFinal)
        listaDocumento.set(nuevoDoc).addOnSuccessListener { listener.exito(etiqueta, clase) }
            .addOnFailureListener { listener.falla(etiqueta) }
    }

    fun deleteDocument(
        rutaDocumento: Map<String, String?>,
        listener: BaseBackend,
        etiqueta: String,
        clase: Any?
    ) {
        var ruta = ""
        for ((key, value) in rutaDocumento) {
            ruta += "$key/"
            if (value == "") break
            ruta += "$value/"
        }
        val rFinal = ruta.substring(0, ruta.length - 1)
        val listaDocumento =
            firebase.document(rFinal)
        listaDocumento.delete().addOnSuccessListener { listener.exito(etiqueta, clase) }
            .addOnFailureListener { listener.falla(etiqueta) }
    }

    fun <T : BaseModel> getDocument(
        rutaDocumento: Map<String, String?>,
        listener: BaseBackend,
        etiqueta: String,
        clase: KClass<T>
    ) {
        var ruta = ""
        for ((key, value) in rutaDocumento) {
            ruta += "$key/"
            if (value == "") break
            ruta += "$value/"
        }
        val rFinal = ruta.substring(0, ruta.length - 1)
        Log.e("URl", rFinal)
        val listaDocumento = firebase.document(rFinal)
        lr = listaDocumento.addSnapshotListener { documentSnapshot, e ->
            if (e != null) listener.falla(Constants.Errors.FAIL_LISTENER)
            if (documentSnapshot != null && documentSnapshot.exists()) {
                val result = documentSnapshot.toObject(clase.java)
                result?.uidDocument = documentSnapshot.id
                Log.e("ID", documentSnapshot.id)
                listener.actualizacion(etiqueta, result)
            }
        }
        listaDocumento.get().addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    val result = document.toObject(clase.java)
                    result?.uidDocument = document.id
                    listener.exito(etiqueta, result)
                } else listener.falla(etiqueta)
            }
            listener.falla(etiqueta)
        })
    }

    fun <T : BaseModel>getCollection(
        rutaColeccion: Map<String, String>,
        listener: BaseBackend,
        etiqueta: String,
        clase: KClass<T>
    ) {
        var ruta = ""
        for ((key, value) in rutaColeccion) {
            ruta += "$key/"
            if (value == "") break
            ruta += "$value/"
        }
        val rFinal = ruta.substring(0, ruta.length - 1)
        val listaColeciones = firebase.collection(rFinal)
        lr = listaColeciones.addSnapshotListener { queryDocumentSnapshots, e ->
            if (e != null) listener.falla(Constants.Errors.FAIL_LISTENER)
            if (queryDocumentSnapshots != null) {
                val list = ArrayList<BaseModel>()
                var count = 0
                for (document in queryDocumentSnapshots) {
                    documents.add(document.id)
                    list.add(document.toObject(clase.java))
                    list[count].uidDocument = document.id
                    count++
                }
                listener.actualizacion(etiqueta, list)
            }
        }
        firebase.collection(rFinal).get().addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                val list = ArrayList<BaseModel>()
                var count = 0
                for (document in task.result) {
                    documents.add(document.id)
                    list.add(document.toObject(clase.java))
                    list[count]?.uidDocument = document.id
                    count++
                }
                listener.exito(etiqueta, list)
            }
            listener.falla(etiqueta)
        })
    }

    fun <T : BaseModel?> getCollectionByField(
        rutaColeccion: Map<String, String?>,
        listener: BaseBackend,
        etiqueta: String,
        clase: Class<T>?,
        campo: String?,
        valor: Any?
    ) {
        var ruta = ""
        for ((key, value) in rutaColeccion) {
            ruta += "$key/"
            if (value == "") break
            ruta += "$value/"
        }
        val rFinal = ruta.substring(0, ruta.length - 1)
        val listaColeciones =
            firebase.collection(rFinal).whereEqualTo(campo!!, valor)
        lr = listaColeciones.addSnapshotListener { queryDocumentSnapshots, e ->
            if (e != null) listener.falla(Constants.Errors.FAIL_LISTENER)
            if (queryDocumentSnapshots != null) {
                val list = ArrayList<T>()
                var count = 0
                for (document in queryDocumentSnapshots) {
                    documents.add(document.id)
                    list.add(document.toObject(clase!!))
                    list[count]?.uidDocument = document.id
                    count++
                }
                listener.actualizacion(etiqueta, list)
            }
        }
        firebase.collection(rFinal).whereEqualTo(campo, valor).get().addOnCompleteListener(
            OnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = ArrayList<T>()
                    var count = 0
                    for (document in task.result) {
                        documents.add(document.id)
                        list.add(document.toObject(clase!!))
                        list[count]?.uidDocument = document.id
                        count++
                    }
                    listener.exito(etiqueta, list)
                }
                listener.falla(etiqueta)
                Log.e("Fallo", "ConsultarColeccion")
            })
    }

    fun stopListener() {
        try {
            lr!!.remove()
        } catch (e: Exception) {
            Log.e("Stop Listener", "Error stopping listeners")
        }
    }
}