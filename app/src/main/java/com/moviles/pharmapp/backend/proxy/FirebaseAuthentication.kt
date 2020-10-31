package com.moviles.pharmapp.backend.proxy

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.moviles.pharmapp.model.User
import com.moviles.pharmapp.utilities.Constants
import com.moviles.pharmapp.backend.repositories.BaseBackend

class FirebaseAuthentication(val activity: AppCompatActivity) : BaseProxy(activity) {

    private lateinit var mAuth: FirebaseAuth

    fun registerUser(usuario: User, listener: BaseBackend, label: String?) {
        if (!checkInternetConnection()) {
            listener.falla(Constants.Errors.NO_INTERNET)
            return
        }
        mAuth = FirebaseAuth.getInstance()
        mAuth.createUserWithEmailAndPassword(usuario.email, usuario.password)
            .addOnCompleteListener(
                activity,
                OnCompleteListener<AuthResult?> { task ->
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful) {
                        listener.falla(Constants.Errors.AUTH_ERROR)
                    } else {
                        val user = mAuth.getCurrentUser()
                        listener.exito(label, user)
                    }
                })
    }
}