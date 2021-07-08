package com.leon.su.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.firebase.auth.FirebaseUser
import com.leon.su.R
import com.leon.su.databinding.ActivityMainBinding
import com.leon.su.domain.Flags
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
        mBinding.imageView.load(R.drawable.app_icon_tp)
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
            mBinding.btnForgotPassword -> ActivityUtils.startActivity(
                ForgotPasswordSendActivity::class.java
            )
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

    override fun onGetUserDataSuccess(user: UserResponse?, flags: Flags?) {
        loading.dismiss()
        ActivityUtils.finishAllActivities()
        when (val isAdmin = user?.data?.roles == Roles.ADMIN.value) {
            true -> openMenu(isAdmin)
            else -> if (flags?.open == true) {
                openMenu(isAdmin)
            } else {
                ToastUtils.showShort("Mohon maaf, toko sedang tutup!")
            }
        }
        super.onGetUserDataSuccess(user, flags)
    }

    override fun onGetUserDataFailed(e: Throwable) {
        onLoginFailed(e)
    }
}
