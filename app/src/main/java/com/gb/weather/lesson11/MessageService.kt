package com.gb.weather.lesson11

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.gb.weather.R
import com.gb.weather.utils.*
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

//  AAAAqmAiVKw:APA91bEgdYl9ANvZmqnkEGYZTdZSwEkjEIuNWJX36iDHxDXCejE0pXsUGEjU1fzQUfH4lHA8oDUOsxJ21ipJNiNMNllH62rfZIIUrsc9NR7sMFSOgB1KRL9ycZieJJVxLNAzuWydM48u

class MessageService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        if(!message.data.isNullOrEmpty()){
            val title = message.data[KEY_TITLE]
            val message = message.data[KEY_MESSAGE]
            if(!title.isNullOrEmpty()&&!message.isNullOrEmpty()){
                push(title,message)
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(LOG_KEY, token)
    }

    private fun push(title:String,message:String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilderHigh = NotificationCompat.Builder(this, CHANNEL_ID_HIGH).apply {
            setSmallIcon(R.drawable.ic_map_marker)
            setContentTitle(title)
            setContentText(message)
            priority = NotificationManager.IMPORTANCE_HIGH
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelNameHigh = "Name $CHANNEL_ID_HIGH"
            val channelDescriptionHigh = "Description $CHANNEL_ID_HIGH"
            val channelPriorityHigh = NotificationManager.IMPORTANCE_HIGH
            val channelHigh =
                NotificationChannel(CHANNEL_ID_HIGH, channelNameHigh, channelPriorityHigh).apply {
                    description = channelDescriptionHigh
                }
            notificationManager.createNotificationChannel(channelHigh)
        }

        notificationManager.notify(NOTIFICATION_ID_HIGH, notificationBuilderHigh.build())

    }
}