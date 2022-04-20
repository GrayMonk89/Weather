package com.gb.weather.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

const val KEY_BUNDLE_WEATHER = "key"
const val LOG_KEY = "(╯°□°)╯┻━━┻"

val SERVER_SIDE = 500..599
val CLIENT_SIDE = 400..499
val RESPONSEOK = 200..299

fun View.showSnackBar(
    text: String,
    actionText: String,
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE
) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}


class MyUtils {

}
