package com.liike.liikegomi.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.liike.liikegomi.base.ui.BaseActivity
import com.liike.liikegomi.databinding.ActivityMainBinding
import com.liike.liikegomi.main.ui.adapters.TestCollectionAdapter
import com.liike.liikegomi.main.view_model.MainViewModel
import com.liike.liikegomi.main.view_model.MainViewModelFactory

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

//    private lateinit var mCategoriesAdapter: Prod

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.mCategoriesList.observe(this) {
            val adapter = TestCollectionAdapter(it.size, supportFragmentManager, lifecycle)
            mBinding.viewPagerMain.adapter = adapter
            TabLayoutMediator(mBinding.tabLayoutMain, mBinding.viewPagerMain) { tab, pos ->
                tab.text = it[pos].category
            }.attach()
        }

        mViewModel.getCategories(this)
    }

}