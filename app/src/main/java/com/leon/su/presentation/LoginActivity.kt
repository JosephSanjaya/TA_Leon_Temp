package com.leon.su.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.firebase.auth.FirebaseUser
import com.leon.su.R
import com.leon.su.databinding.ActivityMainBinding
import com.leon.su.domain.Roles
import com.leon.su.domain.UserResponse
import com.leon.su.presentation.MenuActivity.Companion.openMenu
import com.leon.su.presentation.observer.UserObserver
import com.leon.su.presentation.viewmodel.UserViewModel
import com.leon.su.utils.makeLoadingDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity :
    AppCompatActivity(R.layout.activity_main),
    UserObserver.Interfaces,
    View.OnClickListener {

    private val mBinding by viewBinding(ActivityMainBinding::bind)
    private val mViewModel by viewModel<UserViewModel>()
    private val loading by lazy {
        makeLoadingDialog(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.title = "Login"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        lifecycle.addObserver(UserObserver(this, mViewModel, this))
        mBinding.listener = this
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onClick(v: View?) {
        when (v) {
            mBinding.LoginButton -> {
                mViewModel.login(
                    mBinding.etUsername.text.toString(),
                    mBinding.etPassword.text.toString()
                )
            }
        }
    }

    override fun onLoginLoading() {
        loading.show()
        super.onLoginLoading()
    }

    override fun onLoginSuccess(user: FirebaseUser?) {
        if (user?.isEmailVerified == false) {
            loading.dismiss()
            ActivityUtils.startActivity(VerifyActivity::class.java)
        } else {
            mViewModel.getUserData(user?.uid.toString())
        }
        super.onLoginSuccess(user)
    }

    override fun onLoginFailed(e: Throwable) {
        ToastUtils.showShort(e.message)
        loading.dismiss()
        super.onLoginFailed(e)
    }

    override fun onGetUserDataSuccess(user: UserResponse?) {
        loading.dismiss()
        ActivityUtils.finishAllActivities()
        openMenu(user?.data?.roles == Roles.ADMIN.value)
        super.onGetUserDataSuccess(user)
    }

    override fun onGetUserDataFailed(e: Throwable) {
        onLoginFailed(e)
    }
}
