package com.moviles.pharmapp.view.ui.activities

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.moviles.pharmapp.view.utilities.ProgressBar

open class BaseActivity: AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    fun disappearProgressBar() {
        progressBar.dismiss()
    }

    fun appearProgressBar() {
        progressBar = ProgressBar()
        progressBar.listener = FallaDialog()
        progressBar.show(supportFragmentManager, "Progress Bar")
    }

    inner class FallaDialog: ProgressBar.IProgresBar {
        override fun fallo() {
            Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }
}