package com.liike.liikegomi.base.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.liike.liikegomi.R
import com.liike.liikegomi.background.shared_prefs.SharedPrefs
import com.liike.liikegomi.background.utils.MessageUtils
import com.liike.liikegomi.base.ui.components.Toolbar
import com.liike.liikegomi.base.viewmodel.BaseViewModel
import com.liike.liikegomi.base.viewmodel.BaseViewModelFactory
import com.liike.liikegomi.login.ui.LoginActivity
import com.liike.liikegomi.register.ui.RegisterActivity

abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel> : AppCompatActivity() {
    lateinit var mBinding: VB
        private set
    abstract val mViewModel: VM

    abstract fun inflate(): VB

    inline fun <reified T : BaseViewModel> getViewModelFactory(owner: ViewModelStoreOwner, factory: BaseViewModelFactory<T>): T {
        return ViewModelProvider(owner, factory)[T::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = inflate()
        setContentView(mBinding.root)
        mViewModel.mProgressMessage.observe(this) { message ->
            if (message.isNullOrBlank())
                MessageUtils.closeProgress(supportFragmentManager)
            else
                MessageUtils.progress(supportFragmentManager, message)
        }
        mViewModel.mToastMessage.observe(this) { message ->
            if (!message.isNullOrBlank())
                MessageUtils.toast(this, message.toString())
        }
        mViewModel.mCloseSession.observe(this) { forceClosingSession ->
            if (forceClosingSession) {
                SharedPrefs.deleteAll()
                LoginActivity.launch(this)
                finish()
            }
        }

        if (!SharedPrefs.arePreferencesAvailable)
            SharedPrefs.initPrefs(this)

        if (!isBoardingActivity())
            mViewModel.listenForLoginOrActivatedUser(this)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar?.configure(onBackPressedDispatcher, canShowBackButton = !isTaskRoot)
        super.onPostCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isBoardingActivity())
            return false
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.shoping_cart -> {
                // OPEN CART ACTIVITY
                MessageUtils.toast(this, "Shopping cart")
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun isBoardingActivity(): Boolean {
        return (this is LoginActivity) or (this is RegisterActivity)
    }
}