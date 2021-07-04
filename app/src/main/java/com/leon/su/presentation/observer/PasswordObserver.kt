package com.leon.su.presentation.observer

import androidx.lifecycle.*
import com.leon.su.domain.State
import com.leon.su.presentation.viewmodel.PasswordViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PasswordObserver(
    private val view: Interfaces,
    private val viewModel: PasswordViewModel,
    private val owner: LifecycleOwner
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        owner.lifecycleScope.launch {
            viewModel.sendForgot.collect {
                when (it) {
                    is State.Idle -> view.onSendForgotPasswordIdle()
                    is State.Loading -> view.onSendForgotPasswordLoading()
                    is State.Success -> {
                        view.onSendForgotPasswordSuccess()
                        viewModel.resetSendForgotState()
                    }
                    is State.Failed -> {
                        view.onSendForgotPasswordFailed(it.throwable)
                        viewModel.resetSendForgotState()
                    }
                }
            }
        }
        owner.lifecycleScope.launch {
            viewModel.verifyCodePassword.collect {
                when (it) {
                    is State.Idle -> view.onVerifyCodePasswordIdle()
                    is State.Loading -> view.onVerifyCodePasswordLoading()
                    is State.Success -> {
                        view.onVerifyCodePasswordSuccess(it.data)
                        viewModel.resetVerifyCodePasswordState()
                    }
                    is State.Failed -> {
                        view.onVerifyCodePasswordFailed(it.throwable)
                        viewModel.resetVerifyCodePasswordState()
                    }
                }
            }
        }
        owner.lifecycleScope.launch {
            viewModel.confirmReset.collect {
                when (it) {
                    is State.Idle -> view.onConfirmResetPasswordIdle()
                    is State.Loading -> view.onConfirmResetPasswordLoading()
                    is State.Success -> {
                        view.onConfirmResetPasswordSuccess()
                        viewModel.resetConfirmResetState()
                    }
                    is State.Failed -> {
                        view.onConfirmResetPasswordFailed(it.throwable)
                        viewModel.resetConfirmResetState()
                    }
                }
            }
        }
        owner.lifecycleScope.launch {
            viewModel.changePassword.collect {
                when (it) {
                    is State.Idle -> view.onChangePasswordIdle()
                    is State.Loading -> view.onChangePasswordLoading()
                    is State.Success -> {
                        view.onChangePasswordSuccess()
                        viewModel.resetChangePasswordState()
                    }
                    is State.Failed -> {
                        view.onChangePasswordFailed(it.throwable)
                        viewModel.resetChangePasswordState()
                    }
                }
            }
        }
    }

    interface Interfaces {
        fun onSendForgotPasswordIdle() {}
        fun onSendForgotPasswordLoading() {}
        fun onSendForgotPasswordFailed(e: Throwable) {}
        fun onSendForgotPasswordSuccess() {}

        fun onVerifyCodePasswordIdle() {}
        fun onVerifyCodePasswordLoading() {}
        fun onVerifyCodePasswordFailed(e: Throwable) {}
        fun onVerifyCodePasswordSuccess(code: String) {}

        fun onConfirmResetPasswordIdle() {}
        fun onConfirmResetPasswordLoading() {}
        fun onConfirmResetPasswordFailed(e: Throwable) {}
        fun onConfirmResetPasswordSuccess() {}

        fun onChangePasswordIdle() {}
        fun onChangePasswordLoading() {}
        fun onChangePasswordFailed(e: Throwable) {}
        fun onChangePasswordSuccess() {}
    }
}
