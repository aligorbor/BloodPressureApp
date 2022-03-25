package ru.geekbrains.android2.bloodpressureapp.database

import com.parse.ParseObject
import com.parse.ParseQuery
import ru.geekbrains.android2.bloodpressureapp.model.MeasureObj
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MeasureDBBack4AppImpl : MeasureDB {
    private fun putParseMeasure(parseObject: ParseObject, measure: MeasureObj) {
        parseObject.put("dateofmeasure", measure.dateOfMeasure)
        parseObject.put("sys", measure.sys)
        parseObject.put("dia", measure.dia)
        parseObject.put("pulse", measure.pulse)
    }

    private fun getParseMeasure(obj: ParseObject) = MeasureObj(
        dateOfMeasure = obj.getDate("dateofmeasure") ?: Date(),
        sys = obj.getNumber("sys")?.toInt() ?: 0,
        dia = obj.getNumber("dia")?.toInt() ?: 0,
        pulse = obj.getNumber("pulse")?.toInt() ?: 0,
        key = obj.objectId
    )

    override suspend fun insMeasure(measure: MeasureObj): String? {
        var objectId: String? = null
        val parseObject = ParseObject(CLASS_MEASURE)
        parseObject.save()
        putParseMeasure(parseObject, measure)
        return suspendCoroutine { cont ->
            parseObject.saveInBackground {
                if (it == null) objectId = parseObject.objectId
                cont.resume(objectId)
            }
        }
    }

    override suspend fun updMeasure(key: String, measure: MeasureObj): Boolean {
        val parseObject = ParseObject(CLASS_MEASURE)
        parseObject.objectId = key
        putParseMeasure(parseObject, measure)
        return suspendCoroutine { cont ->
            parseObject.saveInBackground {
                cont.resume(it == null)
            }
        }
    }

    override suspend fun delMeasure(key: String): Boolean {
        val parseObject = ParseObject(CLASS_MEASURE)
        parseObject.objectId = key
        return suspendCoroutine { cont ->
            parseObject.deleteInBackground {
                cont.resume(it == null)
            }
        }
    }

    override suspend fun getMeasure(key: String): MeasureObj? {
        var measure: MeasureObj? = null
        val query = ParseQuery<ParseObject>(CLASS_MEASURE)
        return suspendCoroutine { cont ->
            query.getInBackground(
                key
            ) { obj, e ->
                if (e == null)
                    measure = getParseMeasure(obj)
                cont.resume(measure)
            }
        }
    }

    override suspend fun saveMeasure(measure: MeasureObj): String? {
        var key: String? = null
        if (measure.key == "")
            key = insMeasure(measure)
        else
            if (updMeasure(measure.key, measure)) key = measure.key

        return key
    }

    override suspend fun listMeasure(): List<MeasureObj> {
        var measures: List<MeasureObj> = listOf()
        val query = ParseQuery.getQuery<ParseObject>(CLASS_MEASURE)
        query.orderByAscending("dateofmeasure")
        return suspendCoroutine { cont ->
            query.findInBackground { objects, e ->
                if (e == null) measures = getListParseMeasure(objects)
                cont.resume(measures)
            }
        }
    }

    private fun getListParseMeasure(objects: List<ParseObject>) = objects.map {
        getParseMeasure(it)
    }

    companion object {
        private const val CLASS_MEASURE = "Bloodpressure"
    }
}