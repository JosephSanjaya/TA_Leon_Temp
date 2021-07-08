package com.leon.su.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.ToastUtils
import com.leon.su.R
import com.leon.su.databinding.ActivityNewPasswordBinding
import com.leon.su.presentation.observer.PasswordObserver
import com.leon.su.presentation.viewmodel.PasswordViewModel
import com.leon.su.utils.makeLoadingDialog
import com.skydoves.bundler.bundle
import com.skydoves.bundler.intentOf
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPasswordActivity :
    AppCompatActivity(R.layout.activity_new_password),
    View.OnClickListener,
    PasswordObserver.Interfaces {
    private val binding by viewBinding(ActivityNewPasswordBinding::bind)
    private val mViewModel: PasswordViewModel by viewModel()
    private val loading by lazy { makeLoadingDialog(false) }
    private val mCode by bundle(CODE_KEY, "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Password Baru"
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
            binding.btnSend -> when {
                binding.etPassword.text.isNullOrBlank() ||
                    binding.etPasswordConfirm.text.isNullOrBlank() ->
                    ToastUtils.showShort("Mohon isi Password!")
                !binding.etPassword.text.contentEquals(binding.etPasswordConfirm.text) ->
                    ToastUtils.showShort("Password tidak sama!")
                else -> mViewModel.confirmPasswordReset(
                    mCode,
                    binding.etPassword.text.toString()
                )
            }
        }
    }

    override fun onConfirmResetPasswordLoading() {
        super.onConfirmResetPasswordLoading()
        loading.show()
    }

    override fun onConfirmResetPasswordFailed(e: Throwable) {
        super.onConfirmResetPasswordFailed(e)
        ToastUtils.showShort(e.message)
        loading.dismiss()
    }

    override fun onConfirmResetPasswordSuccess() {
        super.onConfirmResetPasswordSuccess()
        ToastUtils.showShort(
            "Password telah diganti!!"
        )
        loading.dismiss()
        finish()
    }

    companion object {
        const val CODE_KEY = "code"
        fun Context.launchNewPassword(code: String) = intentOf<NewPasswordActivity> {
            putExtra(CODE_KEY to code)
            startActivity(this@launchNewPassword)
        }
    }
}
