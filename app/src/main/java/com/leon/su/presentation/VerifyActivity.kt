/*
 * Copyright (c) 2021 Designed and developed by Joseph Sanjaya, S.T., M.Kom., All Rights Reserved.
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/))
 */

package com.leon.su.presentation

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableBoolean
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leon.su.R
import com.leon.su.databinding.ActivityVerifyBinding
import com.leon.su.presentation.observer.VerifyObserver
import com.leon.su.presentation.viewmodel.VerifyViewModel
import com.leon.su.utils.makeLoadingDialog
import com.soywiz.klock.DateTime
import org.koin.androidx.viewmodel.ext.android.viewModel

class VerifyActivity :
    AppCompatActivity(R.layout.activity_verify),
    View.OnClickListener,
    VerifyObserver.Interfaces {
    private val isCountdownEnabled = ObservableBoolean(false)
    private val binding by viewBinding(ActivityVerifyBinding::bind)
    private val mViewModel: VerifyViewModel by viewModel()
    private val loading by lazy { makeLoadingDialog(false) }
    private val mTimer by lazy {
        object : CountDownTimer(15000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val second = DateTime.fromUnix(millisUntilFinished).seconds
                val text = "Mohon tunggu $second detik.."
                binding.tvCountdown.text = text
            }

            override fun onFinish() {
                isCountdownEnabled.set(false)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Verifikasi"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        lifecycle.addObserver(
            VerifyObserver(
                this,
                mViewModel,
                this
            )
        )
        binding.tvEmail.text = Firebase.auth.currentUser?.email
        binding.listener = this
        binding.isCountDownEnabled = isCountdownEnabled
        mViewModel.sendEmailVerification(Firebase.auth.currentUser)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.btnRetry -> mViewModel.sendEmailVerification(
                Firebase.auth.currentUser
            )
            binding.btnLogout -> {
                Firebase.auth.signOut()
                ActivityUtils.startActivity(LoginActivity::class.java)
                finish()
            }
        }
    }

    override fun onDestroy() {
        mTimer.cancel()
        super.onDestroy()
    }

    override fun onSendingEmailVerificationLoading() {
        loading.show()
        super.onSendingEmailVerificationLoading()
    }

    override fun onSendingEmailVerificationSuccess() {
        isCountdownEnabled.set(true)
        mTimer.start()
        ToastUtils.showShort(
            "Silahkan cek email anda!"
        )
        loading.dismiss()
        super.onSendingEmailVerificationSuccess()
    }

    override fun onSendingEmailVerificationFailed(e: Throwable) {
        ToastUtils.showShort(e.message)
        loading.dismiss()
        super.onSendingEmailVerificationFailed(e)
    }
}
