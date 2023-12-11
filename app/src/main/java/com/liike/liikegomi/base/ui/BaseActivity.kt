package com.liike.liikegomi.base.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.google.android.material.navigation.NavigationView
import com.liike.liikegomi.R
import com.liike.liikegomi.add_address.ui.AddAddressActivity
import com.liike.liikegomi.add_category.ui.AddCategoryActivity
import com.liike.liikegomi.add_product.ui.AddProductActivity
import com.liike.liikegomi.administrate_category.ui.AdminCategoryActivity
import com.liike.liikegomi.administrate_products.ui.AdminProductsActivity
import com.liike.liikegomi.background.shared_prefs.SharedPreferenceKeys
import com.liike.liikegomi.background.shared_prefs.SharedPrefs
import com.liike.liikegomi.background.utils.MessageUtils
import com.liike.liikegomi.base.ui.components.MainNavHeaderView
import com.liike.liikegomi.base.ui.components.Toolbar
import com.liike.liikegomi.base.viewmodel.BaseViewModel
import com.liike.liikegomi.base.viewmodel.BaseViewModelFactory
import com.liike.liikegomi.login.ui.LoginActivity
import com.liike.liikegomi.main.ui.MainActivity
import com.liike.liikegomi.my_purchases.ui.MyPurchasesActivity
import com.liike.liikegomi.payment.ui.PaymentActivity
import com.liike.liikegomi.register.ui.RegisterActivity
import com.liike.liikegomi.select_edit_address.ui.SelectEditAddressActivity
import com.liike.liikegomi.shopping_cart.ui.ShoppingCartActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel> : AppCompatActivity() {
    lateinit var mBinding: VB
        private set
    abstract val mViewModel: VM

    private var mDrawer: DrawerLayout? = null
    private var mDrawerToggle: ActionBarDrawerToggle? = null
    private var mNavView: NavigationView? = null
    private var mIsCartEmpty = true
    private val mDrawableShoppingCart: Drawable? by lazy { AppCompatResources.getDrawable(this, R.drawable.ic_shopping_cart) }
    private val mDrawableShoppingCartCheckout: Drawable? by lazy { AppCompatResources.getDrawable(this, R.drawable.ic_shopping_cart_checkout) }

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
        mNavView = findViewById(R.id.navigation_view)
        mNavView?.let {
            it.addHeaderView(MainNavHeaderView(this))
            it.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    // TODO: ADD OTHER MENU ITEMS
                    R.id.nav_home -> launchActivityMenu(MainActivity::class) { MainActivity.launch(this) }
                    R.id.nav_close_session -> mViewModel.closeSession()
                    R.id.nav_my_purchases -> launchActivityMenu(MyPurchasesActivity::class) { MyPurchasesActivity.launch(this, comesFromAdminView = false) }
                    R.id.nav_admin_add_product -> launchActivityMenu(AddProductActivity::class) { AddProductActivity.launch(this) }
                    R.id.nav_admin_add_category -> launchActivityMenu(AddCategoryActivity::class) { AddCategoryActivity.launch(this) }
                    R.id.nav_admin_categories_list -> launchActivityMenu(AdminCategoryActivity::class) { AdminCategoryActivity.launch(this) }
                    R.id.nav_admin_products_list -> launchActivityMenu(AdminProductsActivity::class) { AdminProductsActivity.launch(this) }
                    R.id.nav_admin_purchases_list -> launchActivityMenu(MyPurchasesActivity::class) { MyPurchasesActivity.launch(this, comesFromAdminView = true) }
                }
                true
            }
        }
        searchAndShowDrawerAdminMenuOption()
        mViewModel.mIsCartEmpty.observe(this) { isEmpty ->
            mIsCartEmpty = isEmpty
            invalidateOptionsMenu()
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = mutableListOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
            }
            requestPermissions(permissions.toTypedArray(), 0x111)
        } else {
            MessageUtils.dialog(
                context = this,
                title = "Advertencia",
                message = "Todos los permisos son necesarios, abre tus ajustes y habilita todos los permisos",
                okButton = "Ok",
                onAction = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                    finish()
                }
            )
        }
        super.onPostCreate(savedInstanceState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0x111) {
            val allPermissionsAreGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (!allPermissionsAreGranted) {
                MessageUtils.toast(this, "El permiso es necesario, la app se cerrará en 2 segundos...")
                lifecycleScope.launch {
                    delay(2000)
                    finish()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.verifyCartEmpty()
    }

    override fun onRestart() {
        mNavView?.setCheckedItem(R.id.nav_home)
        super.onRestart()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isBoardingActivity())
            return false
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val cart = menu?.findItem(R.id.shopping_cart)
        val contact = menu?.findItem(R.id.contact)
        if (mIsCartEmpty) {
            cart?.icon = mDrawableShoppingCart
        } else {
            cart?.icon = mDrawableShoppingCartCheckout
        }
        val isUserAdmin = SharedPrefs.bool(SharedPreferenceKeys.USER_IS_ADMIN)
        cart?.isVisible = !isUserAdmin
        contact?.isVisible = !isUserAdmin
        if (isShoppingCartView())
            cart?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (mDrawerToggle?.onOptionsItemSelected(item) == true) {
            return true
        }
        return when (item.itemId) {
            R.id.shopping_cart -> {
                ShoppingCartActivity.launch(this)
                true
            }
            R.id.nav_home -> {
                mDrawer?.closeDrawers()
                true
            }
            R.id.contact -> {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://wa.me/+5217821300490")
                }
                startActivity(intent)
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
        mDrawer?.closeDrawers()
        if (this::class != kClass) {
            openClass.invoke()
        }
    }

    private fun searchAndShowDrawerAdminMenuOption() {
        val navMenu = mNavView?.menu
        val adminMenu = navMenu?.findItem(R.id.admin_menu_options)
        val myPurchasesMenu = navMenu?.findItem(R.id.nav_my_purchases)
        val userProfile = navMenu?.findItem(R.id.nav_profile)
        val isUserAdmin = SharedPrefs.bool(SharedPreferenceKeys.USER_IS_ADMIN)
        myPurchasesMenu?.isVisible = !isUserAdmin
        userProfile?.isVisible = !isUserAdmin
        adminMenu?.isVisible = isUserAdmin
    }

    private fun isShoppingCartView(): Boolean {
        return when (this) {
            is ShoppingCartActivity, is PaymentActivity, is SelectEditAddressActivity, is AddAddressActivity -> true
            else -> false
        }
    }
}