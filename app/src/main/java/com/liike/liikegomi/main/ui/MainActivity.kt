package com.liike.liikegomi.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityMainBinding
import com.liike.liikegomi.main.view_model.MainViewModel
import com.liike.liikegomi.main.view_model.MainViewModelFactory

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    companion object {
        fun launch(appCompatActivity: AppCompatActivity) {
            val intent = Intent(appCompatActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            appCompatActivity.startActivity(intent)
        }
    }

    override val mViewModel: MainViewModel
        get() = getViewModelFactory(this, MainViewModelFactory())

    override fun inflate(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

}