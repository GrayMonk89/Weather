package com.gb.weather.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gb.weather.R
import com.gb.weather.view.main.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.mainContainer,
                MainFragment.newInstance()
            ).commit()
        }
    }
}