package com.moviles.pharmapp.view.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.moviles.pharmapp.R
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(){
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
    }
}