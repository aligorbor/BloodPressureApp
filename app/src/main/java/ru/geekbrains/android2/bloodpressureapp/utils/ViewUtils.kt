package ru.geekbrains.android2.bloodpressureapp.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.android2.bloodpressureapp.R

fun View.showSnackBar(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}

fun View.showSnackBar(
    resIdText: Int,
    resIdActionText: Int,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    this.showSnackBar(
        this.context.getString(resIdText),
        this.context.getString(resIdActionText),
        action,
        length
    )
}

fun View.showSnackBar(
    text: String,
    actionText: String = "Ok",
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    this.showSnackBar(text, actionText, {}, length)
}

fun View.showSnackBar(
    resIdText: Int,
    actionText: String = "Ok",
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    this.showSnackBar(this.context.getString(resIdText), actionText, {}, length)
}

fun View.setBackgroundPressure(sys: Int) {
    when {
        sys >= 110 && sys <= 135 -> setBackgroundResource(R.drawable.pressure_back_normal)
        sys > 135 && sys <= 145 -> setBackgroundResource(R.drawable.pressure_back_middle)
        sys > 145 -> setBackgroundResource(R.drawable.pressure_back_max)
        sys < 110 -> setBackgroundResource(R.drawable.pressure_back_min)
    }

}