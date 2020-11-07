package com.moviles.pharmapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.moviles.pharmapp.backend.repositories.BaseBackend
import com.moviles.pharmapp.backend.repositories.RepoMedicines
import com.moviles.pharmapp.model.Calendar
import com.moviles.pharmapp.model.Medication
import com.moviles.pharmapp.model.MedicineInCalendar
import com.moviles.pharmapp.model.User

class HomeViewModel: ViewModel(), BaseBackend {

    var liveUser: MutableLiveData<User> = MutableLiveData()
    var liveCalendar: MutableLiveData<List<Calendar>> = MutableLiveData()
    val repoMedicines: RepoMedicines = RepoMedicines()
    var medicine = Medication()
    private var firebaseAuth = FirebaseAuth.getInstance()
    lateinit var listCalendar: MutableList<Calendar>


    fun refresh() {

        var uIdUser = firebaseAuth.uid
        if (uIdUser != null) {
            repoMedicines.getUserInformation(this,label = "Bring user",userUID = uIdUser)
            repoMedicines.getCalendarMedicines(this, label= "Bring calendar", userUID = uIdUser)
        }
    }



    override fun stopListener() {
        TODO("Not yet implemented")
    }

    fun askForMedicines(idCalendar:String) {
        var uIdUser = firebaseAuth.uid
        if (uIdUser != null) {
            repoMedicines.getMedicinesOfCalendar(this, "Bring medicines&&$idCalendar", uIdUser, idCalendar)
        }
    }

    override fun exito(etiqueta: String, objeto: Any?) {

        if (etiqueta === "Bring user") {
            val user: User = objeto as User
            liveUser.postValue(user)
        }
        if (etiqueta === "Bring calendar") {
            listCalendar = objeto as MutableList<Calendar>
            for (calendar in listCalendar){
                if (calendar.uidDocument != null)
                    askForMedicines(calendar.uidDocument!!)
            }
        }
        if (etiqueta.contains("Bring medicines")){
            val idCalendarSplit = etiqueta.split("&&")
            val idCalendar = idCalendarSplit[idCalendarSplit.size-1]
            val listMedicine = objeto as MutableList<MedicineInCalendar>

            for (calendar in listCalendar){
                if (idCalendar === calendar.uidDocument){
                  calendar.medicines = listMedicine
                }
            }
            liveCalendar.postValue(listCalendar)
        }
    }

    override fun falla(etiqueta: String) {
        print(etiqueta)
    }

    override fun actualizacion(etiqueta: String, objeto: Any?) {
        if (etiqueta === "Bring user") {
            val user: User = objeto as User
            liveUser.postValue(user)
        }
        if (etiqueta === "Bring calendar") {
            listCalendar = objeto as MutableList<Calendar>
            for (calendar in listCalendar){
                if (calendar.uidDocument != null)
                    askForMedicines(calendar.uidDocument!!)
            }
        }
        if (etiqueta.contains("Bring medicines")){
            val idCalendarSplit = etiqueta.split("&&")
            val idCalendar = idCalendarSplit[idCalendarSplit.size-1]
            val listMedicine = objeto as MutableList<MedicineInCalendar>

            for (calendar in listCalendar){
                if (idCalendar.equals(calendar.uidDocument)){
                    calendar.medicines = listMedicine
                }
            }
            liveCalendar.postValue(listCalendar)
        }
    }
}