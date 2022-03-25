package ru.geekbrains.android2.bloodpressureapp.utils

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

fun dateToStr(date: Date): String {
    val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    return localDate.format(DateTimeFormatter.ofPattern("dd MM yyyy"))
}

fun dateToStrHM(date: Date): String {
    val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    return localDate.format(DateTimeFormatter.ofPattern("HH:mm"))
}

fun dayChanged(date1: Date, date2: Date): Boolean {
    val c = Calendar.getInstance()
    c.time = date1
    val day1 = c.get(Calendar.DAY_OF_YEAR)
    c.time = date2
    val day2 = c.get(Calendar.DAY_OF_YEAR)
    return day1 != day2
}