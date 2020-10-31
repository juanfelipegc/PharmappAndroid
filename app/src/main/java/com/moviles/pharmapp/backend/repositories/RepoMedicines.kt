package com.moviles.pharmapp.backend.repositories

import com.moviles.pharmapp.backend.proxy.FirebaseDB
import com.moviles.pharmapp.model.Medication
import kotlin.collections.LinkedHashMap

class RepoMedicines() {

    val firebase = FirebaseDB()

    fun getAllMedicines(listener: BaseBackend, label: String) {
        val collection: MutableMap<String, String> = LinkedHashMap()
        collection["medicines"] = ""
        firebase.getCollection(collection, listener, label, Medication::class)
    }
}