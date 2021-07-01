/*
 * Copyright (c) 2021 Designed and developed by Joseph Sanjaya, S.T., M.Kom., All Rights Reserved.
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/))
 */

package com.leon.su.presentation

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.leon.su.R
import com.leon.su.data.users
import com.leon.su.domain.Roles
import com.leon.su.domain.UserResponse
import com.leon.su.presentation.MenuActivity.Companion.openMenu
import com.leon.su.presentation.observer.PasswordObserver
import com.leon.su.presentation.observer.UserObserver
import com.leon.su.presentation.observer.VerifyObserver
import com.leon.su.presentation.viewmodel.PasswordViewModel
import com.leon.su.presentation.viewmodel.UserViewModel
import com.leon.su.presentation.viewmodel.VerifyViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity :
    AppCompatActivity(R.layout.activity_splash),
    UserObserver.Interfaces,
    VerifyObserver.Interfaces,
    PasswordObserver.Interfaces {

    private val mSharedPreferences by inject<SharedPreferences>()
    private val mViewModel: UserViewModel by viewModel()
    private val mVerifyViewModel: VerifyViewModel by viewModel()
    private val mPasswordViewModel: PasswordViewModel by viewModel()
    private var mCurrentUser: FirebaseUser? = null
    private var isUmumLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(UserObserver(this, mViewModel, this))
        lifecycle.addObserver(VerifyObserver(this, mVerifyViewModel, this))
        lifecycle.addObserver(PasswordObserver(this, mPasswordViewModel, this))
        if (intent.data != null) {
            Firebase.dynamicLinks.getDynamicLink(intent).addOnSuccessListener {
                if (it != null) {
                    val deepLink = it.link
                    val oobCode = deepLink?.getQueryParameter("oobCode")
                    when {
                        deepLink.toString().contains("verify") -> {
                            mVerifyViewModel.verifyEmail(mCurrentUser, oobCode?.trim() ?: "")
                        }
                        deepLink.toString().contains("forgotPassword") -> {
                            mPasswordViewModel.verifyCodePassword(oobCode?.trim() ?: "")
                        }
                        else -> mViewModel.reloadCurrentUser()
                    }
                } else mViewModel.reloadCurrentUser()
            }.addOnFailureListener {
                start()
            }
        } else start()
    }

    private fun start() {
        if (mSharedPreferences.users != null) {
            mViewModel.reloadCurrentUser()
        } else {
            ActivityUtils.finishAllActivities(true)
            ActivityUtils.startActivity(LoginActivity::class.java)
        }
    }

    override fun onReloadSuccess() {
        mCurrentUser = Firebase.auth.currentUser
        mCurrentUser?.uid?.let {
            mViewModel.getUserData(it)
        } ?: onReloadFailed(Throwable("Uid salah!"))
        super.onReloadSuccess()
    }

    override fun onReloadFailed(e: Throwable) {
        ToastUtils.showShort(e.message)
        if (!isUmumLoaded) {
            ActivityUtils.startActivity(LoginActivity::class.java)
            isUmumLoaded = true
        }
        finish()
        super.onReloadFailed(e)
    }

    override fun onGetUserDataSuccess(user: UserResponse?) {
        if (mCurrentUser != null) {
            next(user?.data?.roles == Roles.ADMIN.value)
        } else {
            ActivityUtils.finishAllActivities(true)
            ActivityUtils.startActivity(LoginActivity::class.java)
        }
        super.onGetUserDataSuccess(user)
    }

    override fun onGetUserDataFailed(e: Throwable) {
        ToastUtils.showShort(e.message)
        ActivityUtils.finishAllActivities(true)
        ActivityUtils.startActivity(LoginActivity::class.java)
        super.onGetUserDataFailed(e)
    }

    override fun onVerifyEmailFailed(e: Throwable) {
        ToastUtils.showShort("Kode verifikasi salah atau sudah kadaluarsa!")
        mViewModel.reloadCurrentUser()
        super.onVerifyEmailFailed(e)
    }

    override fun onVerifyEmailSuccess() {
        ToastUtils.showShort("Email telah berhasil terverifikasi")
        mViewModel.reloadCurrentUser()
        super.onVerifyEmailSuccess()
    }

    override fun onVerifyCodePasswordSuccess(code: String) {
        ToastUtils.showShort("Kode verifikasi diterima!")
        // TODO PasswordActivity.launchConfirmReset(code)
        ActivityUtils.finishAllActivities(true)
        super.onVerifyCodePasswordSuccess(code)
    }

    override fun onVerifyCodePasswordFailed(e: Throwable) {
        ToastUtils.showShort("Kode salah atau sudah kadaluarsa!")
        mViewModel.reloadCurrentUser()
        super.onVerifyCodePasswordFailed(e)
    }

    private fun next(isAdmin: Boolean = false) {
        ActivityUtils.finishAllActivities(true)
        if (Firebase.auth.currentUser?.isEmailVerified == false) {
            ActivityUtils.startActivity(VerifyActivity::class.java)
        } else {
            openMenu(isAdmin)
        }
    }
}
