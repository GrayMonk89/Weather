package com.gb.weather.lesson6

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.gb.weather.utils.*
import java.lang.Thread.sleep

class MainService(serviceName: String = "Same_Service") : IntentService(serviceName) {
    override fun onHandleIntent(intent: Intent?) {
        Log.d(LOG_KEY,"Сервис MainService заработал")
        //TODO вызвать Snackbar

        intent?.let {
            val stringExtra = it.getStringExtra(MAIN_ACTIVITY_KEY)
            Log.d(LOG_KEY,"MainActivity говорит: $stringExtra")
            sleep(MAIN_SERVICE_HARD_WORK_TIME)
            val message = Intent(BROADCAST_RECEIVER_CHANNEL_KEY)
            message.putExtra(MAIN_SERVICE_KEY,"From MainService with love!")
            sendBroadcast(message)
            //LocalBroadcastManager.getInstance(this).sendBroadcast(message)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_KEY,"Сервис MainService завершил работу")
    }
}