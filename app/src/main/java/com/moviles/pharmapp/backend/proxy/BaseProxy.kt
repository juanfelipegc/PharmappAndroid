package com.moviles.pharmapp.backend.proxy

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat.getSystemService

open class BaseProxy(context: Context) {

    val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
    val currentNetwork = connectivityManager.getActiveNetwork()
    val caps = connectivityManager.getNetworkCapabilities(currentNetwork)

    fun checkInternetConnection(): Boolean {
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}