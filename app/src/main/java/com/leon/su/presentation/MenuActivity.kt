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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leon.su.R
import com.leon.su.databinding.ActivityMenuBinding
import com.leon.su.domain.Flags
import com.leon.su.domain.UserResponse
import com.leon.su.presentation.ProductInvoicesActivity.Companion.launchInvoices
import com.leon.su.presentation.observer.UserObserver
import com.leon.su.presentation.viewmodel.UserViewModel
import com.leon.su.utils.makeLoadingDialog
import com.leon.su.utils.setButtonStatus
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
        if (!isAdmin) {
            mBinding.uRegisterButton.visibility = View.GONE
            mBinding.PriceButton.visibility = View.GONE
        }
        mViewModel.getUserData(Firebase.auth.uid.toString())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onGetUserDataLoading() {
        super.onGetUserDataLoading()
        mLoading.show()
    }

    override fun onGetUserDataFailed(e: Throwable) {
        super.onGetUserDataFailed(e)
        ToastUtils.showShort(e.message)
        mLoading.dismiss()
    }

    override fun onGetUserDataSuccess(user: UserResponse?, flags: Flags?) {
        super.onGetUserDataSuccess(user, flags)
        supportActionBar?.apply {
            title = "Welcome, ${user?.data?.nama}"
            setDisplayHomeAsUpEnabled(true)
        }
        mBinding.tbStatus.setButtonStatus(flags?.open == true)
        mLoading.dismiss()
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

    override fun onSetFlagsLoading() {
        super.onSetFlagsLoading()
        mLoading.show()
    }

    override fun onSetFlagsSuccess() {
        super.onSetFlagsSuccess()
        mBinding.tbStatus.setButtonStatus(mBinding.tbStatus.isChecked)
        ToastUtils.showShort(
            if (mBinding.tbStatus.isChecked)
                "Toko dibuka!" else "Toko ditutup!"
        )
        mLoading.dismiss()
    }

    override fun onSetFlagsFailed(e: Throwable) {
        super.onSetFlagsFailed(e)
        ToastUtils.showShort(e.message)
        mLoading.dismiss()
    }

    override fun onClick(v: View?) {
        when (v) {
            mBinding.ProductButton ->
                ActivityUtils.startActivity(ProductActivity::class.java)
            mBinding.PriceButton ->
                ActivityUtils.startActivity(EditProductActivity::class.java)
            mBinding.InvoiceButton -> launchInvoices(false)
            mBinding.StockButton -> launchInvoices(true)
            mBinding.uRegisterButton ->
                ActivityUtils.startActivity(RegisterPegawaiActivity::class.java)
            mBinding.tbStatus -> mViewModel.setFlags(
                Flags(
                    open = mBinding.tbStatus.isChecked
                )
            )
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
