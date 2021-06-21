package com.leon.su.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.leon.su.R
import com.leon.su.databinding.ActivityMainBinding
import com.leon.su.domain.State
import com.leon.su.domain.Users
import com.leon.su.presentation.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity :
    AppCompatActivity(R.layout.activity_main),
    View.OnClickListener {

    private val mBinding by viewBinding(ActivityMainBinding::bind)
    private val mViewModel by viewModel<UserViewModel>()
    private val mUsers = Users()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.listener = this
        stateObserve()
    }

    private fun stateObserve() {
        lifecycleScope.launch {
            mViewModel.mLogin.collect {
                when (it) {
                    is State.Idle -> {
                        // idle
                    }
                    is State.Loading -> {
                        // loading
                    }
                    is State.Success -> {
                        mViewModel.resetLoginState()
                        // success
                    }
                    is State.Failed -> {
                        mViewModel.resetLoginState()
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            mBinding.LoginButton -> {
                mViewModel.login(
                    mBinding.etUsername.text.toString(),
                    mBinding.etPassword.text.toString(),
                    mUsers.apply {
                        nama = mBinding.etUsername.text.toString()
                        roles = "karyawan"
                    }
                )
            }
        }
    }
}
