package com.moviles.pharmapp.view.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.util.LruCache
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import com.moviles.pharmapp.R
import com.moviles.pharmapp.network.ConnectionReceiver
import com.moviles.pharmapp.network.MyApplication
import com.moviles.pharmapp.view.ui.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), HomeFragment.IHomeListener,ConnectionReceiver.ConnectionReceiverListener {

    lateinit var memoryCache: LruCache<String, Bitmap>

    fun configNav(){
        NavigationUI.setupWithNavController(bottom_navigation, Navigation.findNavController(this,
            R.id.fragContainer
        ))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configNav()

        baseContext.registerReceiver(ConnectionReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        MyApplication.instance.setConnectionListener(this)
        var maxMemory = (Runtime.getRuntime().maxMemory()/1024);
        var cacheSize = maxMemory/8;
        memoryCache = LruCache<String, Bitmap>(1024)

    }

    override fun onPause() {
        super.onPause()

        //baseContext.unregisterReceiver(ConnectionReceiver())
    }

    override fun logOut() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {

        var fm = supportFragmentManager



        if(isConnected) {


            val builder = AlertDialog.Builder(this)

            builder.setTitle(R.string.dialoTitlegInternet)
            builder.setMessage(R.string.dialogMessageInternet)

            builder.setNeutralButton("Ok") {DialogInterface, which ->


                Log.i("NetworkInfo","Not found")
            }

            val alertDialog = builder.create()

            alertDialog.show()


            Log.i("NetworkInfo","connected")
        } else {

            val builder = AlertDialog.Builder(this)

            builder.setTitle(R.string.dialoTitlegNoInternet)
            builder.setMessage(R.string.dialogMessageNoInternet)

            builder.setNeutralButton("Ok") {DialogInterface, which ->


                Log.i("MedicineNotFound","Not found")
            }

            val alertDialog = builder.create()

            alertDialog.show()

            Log.i("NetworkInfo","Not  Connected")
        }
    }


}
