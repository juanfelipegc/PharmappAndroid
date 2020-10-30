package com.moviles.pharmapp.network

import java.lang.Exception

interface Callback<T> {

    fun onSucces(result: T?)

    fun onFailed(exception: Exception)

}