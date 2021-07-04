package com.leon.su.presentation

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.blankj.utilcode.util.ToastUtils
import com.google.firebase.auth.FirebaseUser
import com.leon.su.R
import com.leon.su.databinding.ActivityRegisterBinding
import com.leon.su.domain.Roles
import com.leon.su.domain.UserData
import com.leon.su.presentation.observer.RegisterObserver
import com.leon.su.presentation.viewmodel.RegisterViewModel
import com.leon.su.utils.makeLoadingDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterPegawaiActivity :
    AppCompatActivity(R.layout.activity_register),
    RegisterObserver.Interfaces,
    View.OnClickListener {

    private val mBinding by viewBinding(ActivityRegisterBinding::bind)
    private val mViewModel by viewModel<RegisterViewModel>()
    private val mLoading by lazy {
        makeLoadingDialog(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(RegisterObserver(this, mViewModel, this))
        mBinding.listener = this
    }

    override fun onRegisterLoading() {
        super.onRegisterLoading()
        mLoading.show()
    }

    override fun onRegisterFailed(e: Throwable) {
        super.onRegisterFailed(e)
        mLoading.dismiss()
        ToastUtils.showShort(e.message)
    }

    override fun onRegisterSuccess(user: FirebaseUser?) {
        super.onRegisterSuccess(user)
        ToastUtils.showShort("Berhasil mendaftarkan akun!")
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onClick(v: View?) {
        when (v) {
            mBinding.RegisterButton -> {
                mViewModel.registerPegawai(
                    creatorPassword = mBinding.etCreatorPassword.text.toString(),
                    data = UserData(
                        userEmail = mBinding.etEmail.text.toString(),
                        nama = mBinding.etNamaLengkap.text.toString(),
                        roles = Roles.PEGAWAI.value
                    ),
                    password = mBinding.etPassword.text.toString()
                )
            }
        }
    }
}