package ru.geekbrains.android2.bloodpressureapp.database

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import ru.geekbrains.android2.bloodpressureapp.App
import ru.geekbrains.android2.bloodpressureapp.model.MeasureObj
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MeasureDBFirestoreImpl : MeasureDB {

    private fun toDocument(measure: MeasureObj) = hashMapOf(
        "dateofmeasure" to measure.dateOfMeasure,
        "sys" to measure.sys,
        "dia" to measure.dia,
        "pulse" to measure.pulse
    )

    private fun toMeasureObj(document: DocumentSnapshot) =
        if (document.data == null) null else MeasureObj(
            dateOfMeasure = (document.data!!["dateofmeasure"] as Timestamp).toDate(),
            sys = (document.data!!["sys"] as Number).toInt(),
            dia = (document.data!!["dia"] as Number).toInt(),
            pulse = (document.data!!["pulse"] as Number).toInt(),
            key = document.id
        )

    override suspend fun insMeasure(measure: MeasureObj): String? {
        return suspendCoroutine { cont ->
            App.dbInstance.collection(CLASS_MEASURE)
                .add(toDocument(measure))
                .addOnSuccessListener { documentReference ->
                    cont.resume(documentReference.id)
                }
                .addOnFailureListener { e ->
                    cont.resume(null)
                }
        }
    }

    override suspend fun updMeasure(key: String, measure: MeasureObj): Boolean {
        return suspendCoroutine { cont ->
            App.dbInstance.collection(CLASS_MEASURE)
                .document(key)
                .set(toDocument(measure)).addOnCompleteListener { task ->
                    cont.resume(task.isSuccessful)
                }
        }
    }

    override suspend fun delMeasure(key: String): Boolean {
        return suspendCoroutine { cont ->
            App.dbInstance.collection(CLASS_MEASURE)
                .document(key)
                .delete().addOnCompleteListener { task ->
                    cont.resume(task.isSuccessful)
                }
        }
    }

    override suspend fun getMeasure(key: String): MeasureObj? {
        return suspendCoroutine { cont ->
            App.dbInstance.collection(CLASS_MEASURE)
                .document(key)
                .get()
                .addOnSuccessListener { document ->
                    cont.resume(toMeasureObj(document))
                }
                .addOnFailureListener { e ->
                    cont.resume(null)
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
        val measures: MutableList<MeasureObj> = mutableListOf()
        return suspendCoroutine { cont ->
            App.dbInstance.collection(CLASS_MEASURE)
                .orderBy("dateofmeasure", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val obj = toMeasureObj(document)
                        if (obj != null) measures.add(obj)
                    }
                    cont.resume(measures)
                }
                .addOnFailureListener { e ->
                    cont.resume(measures)
                }
        }
    }

    companion object {
        private const val CLASS_MEASURE = "Bloodpressure"
    }
}