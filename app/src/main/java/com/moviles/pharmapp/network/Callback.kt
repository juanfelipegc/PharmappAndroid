package com.moviles.pharmapp.network

import com.moviles.pharmapp.model.Medication
import java.lang.Exception

interface Callback<T> {

    fun onSucces(result: T?)

    fun onFailed(exception: Exception)

    fun onFailedMsg()



}