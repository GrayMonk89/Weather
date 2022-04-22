package com.gb.weather.view

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.gb.weather.R
import com.gb.weather.lesson6.MainService
import com.gb.weather.lesson6.MyBroadcastReceiver
import com.gb.weather.lesson6.ThreadsFragment
import com.gb.weather.utils.BROADCAST_RECEIVER_CHANNEL_KEY
import com.gb.weather.utils.MAIN_ACTIVITY_KEY
import com.gb.weather.view.weatherlist.WeatherListFragment
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

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            (R.id.actionThreads) -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.mainContainer, ThreadsFragment.newInstance())
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