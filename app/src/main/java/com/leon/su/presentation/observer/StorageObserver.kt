package com.leon.su.presentation.observer

import androidx.lifecycle.*
import com.leon.su.domain.State
import com.leon.su.presentation.viewmodel.StorageViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class StorageObserver(
    private val view: Interfaces,
    private val viewModel: StorageViewModel,
    private val owner: LifecycleOwner
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        owner.lifecycleScope.launch {
            viewModel.mUpload.collect {
                when (it) {
                    is State.Idle -> view.onUploadIdle()
                    is State.Loading -> view.onUploadLoading()
                    is State.Success -> {
                        view.onUploadSuccess()
                        viewModel.resetUploadState()
                    }
                    is State.Failed -> {
                        view.onUploadFailed(it.throwable)
                        viewModel.resetUploadState()
                    }
                }
            }
        }
    }

    interface Interfaces {
        fun onUploadIdle() {}
        fun onUploadLoading() {}
        fun onUploadFailed(e: Throwable) {}
        fun onUploadSuccess() {}
    }
}
