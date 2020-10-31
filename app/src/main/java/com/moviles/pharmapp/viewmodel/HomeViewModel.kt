package com.moviles.pharmapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.moviles.pharmapp.backend.repositories.BaseBackend
import com.moviles.pharmapp.backend.repositories.RepoMedicines
import com.moviles.pharmapp.model.Medication
import com.moviles.pharmapp.model.User

class HomeViewModel: ViewModel(), BaseBackend {

    var liveUser: MutableLiveData<User> = MutableLiveData()
    val repoMedicines: RepoMedicines = RepoMedicines()
    private var firebaseAuth = FirebaseAuth.getInstance()

    fun refresh() {

        var uIdUser = firebaseAuth.uid
        if (uIdUser != null) {
            repoMedicines.getUserInformation(this,label = "Bring user",userUID = uIdUser)
        }
    }
    override fun stopListener() {
        TODO("Not yet implemented")
    }

    override fun exito(etiqueta: String?, objeto: Any?) {
        val user: User = objeto as User
        liveUser.postValue(user)
    }

    override fun falla(etiqueta: String?) {
        TODO("Not yet implemented")
    }

    override fun actualizacion(etiqueta: String?, objeto: Any?) {
        TODO("Not yet implemented")
    }


}