package com.moviles.pharmapp.backend.repositories

import android.util.ArrayMap
import androidx.appcompat.app.AppCompatActivity
import com.moviles.pharmapp.backend.proxy.FirebaseAuthentication
import com.moviles.pharmapp.backend.proxy.FirebaseDB
import com.moviles.pharmapp.model.User
import java.util.*
import kotlin.collections.LinkedHashMap

class RepoAuth(val activity: AppCompatActivity) {

    val firebaseAuth = FirebaseAuthentication(activity)
    val firebase = FirebaseDB()

    fun registerNewUser(user: User, listener: BaseBackend, label: String) {
        firebaseAuth.registerUser(user, listener, label)
    }

    fun loginUser(user: User, listener: BaseBackend, label: String) {
        firebaseAuth.loginWithEmail(user, listener, label)
    }

    fun createUserDB(user: User, listener: BaseBackend, label: String) {
        val collection: MutableMap<String, String?> = LinkedHashMap()
        collection["users"] = user.uidDocument
        val save: MutableMap<String?, Any?> = ArrayMap()
        save["name"] = user.name
        save["email"]=user.email
        save["insurance"]=user.insurance
        save["city"]=user.city
        save["gender"]=user.gender
        firebase.addDocument(collection, save, listener, label, User::class)
    }
}