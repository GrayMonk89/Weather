package com.gb.weather.view

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {



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


        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("mylogs_push", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            Log.d("mylogs_push", "$token")
        })

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