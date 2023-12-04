package com.liike.liikegomi.base.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding
import com.google.android.material.navigation.NavigationView
import com.liike.liikegomi.R
import com.liike.liikegomi.add_product.ui.AddProductActivity
import com.liike.liikegomi.background.shared_prefs.SharedPreferenceKeys
import com.liike.liikegomi.background.shared_prefs.SharedPrefs
import com.liike.liikegomi.background.utils.MessageUtils
import com.liike.liikegomi.base.ui.components.MainNavHeaderView
import com.liike.liikegomi.base.ui.components.Toolbar
import com.liike.liikegomi.base.viewmodel.BaseViewModel
import com.liike.liikegomi.base.viewmodel.BaseViewModelFactory
import com.liike.liikegomi.login.ui.LoginActivity
import com.liike.liikegomi.main.ui.MainActivity
import com.liike.liikegomi.register.ui.RegisterActivity
import kotlin.reflect.KClass

abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel> : AppCompatActivity() {
    lateinit var mBinding: VB
        private set
    abstract val mViewModel: VM

    private var mDrawer: DrawerLayout? = null
    private var mDrawerToggle: ActionBarDrawerToggle? = null

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
        mDrawer = findViewById(R.id.drawer)

        mDrawer.let { drawer ->
            if (drawer != null) {
                mDrawerToggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
                drawer.addDrawerListener(mDrawerToggle!!)
                mDrawerToggle!!.syncState()
                toolbar?.configureDrawer(drawer)
            }
        }
        val navView = findViewById<NavigationView>(R.id.navigation_view)
        navView?.addHeaderView(MainNavHeaderView(this))
        navView?.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                // TODO: ADD OTHER MENU ITEMS
                R.id.nav_home -> launchActivityMenu(MainActivity::class) { MainActivity.launch(this) }
                R.id.nav_close_session -> mViewModel.closeSession()
                R.id.nav_my_purchases -> launchActivityMenu(AddProductActivity::class) { AddProductActivity.launch(this) }
            }
            true
        }
        searchAndShowDrawerAdminMenuOption()
        super.onPostCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isBoardingActivity())
            return false
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (mDrawerToggle?.onOptionsItemSelected(item) == true) {
            return true
        }
        return when (item.itemId) {
            R.id.shoping_cart -> {
                // OPEN CART ACTIVITY
                MessageUtils.toast(this, "Shopping cart")
                true
            }
            R.id.nav_home -> {
                mDrawer?.closeDrawers()
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

    private inline fun <reified T : AppCompatActivity> launchActivityMenu(kClass: KClass<T>, openClass: () -> Unit) {
        if (this::class == kClass) {
            mDrawer?.closeDrawers()
        } else {
            openClass.invoke()
        }
    }

    private fun searchAndShowDrawerAdminMenuOption() {
        val navView = findViewById<NavigationView>(R.id.navigation_view)
        val navMenu = navView?.menu
        val adminMenu = navMenu?.findItem(R.id.admin_menu_options)
        val isUserAdmin = SharedPrefs.bool(SharedPreferenceKeys.USER_IS_ADMIN)
        adminMenu?.isVisible = isUserAdmin
    }
}