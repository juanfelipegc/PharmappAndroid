package com.moviles.pharmapp.backend.repositories

interface BaseBackend {
    fun stopListener()
    fun exito(etiqueta: String?, objeto: Any?)
    fun falla(etiqueta: String?)
    fun actualizacion(etiqueta: String?, objeto: Any?)
}