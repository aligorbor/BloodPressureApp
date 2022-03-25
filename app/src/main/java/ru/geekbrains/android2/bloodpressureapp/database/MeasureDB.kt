package ru.geekbrains.android2.bloodpressureapp.database

import ru.geekbrains.android2.bloodpressureapp.model.MeasureObj

interface MeasureDB {
    suspend fun insMeasure(measure: MeasureObj): String?
    suspend fun updMeasure(key: String, measure: MeasureObj): Boolean
    suspend fun delMeasure(key: String): Boolean
    suspend fun getMeasure(key: String): MeasureObj?
    suspend fun saveMeasure(measure: MeasureObj): String?
    suspend fun listMeasure(): List<MeasureObj>
}