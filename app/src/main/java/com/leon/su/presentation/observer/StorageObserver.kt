package com.leon.su.presentation.observer

import androidx.lifecycle.*
import com.google.firebase.storage.StorageReference
import com.leon.su.domain.State
import com.leon.su.presentation.viewmodel.StorageViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

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
        owner.lifecycleScope.launch {
            viewModel.mFetch.collect {
                when (it) {
                    is State.Idle -> view.onFetchIdle()
                    is State.Loading -> view.onFetchLoading()
                    is State.Success -> {
                        view.onFetchSuccess(it.data)
                        viewModel.resetFetchState()
                    }
                    is State.Failed -> {
                        view.onFetchFailed(it.throwable)
                        viewModel.resetFetchState()
                    }
                }
            }
        }
        owner.lifecycleScope.launch {
            viewModel.mFetchByRef.collect {
                when (it) {
                    is State.Idle -> view.onFetchByRefIdle()
                    is State.Loading -> view.onFetchByRefLoading()
                    is State.Success -> {
                        view.onFetchByRefSuccess(it.data)
                        viewModel.resetFetchByRefState()
                    }
                    is State.Failed -> {
                        view.onFetchByRefFailed(it.throwable)
                        viewModel.resetFetchByRefState()
                    }
                }
            }
        }
        owner.lifecycleScope.launch {
            viewModel.mDownload.collect {
                when (it) {
                    is State.Idle -> view.onDownloadIdle()
                    is State.Loading -> view.onDownloadLoading()
                    is State.Success -> {
                        view.onDownloadSuccess(it.data)
                        viewModel.resetDownloadState()
                    }
                    is State.Failed -> {
                        view.onDownloadFailed(it.throwable)
                        viewModel.resetDownloadState()
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

        fun onFetchIdle() {}
        fun onFetchLoading() {}
        fun onFetchFailed(e: Throwable) {}
        fun onFetchSuccess(data: MutableList<StorageReference>) {}

        fun onFetchByRefIdle() {}
        fun onFetchByRefLoading() {}
        fun onFetchByRefFailed(e: Throwable) {}
        fun onFetchByRefSuccess(data: MutableList<StorageReference>) {}

        fun onDownloadIdle() {}
        fun onDownloadLoading() {}
        fun onDownloadFailed(e: Throwable) {}
        fun onDownloadSuccess(data: File) {}
    }
}
