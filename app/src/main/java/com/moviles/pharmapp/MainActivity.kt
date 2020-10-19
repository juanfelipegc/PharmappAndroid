package com.moviles.pharmapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    fun configNav(){

        NavigationUI.setupWithNavController(bottom_navigation, Navigation.findNavController(this,R.id.fragContainer))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configNav()

    }
}
