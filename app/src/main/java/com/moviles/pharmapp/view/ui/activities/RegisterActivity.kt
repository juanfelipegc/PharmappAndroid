package com.moviles.pharmapp.view.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.moviles.pharmapp.R
import android.os.Bundle
import com.moviles.pharmapp.backend.repositories.BaseBackend
import com.moviles.pharmapp.backend.repositories.RepoAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(),
    BaseBackend {

    private val repoAuth = RepoAuth(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setupListeners()
    }

    fun setupListeners(){
        btnLogin.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        btnRegister.setOnClickListener{

        }
    }

    override fun stopListener() {
        TODO("Not yet implemented")
    }

    override fun exito(etiqueta: String?, objeto: Any?) {
        TODO("Not yet implemented")
    }

    override fun falla(etiqueta: String?) {
        TODO("Not yet implemented")
    }

    override fun actualizacion(etiqueta: String?, objeto: Any?) {
        TODO("Not yet implemented")
    }
}