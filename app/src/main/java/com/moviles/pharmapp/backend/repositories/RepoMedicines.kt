package com.moviles.pharmapp.backend.repositories

import com.moviles.pharmapp.backend.proxy.FirebaseDB
import com.moviles.pharmapp.model.Calendar
import com.moviles.pharmapp.model.Medication
import com.moviles.pharmapp.model.MedicineInCalendar
import com.moviles.pharmapp.model.User
import kotlin.collections.LinkedHashMap

class RepoMedicines() {

    val firebase = FirebaseDB()

    fun getAllMedicines(listener: BaseBackend, label: String) {
        val collection: MutableMap<String, String> = LinkedHashMap()
        collection["medicines"] = ""
        firebase.getCollection(collection, listener, label, Medication::class)
    }

    fun getUserInformation(listener: BaseBackend, label: String, userUID: String) {
        val collection: MutableMap<String, String> = LinkedHashMap()
        collection["users"] = userUID
        firebase.getDocument(collection, listener, label, User::class)
    }

    fun getCalendarMedicines(listener: BaseBackend, label: String, userUID: String){
        val collection: MutableMap<String, String> = LinkedHashMap()
        collection["users"] = userUID
        collection["calendar"] = ""
        firebase.getCollection(collection, listener, label, Calendar::class)
    }

    fun getMedicinesOfCalendar(listener: BaseBackend, label: String, userUID: String, calendarId: String){
        val collection: MutableMap<String, String> = LinkedHashMap()
        collection["users"] = userUID
        collection["calendar"] = calendarId
        collection["medicines"] = ""
        firebase.getCollection(collection, listener, label, MedicineInCalendar::class)
    }
}