package com.leon.su.presentation.observer

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import com.leon.su.domain.State
import com.leon.su.domain.UserResponse
import com.leon.su.presentation.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserObserver(
    private val view: Interfaces,
    private val viewModel: UserViewModel,
    private val owner: LifecycleOwner
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        owner.lifecycleScope.launch {
            viewModel.reload.collect {
                when (it) {
                    is State.Idle -> view.onReloadIdle()
                    is State.Loading -> view.onReloadLoading()
                    is State.Success -> {
                        view.onReloadSuccess()
                        viewModel.resetReloadState()
                    }
                    is State.Failed -> {
                        view.onReloadFailed(it.throwable)
                        viewModel.resetReloadState()
                    }
                }
            }
        }
        owner.lifecycleScope.launch {
            viewModel.login.collect {
                when (it) {
                    is State.Idle -> view.onLoginIdle()
                    is State.Loading -> view.onLoginLoading()
                    is State.Success -> {
                        view.onLoginSuccess(it.data)
                        viewModel.resetLoginState()
                    }
                    is State.Failed -> {
                        view.onLoginFailed(it.throwable)
                        viewModel.resetLoginState()
                    }
                }
            }
        }
        owner.lifecycleScope.launch {
            viewModel.mUser.collect {
                when (it) {
                    is State.Idle -> view.onGetUserDataIdle()
                    is State.Loading -> view.onGetUserDataLoading()
                    is State.Success -> {
                        view.onGetUserDataSuccess(it.data)
                        viewModel.resetGetUserData()
                    }
                    is State.Failed -> {
                        view.onGetUserDataFailed(it.throwable)
                        viewModel.resetGetUserData()
                    }
                }
            }
        }
        owner.lifecycleScope.launch {
            viewModel.logout.collect {
                when (it) {
                    is State.Idle -> view.onLogoutIdle()
                    is State.Loading -> view.onLogoutLoading()
                    is State.Success -> {
                        view.onLogoutSuccess()
                        viewModel.resetLogout()
                    }
                    is State.Failed -> {
                        view.onLogoutFailed(it.throwable)
                        viewModel.resetLogout()
                    }
                }
            }
        }
        owner.lifecycleScope.launch {
            viewModel.mDelete.collect {
                when (it) {
                    is State.Idle -> view.onDeleteUserIdle()
                    is State.Loading -> view.onDeleteUserLoading()
                    is State.Success -> {
                        view.onDeleteUserSuccess()
                        viewModel.resetDeleteState()
                    }
                    is State.Failed -> {
                        view.onDeleteUserFailed(it.throwable)
                        viewModel.resetDeleteState()
                    }
                }
            }
        }
        owner.lifecycleScope.launch {
            viewModel.mFetchUsers.collect {
                when (it) {
                    is State.Idle -> view.onFetchUsersIdle()
                    is State.Loading -> view.onFetchUsersLoading()
                    is State.Success -> {
                        view.onFetchUsersSuccess(it.data)
                        viewModel.resetFetchUsersState()
                    }
                    is State.Failed -> {
                        view.onFetchUsersFailed(it.throwable)
                        viewModel.resetFetchUsersState()
                    }
                }
            }
        }
    }

    interface Interfaces {
        fun onGetUserDataIdle() {}
        fun onGetUserDataLoading() {}
        fun onGetUserDataFailed(e: Throwable) {}
        fun onGetUserDataSuccess(user: UserResponse?) {}

        fun onDeleteUserIdle() {}
        fun onDeleteUserLoading() {}
        fun onDeleteUserSuccess() {}
        fun onDeleteUserFailed(e: Throwable) {}

        fun onReloadIdle() {}
        fun onReloadLoading() {}
        fun onReloadFailed(e: Throwable) {}
        fun onReloadSuccess() {}

        fun onLoginIdle() {}
        fun onLoginLoading() {}
        fun onLoginFailed(e: Throwable) {}
        fun onLoginSuccess(user: FirebaseUser?) {}

        fun onLogoutIdle() {}
        fun onLogoutLoading() {}
        fun onLogoutFailed(e: Throwable) {}
        fun onLogoutSuccess() {}

        fun onFetchUsersIdle() {}
        fun onFetchUsersLoading() {}
        fun onFetchUsersFailed(e: Throwable) {}
        fun onFetchUsersSuccess(data: List<UserResponse>) {}
    }
}
