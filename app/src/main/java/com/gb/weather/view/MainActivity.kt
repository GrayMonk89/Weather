package com.gb.weather.view

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.gb.weather.MyApp
import com.gb.weather.R
import com.gb.weather.lesson10.MapsFragment
import com.gb.weather.lesson6.MainService
import com.gb.weather.lesson6.MyBroadcastReceiver
import com.gb.weather.lesson6.ThreadsFragment
import com.gb.weather.lesson9.ContentProviderFragment
import com.gb.weather.utils.*
import com.gb.weather.view.historylist.HistoryWeatherListFragment
import com.gb.weather.view.weatherlist.WeatherListFragment
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private fun push(){
//notification
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilderLow = NotificationCompat.Builder(this, CHANNEL_ID_LOW).apply {
            setSmallIcon(R.drawable.ic_map_pin)
            setContentTitle(NOTIFICATION_TITLE_LOW)
            setContentText(NOTIFICATION_TEXT_LOW)
            priority = NotificationManager.IMPORTANCE_LOW
        }
        val notificationBuilderHigh = NotificationCompat.Builder(this, CHANNEL_ID_HIGH).apply {
            setSmallIcon(R.drawable.ic_map_marker)
            setContentTitle(NOTIFICATION_TITLE_HIGH)
            setContentText(NOTIFICATION_TEXT_HIGH)
            priority = NotificationManager.IMPORTANCE_HIGH
        }

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channelNameLow = "Name $CHANNEL_ID_LOW"
            val channelDescriptionLow = "Description $CHANNEL_ID_LOW"
            val channelPriorityLow = NotificationManager.IMPORTANCE_LOW
            val channelLow = NotificationChannel(CHANNEL_ID_LOW,channelNameLow,channelPriorityLow).apply {
                description = channelDescriptionLow
            }
            notificationManager.createNotificationChannel(channelLow)
        }

        notificationManager.notify(NOTIFICATION_ID_LOW,notificationBuilderLow.build())

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val channelNameHigh = "Name $CHANNEL_ID_HIGH"
            val channelDescriptionHigh = "Description $CHANNEL_ID_HIGH"
            val channelPriorityHigh = NotificationManager.IMPORTANCE_HIGH
            val channelHigh = NotificationChannel(CHANNEL_ID_HIGH,channelNameHigh,channelPriorityHigh).apply {
                description = channelDescriptionHigh
            }
            notificationManager.createNotificationChannel(channelHigh)
        }

        notificationManager.notify(NOTIFICATION_ID_HIGH,notificationBuilderHigh.build())

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.mainContainer,
                WeatherListFragment.newInstance()
            ).commit()
        }

        startService(Intent(this, MainService::class.java).apply {
            putExtra(MAIN_ACTIVITY_KEY, "Hail to the Service")
        })

        val receiver = MyBroadcastReceiver()
        registerReceiver(receiver, IntentFilter(BROADCAST_RECEIVER_CHANNEL_KEY))
/*        LocalBroadcastManager.getInstance(this)
            .registerReceiver(receiver, IntentFilter(BROADCAST_RECEIVER_CHANNEL_KEY))*/
        Thread { MyApp.getHistoryDAO().getAll() }.start()

        push()



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            (R.id.actionHistory) -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.mainContainer, HistoryWeatherListFragment.newInstance())
                    .addToBackStack("")
                    .commit()
            }
            (R.id.actionThreads) -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.mainContainer, ThreadsFragment.newInstance())
                    .addToBackStack("")
                    .commit()
            }
            (R.id.actionWorkWithContentProvider) -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.mainContainer, ContentProviderFragment.newInstance())
                    .addToBackStack("")
                    .commit()
            }
            (R.id.actionGoToTheMap) -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.mainContainer, MapsFragment())
                    .addToBackStack("")
                    .commit()
            }
            (R.id.actionExit) -> {
                exitProcess(0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}