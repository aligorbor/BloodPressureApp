package ru.geekbrains.android2.bloodpressureapp.ui

import ru.geekbrains.android2.bloodpressureapp.model.MeasureObj

sealed class AppStateMeasures {
    data class Success(val measures: List<MeasureObj>) : AppStateMeasures()
    data class Error(val error: Throwable) : AppStateMeasures()
    object Loading : AppStateMeasures()
}

