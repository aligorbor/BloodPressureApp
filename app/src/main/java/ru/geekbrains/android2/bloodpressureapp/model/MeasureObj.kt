package ru.geekbrains.android2.bloodpressureapp.model

import java.util.*

data class MeasureObj(
    var dateOfMeasure: Date = Date(),
    var sys: Int = 0,
    var dia: Int = 0,
    var pulse: Int = 0,
    var key:String = ""
)
