package com.moviles.pharmapp.view.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.moviles.pharmapp.R
import android.os.Bundle
import com.moviles.pharmapp.backend.repositories.BaseBackend
import com.moviles.pharmapp.backend.repositories.RepoAuth
import com.moviles.pharmapp.model.User
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(),
    BaseBackend {

    lateinit var repoAuth: RepoAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        repoAuth = RepoAuth(this@RegisterActivity)
    }

    fun setupListeners(){
        btnLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        btnRegister.setOnClickListener{
            if(!etName.text.isEmpty() && !etEmail.text.isEmpty()
                && !etPassword.text.isEmpty()) {
                val user = User()
                user.name = etName.text.toString()
                user.email = etEmail.text.toString()
                user.password = etPassword.text.toString()
                repoAuth.registerNewUser(user, this, "registro")
            }
        }
    }

    override fun stopListener() {
        TODO("Not yet implemented")
    }

    override fun exito(etiqueta: String?, objeto: Any?) {
        if(etiqueta==="registro") {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun falla(etiqueta: String?) {
        TODO("Not yet implemented")
    }

    override fun actualizacion(etiqueta: String?, objeto: Any?) {
        TODO("Not yet implemented")
    }
}