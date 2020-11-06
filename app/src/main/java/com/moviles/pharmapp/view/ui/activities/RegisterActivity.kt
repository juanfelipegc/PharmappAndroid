package com.moviles.pharmapp.view.ui.activities

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.moviles.pharmapp.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.get
import com.moviles.pharmapp.backend.repositories.BaseBackend
import com.moviles.pharmapp.backend.repositories.RepoAuth
import com.moviles.pharmapp.model.User
import com.moviles.pharmapp.utilities.Constants
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : BaseActivity(),
    BaseBackend {

    lateinit var repoAuth: RepoAuth
    var user: User = User()
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setupListeners()
        preferences = getSharedPreferences("userData", Context.MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()
        repoAuth = RepoAuth(this@RegisterActivity)
        getDataFromCache()
    }

    fun setupListeners() {
        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        btnRegister.setOnClickListener {
            if (!etName.text.isEmpty() && !etEmail.text.isEmpty()
                && !etPassword.text.isEmpty() && user.birthDate.isNotEmpty()
                && !spinner_gender.selectedItem.toString().equals("Gender")
                && !spinner_city.selectedItem.toString().equals("Cities")
                && etInsurance.text.isNotEmpty()
            ) {
                appearProgressBar()
                user.name = etName.text.toString()
                user.email = etEmail.text.toString()
                user.password = etPassword.text.toString()
                user.city = spinner_city.selectedItem.toString()
                user.insurance = etInsurance.text.toString()
                user.gender = spinner_gender.selectedItem.toString()
                repoAuth.registerNewUser(user, this, "registro")
            } else
                Toast.makeText(
                    applicationContext,
                    "You must complete all the fields to register",
                    Toast.LENGTH_SHORT
                ).show()
        }
        btnBirthDate.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val birthDate = "" + dayOfMonth + "/" + (month + 1) + "/" + year
                    user.birthDate = birthDate
                    btnBirthDate.setText(birthDate)
                },
                year,
                month,
                day
            )
            dpd.show()
        }
    }

    fun getDataFromCache() {
        user.name = preferences.getString("name","").toString()
        etName.setText(user.name)
        user.email = preferences.getString("email","").toString()
        etEmail.setText(user.email)
        user.birthDate = preferences.getString("birthdate","").toString()
        if (user.birthDate.isNotEmpty())
            btnBirthDate.setText(user.birthDate)
        user.insurance = preferences.getString("insurance","").toString()
        etInsurance.setText(user.insurance)
        user.gender = preferences.getString("gender","").toString()
        if (user.gender.isNotEmpty())
            spinner_gender.setSelection(findSelectedSpinner(spinner_gender, user.gender))
        user.city = preferences.getString("city","").toString()
        if (user.city.isNotEmpty())
            spinner_city.setSelection(findSelectedSpinner(spinner_city, user.city))
    }

    private fun findSelectedSpinner(spinner: Spinner, find: String):Int{
        var position = 0
        for (i in 0..spinner.adapter.count){
            if ((spinner.adapter.getItem(i) as String).equals(find)) {
                position = i
                break
            }
        }
        return position
    }

    fun saveUserOnFirebase(uid: String) {
        appearProgressBar()
        user.uidDocument = uid
        repoAuth.createUserDB(user, this, "created")
    }

    fun savePreferences(){
        val editor = preferences.edit()
        editor.putString("name", user.name)
        editor.putString("email", user.email)
        editor.putString("birthdate", user.birthDate)
        editor.putString("gender", user.gender)
        editor.putString("insurance", user.insurance)
        editor.putString("city", user.city)
        editor.apply()
    }

    override fun stopListener() {
        TODO("Not yet implemented")
    }

    override fun exito(etiqueta: String?, objeto: Any?) {
        disappearProgressBar()
        if (etiqueta === "registro") {
            saveUserOnFirebase(objeto as String)
        }
        if (etiqueta === "created") {
            preferences.edit().clear().apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun falla(etiqueta: String?) {
        disappearProgressBar()
        if (etiqueta.equals(Constants.Errors.AUTH_ERROR))
            Toast.makeText(
                applicationContext,
                "There was an error with your register, please check your data and try again",
                Toast.LENGTH_SHORT
            ).show()
        if (etiqueta.equals(Constants.Errors.NO_INTERNET)) {
            Toast.makeText(
                applicationContext,
                "You do not have an stable internet connection but your user data will be saved while you recover your connection",
                Toast.LENGTH_LONG
            ).show()
            savePreferences()
        }
    }

    override fun actualizacion(etiqueta: String?, objeto: Any?) {
        TODO("Not yet implemented")
    }
}