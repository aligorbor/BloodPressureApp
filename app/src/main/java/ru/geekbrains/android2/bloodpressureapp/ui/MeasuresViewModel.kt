package ru.geekbrains.android2.bloodpressureapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ru.geekbrains.android2.bloodpressureapp.database.MeasureDB
import ru.geekbrains.android2.bloodpressureapp.database.MeasureDBBack4AppImpl
import ru.geekbrains.android2.bloodpressureapp.database.MeasureDBFirestoreImpl
import ru.geekbrains.android2.bloodpressureapp.model.MeasureObj

class MeasuresViewModel(
    private val liveDataToObserve: MutableLiveData<AppStateMeasures> = MutableLiveData(),
    private var repository: MeasureDB = MeasureDBFirestoreImpl() //MeasureDBBack4AppImpl()
) : ViewModel(), CoroutineScope by MainScope() {

    fun getLiveData() = liveDataToObserve

    fun getListOfMeasures() {
        liveDataToObserve.value = AppStateMeasures.Loading
        launch(Dispatchers.IO) {
            try {
                liveDataToObserve.postValue(
                    AppStateMeasures.Success(repository.listMeasure())
                )
            } catch (e: Exception) {
                liveDataToObserve.postValue(AppStateMeasures.Error(e))
            }
        }
    }

    fun saveMeasure(measure: MeasureObj) {
        liveDataToObserve.value = AppStateMeasures.Loading
        launch(Dispatchers.IO) {
            try {
                if (repository.saveMeasure(measure) != null)
                    liveDataToObserve.postValue(
                        AppStateMeasures.Success(repository.listMeasure())
                    )
            } catch (e: Exception) {
                liveDataToObserve.postValue(AppStateMeasures.Error(e))
            }
        }
    }

    fun deleteMeasure(measure: MeasureObj) {
        liveDataToObserve.value = AppStateMeasures.Loading
        launch(Dispatchers.IO) {
            try {
                if (repository.delMeasure(measure.key))
                    liveDataToObserve.postValue(
                        AppStateMeasures.Success(repository.listMeasure())
                    )
            } catch (e: Exception) {
                liveDataToObserve.postValue(AppStateMeasures.Error(e))
            }
        }
    }

    fun toUseBack4App(useBack4App: Boolean = false) {
        if (useBack4App) repository = MeasureDBBack4AppImpl()
        else repository = MeasureDBFirestoreImpl()
    }
}