package com.liike.liikegomi.main.ui

import android.os.Bundle
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityMainBinding
import com.liike.liikegomi.main.view_model.MainViewModel
import com.liike.liikegomi.main.view_model.MainViewModelFactory

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override val mViewModel: MainViewModel
        get() = getViewModelFactory(this, MainViewModelFactory())

    override fun inflate(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.template()
    }
}