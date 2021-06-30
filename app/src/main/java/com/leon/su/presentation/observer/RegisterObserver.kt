/*
 * Copyright (c) 2021 Designed and developed by Joseph Sanjaya, S.T., M.Kom., All Rights Reserved.
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/))
 */

package com.leon.su.presentation.observer

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseUser
import com.leon.su.domain.State
import com.leon.su.presentation.viewmodel.RegisterViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RegisterObserver(
    private val view: Interfaces,
    private val viewModel: RegisterViewModel,
    private val owner: LifecycleOwner
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        owner.lifecycleScope.launch {
            viewModel.mRegister.collect {
                when (it) {
                    is State.Idle -> view.onRegisterIdle()
                    is State.Loading -> view.onRegisterLoading()
                    is State.Success -> {
                        view.onRegisterSuccess(it.data)
                        viewModel.resetRegisterState()
                    }
                    is State.Failed -> {
                        view.onRegisterFailed(it.throwable)
                        viewModel.resetRegisterState()
                    }
                }
            }
        }
    }

    interface Interfaces {
        fun onRegisterIdle() {}
        fun onRegisterLoading() {}
        fun onRegisterFailed(e: Throwable) {}
        fun onRegisterSuccess(user: FirebaseUser?) {}
    }
}
