package com.moviles.pharmapp.backend.repositories

import android.content.Context
import com.moviles.pharmapp.backend.proxy.FirebaseDB
import com.moviles.pharmapp.model.BaseModel
import com.moviles.pharmapp.model.Medication
import com.moviles.pharmapp.viewmodel.BaseViewModel
import java.util.*
import kotlin.collections.LinkedHashMap

class RepoMedicines() {

    val firebase = FirebaseDB()

    fun getAllMedicines(listener: BaseViewModel, label: String) {
        val collection: MutableMap<String, String> = LinkedHashMap()
        collection["medicines"] = ""
        //firebase.getCollection(collection, listener, label, Medication::class)
    }
}