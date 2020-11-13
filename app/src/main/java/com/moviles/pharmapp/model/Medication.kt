package com.moviles.pharmapp.model

import java.io.Serializable

class Medication :  BaseModel(), Serializable {

    var name =""
    var tag = ""
    var id = ""
    var image = ""
    var description = ""
    var warning = arrayListOf<String>()

}