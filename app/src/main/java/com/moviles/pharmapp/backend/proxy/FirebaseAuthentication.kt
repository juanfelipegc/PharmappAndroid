package com.moviles.pharmapp.backend.proxy

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.moviles.pharmapp.backend.repositories.BaseBackend
import com.moviles.pharmapp.model.User
import com.moviles.pharmapp.utilities.Constants

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
                        if (user != null) {
                            listener.exito(label, user.uid)
                        }
                        else
                            listener.falla(Constants.Errors.AUTH_ERROR)
                    }
                })
    }

    fun loginWithEmail(
        pUser: User,
        listener: BaseBackend,
        etiqueta: String
    ) {
        if (!checkInternetConnection()) {
            listener.falla(Constants.Errors.NO_INTERNET)
            return
        }
        mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(pUser.email, pUser.password)
            .addOnCompleteListener(
                activity
            ) { task: Task<AuthResult?> ->
                if (!task.isSuccessful) {
                    val errr = task.exception!!.message
                    val erNoReconocido =
                        "There is no user record corresponding to this identifier. The user may have been deleted."
                    if (errr == erNoReconocido) {
                        listener.falla(Constants.Errors.EMAIL_UNKNOWN)
                    } else {
                        listener.falla(Constants.Errors.WRONG_INFO_EMAIL)
                    }
                } else {
                    val user1 = mAuth.currentUser
                    listener.exito(etiqueta, user1)
                }
            }
    }


}