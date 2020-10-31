package com.moviles.pharmapp.backend.repositories

import androidx.appcompat.app.AppCompatActivity
import com.moviles.pharmapp.backend.proxy.FirebaseAuthentication
import com.moviles.pharmapp.model.User

class RepoAuth(val activity: AppCompatActivity) {

    val firebaseAuth = FirebaseAuthentication(activity)

    fun registerNewUser(user: User, listener: BaseBackend, label: String){
        firebaseAuth.registerUser(user, listener, label)
    }
}