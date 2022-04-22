package com.gb.weather.lesson6

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.gb.weather.utils.LOG_KEY
import com.gb.weather.utils.MAIN_SERVICE_KEY

class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        intent.let {
            val stringExtra = it.getStringExtra(MAIN_SERVICE_KEY)
            Log.d(LOG_KEY,"MainService говорит через MyBroadcastReceiver: $stringExtra")
        }
    }
}