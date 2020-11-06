package com.moviles.pharmapp.view.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.moviles.pharmapp.R
import com.moviles.pharmapp.backend.repositories.BaseBackend
import com.moviles.pharmapp.backend.repositories.RepoAuth
import com.moviles.pharmapp.model.User
import com.moviles.pharmapp.utilities.Constants
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.etEmail
import kotlinx.android.synthetic.main.activity_login.etPassword
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : BaseActivity(), BaseBackend {

    lateinit var repoAuth: RepoAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        repoAuth = RepoAuth(this@LoginActivity)
    }

    fun setupListeners() {
        btnlogin.setOnClickListener {
            if (etEmail.text.isNotEmpty()
                && etPassword.text.isNotEmpty()
            ) {
                appearProgressBar()
                val user = User()
                user.email = etEmail.text.toString()
                user.password = etPassword.text.toString()
                repoAuth.loginUser(user, this, "login")
            }
            else
                Toast.makeText(
                    applicationContext,
                    "You must complete all the fields to continue",
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    override fun stopListener() {
        TODO("Not yet implemented")
    }

    override fun exito(etiqueta: String?, objeto: Any?) {
        disappearProgressBar()
        if (etiqueta === "login") {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun falla(etiqueta: String?) {
        disappearProgressBar()
        if (etiqueta.equals(Constants.Errors.EMAIL_UNKNOWN))
            Toast.makeText(
                applicationContext,
                "The email you enter is not registered, please try again",
                Toast.LENGTH_SHORT
            ).show()
        if (etiqueta.equals(Constants.Errors.NO_INTERNET))
            Toast.makeText(
                applicationContext,
                "You do not have an stable internet connection to do the register, try again later ",
                Toast.LENGTH_LONG
            ).show()
    }

    override fun actualizacion(etiqueta: String?, objeto: Any?) {
        TODO("Not yet implemented")
    }
}