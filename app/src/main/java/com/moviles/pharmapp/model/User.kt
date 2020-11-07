package com.moviles.pharmapp.model

import java.io.Serializable

class User: BaseModel(), Serializable {

    var name = ""
    var email = ""
    var birthDate = ""
    var city = ""
    var insurance = ""
    var password = ""
    var gender = ""
}