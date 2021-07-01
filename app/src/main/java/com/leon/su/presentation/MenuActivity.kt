package com.leon.su.presentation

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.leon.su.R
import com.leon.su.databinding.ActivityMenuBinding
import com.leon.su.presentation.observer.UserObserver
import com.leon.su.presentation.viewmodel.UserViewModel
import com.leon.su.utils.makeLoadingDialog
import com.skydoves.bundler.bundle
import com.skydoves.bundler.intentOf
import org.koin.androidx.viewmodel.ext.android.viewModel

class MenuActivity :
    AppCompatActivity(R.layout.activity_menu),
    View.OnClickListener,
    UserObserver.Interfaces {

    private val mBinding by viewBinding(ActivityMenuBinding::bind)
    private val mViewModel by viewModel<UserViewModel>()
    private val mLoading by lazy {
        makeLoadingDialog(false)
    }

    // TODO check isAdmin to disable several menu
    private val isAdmin: Boolean by bundle(IS_ADMIN_KEY, true)

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> mViewModel.doLogout()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(UserObserver(this, mViewModel, this))
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.apply {
            title = "Menu Utama"
            setDisplayHomeAsUpEnabled(true)
        }
        mBinding.listener = this
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onLogoutLoading() {
        mLoading.show()
        super.onLogoutLoading()
    }

    override fun onLogoutFailed(e: Throwable) {
        mLoading.dismiss()
        ToastUtils.showShort(e.message)
        super.onLogoutFailed(e)
    }

    override fun onLogoutSuccess() {
        mLoading.dismiss()
        ActivityUtils.finishAllActivities()
        ActivityUtils.startActivity(LoginActivity::class.java)
        super.onLogoutSuccess()
    }

    override fun onClick(v: View?) {
        when (v) {
            mBinding.ProductButton -> ActivityUtils.startActivity(ProductActivity::class.java)
        }
    }

    companion object {
        const val IS_ADMIN_KEY = "is_admin"
        fun Context.openMenu(isAdmin: Boolean) = intentOf<MenuActivity> {
            putExtra(IS_ADMIN_KEY to isAdmin)
            startActivity(this@openMenu)
        }
    }
}
