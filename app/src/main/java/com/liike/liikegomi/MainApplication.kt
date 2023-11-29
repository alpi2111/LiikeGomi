package com.liike.liikegomi

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.liike.liikegomi.background.database.Dao

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Dao.initDatabase(this)
        Firebase.initialize(this)
    }
}