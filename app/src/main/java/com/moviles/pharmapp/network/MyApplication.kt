package com.moviles.pharmapp.network

import android.app.Application

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun setConnectionListener(listener: ConnectionReceiver.ConnectionReceiverListener) {
        ConnectionReceiver.connectionReceiverListener = listener
    }


    companion object {
        @get:Synchronized
        lateinit var instance: MyApplication
    }
}