package com.leon.su.presentation.observer

import androidx.lifecycle.*
import com.leon.su.domain.State
import com.leon.su.presentation.viewmodel.VerifyViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class VerifyObserver(
    private val view: Interfaces,
    private val viewModel: VerifyViewModel,
    private val owner: LifecycleOwner
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        owner.lifecycleScope.launch {
            viewModel.emailVerify.collect {
                when (it) {
                    is State.Idle -> view.onSendingEmailVerificationIdle()
                    is State.Loading -> view.onSendingEmailVerificationLoading()
                    is State.Success -> {
                        view.onSendingEmailVerificationSuccess()
                        viewModel.resetEmailVerifyState()
                    }
                    is State.Failed -> {
                        view.onSendingEmailVerificationFailed(it.throwable)
                        viewModel.resetEmailVerifyState()
                    }
                }
            }
        }
        owner.lifecycleScope.launch {
            viewModel.verifyEmail.collect {
                when (it) {
                    is State.Idle -> view.onVerifyEmailIdle()
                    is State.Loading -> view.onVerifyEmailLoading()
                    is State.Success -> {
                        view.onVerifyEmailSuccess()
                        viewModel.resetVerifyEmailState()
                    }
                    is State.Failed -> {
                        view.onVerifyEmailFailed(it.throwable)
                        viewModel.resetVerifyEmailState()
                    }
                }
            }
        }
    }

    interface Interfaces {
        fun onSendingEmailVerificationIdle() {}
        fun onSendingEmailVerificationLoading() {}
        fun onSendingEmailVerificationFailed(e: Throwable) {}
        fun onSendingEmailVerificationSuccess() {}

        fun onVerifyEmailIdle() {}
        fun onVerifyEmailLoading() {}
        fun onVerifyEmailFailed(e: Throwable) {}
        fun onVerifyEmailSuccess() {}
    }
}
