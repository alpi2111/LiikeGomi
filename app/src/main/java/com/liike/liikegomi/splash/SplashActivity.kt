package com.liike.liikegomi.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.liike.liikegomi.databinding.ActivitySplashBinding
import com.liike.liikegomi.login.ui.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val runnable = Runnable {
            binding.logo.animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
            binding.logo.isVisible = true
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 500)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        val runnable = Runnable {
            // TODO: ADD LOGIC TO KNOW IF OPENS LOGIN OR MAIN ACTIVITY
            LoginActivity.launch(this)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 1500)
    }
}