package com.leon.su.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.ToastUtils
import com.leon.su.R
import com.leon.su.databinding.ActivityForgotBinding
import com.leon.su.presentation.observer.PasswordObserver
import com.leon.su.presentation.viewmodel.PasswordViewModel
import com.leon.su.utils.makeLoadingDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPasswordSendActivity :
    AppCompatActivity(R.layout.activity_forgot),
    View.OnClickListener,
    PasswordObserver.Interfaces {
    private val binding by viewBinding(ActivityForgotBinding::bind)
    private val mViewModel: PasswordViewModel by viewModel()
    private val loading by lazy { makeLoadingDialog(false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Lupa Password"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        lifecycle.addObserver(
            PasswordObserver(
                this,
                mViewModel,
                this
            )
        )
        binding.listener = this
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.btnSend -> if (binding.etEmail.text.isNullOrBlank())
                ToastUtils.showShort("Mohon isi email terlebih dahulu!")
            else mViewModel.sendForgotPassword(
                binding.etEmail.text.toString()
            )
        }
    }

    override fun onSendForgotPasswordLoading() {
        super.onSendForgotPasswordLoading()
        loading.show()
    }

    override fun onSendForgotPasswordFailed(e: Throwable) {
        super.onSendForgotPasswordFailed(e)
        ToastUtils.showShort(e.message)
        loading.dismiss()
    }

    override fun onSendForgotPasswordSuccess() {
        super.onSendForgotPasswordSuccess()
        ToastUtils.showShort(
            "Email telah terkirim, silahkan cek email anda!"
        )
        loading.dismiss()
        finish()
    }
}
