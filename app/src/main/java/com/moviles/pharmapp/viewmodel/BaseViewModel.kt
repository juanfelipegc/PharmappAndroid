package com.moviles.pharmapp.viewmodel

interface BaseViewModel {
    fun stopListener()
    fun exito(etiqueta: String?, objeto: Any?)
    fun falla(etiqueta: String?)
    fun actualizacion(etiqueta: String?, objeto: Any?)
}