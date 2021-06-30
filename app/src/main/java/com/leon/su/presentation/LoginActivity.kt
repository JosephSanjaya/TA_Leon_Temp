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
import com.leon.su.presentation.observer.UserObserver
import com.leon.su.presentation.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity :
    AppCompatActivity(R.layout.activity_main),
    UserObserver.Interfaces,
    View.OnClickListener {

    private val mBinding by viewBinding(ActivityMainBinding::bind)
    private val mViewModel by viewModel<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(UserObserver(this, mViewModel, this))
        mBinding.listener = this
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
}
