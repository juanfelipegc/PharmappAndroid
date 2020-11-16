package com.moviles.pharmapp.model

import java.io.Serializable

class Calendar: BaseModel(), Serializable {

    var times = 0
    var day = ""
    var medicines: MutableList<MedicineInCalendar>? = null
}