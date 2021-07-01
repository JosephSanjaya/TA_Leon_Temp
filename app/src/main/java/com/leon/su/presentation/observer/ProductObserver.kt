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
import com.leon.su.domain.Product
import com.leon.su.domain.State
import com.leon.su.presentation.viewmodel.ProductViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProductObserver(
    private val view: Interfaces,
    private val viewModel: ProductViewModel,
    private val owner: LifecycleOwner
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        owner.lifecycleScope.launch {
            viewModel.mInsert.collect {
                when (it) {
                    is State.Idle -> view.onBuatProductIdle()
                    is State.Loading -> view.onBuatProductLoading()
                    is State.Success -> {
                        view.onBuatProductSuccess(it.data)
                        viewModel.resetInsertState()
                    }
                    is State.Failed -> {
                        view.onBuatProductFailed(it.throwable)
                        viewModel.resetInsertState()
                    }
                }
            }
        }
        owner.lifecycleScope.launch {
            viewModel.mEdit.collect {
                when (it) {
                    is State.Idle -> view.onEditProductIdle()
                    is State.Loading -> view.onEditProductLoading()
                    is State.Success -> {
                        view.onEditProductSuccess()
                        viewModel.resetEditState()
                    }
                    is State.Failed -> {
                        view.onEditProductFailed(it.throwable)
                        viewModel.resetEditState()
                    }
                }
            }
        }
        owner.lifecycleScope.launch {
            viewModel.mFetch.collect {
                when (it) {
                    is State.Idle -> view.onFetchProductIdle()
                    is State.Loading -> view.onFetchProductLoading()
                    is State.Success -> {
                        view.onFetchProductSuccess(it.data)
                        viewModel.resetFetchState()
                    }
                    is State.Failed -> {
                        view.onFetchProductFailed(it.throwable)
                        viewModel.resetFetchState()
                    }
                }
            }
        }
        owner.lifecycleScope.launch {
            viewModel.mDelete.collect {
                when (it) {
                    is State.Idle -> view.onDeleteProductIdle()
                    is State.Loading -> view.onDeleteProductLoading()
                    is State.Success -> {
                        view.onDeleteProductSuccess()
                        viewModel.resetDeleteState()
                    }
                    is State.Failed -> {
                        view.onDeleteProductFailed(it.throwable)
                        viewModel.resetDeleteState()
                    }
                }
            }
        }
    }

    interface Interfaces {
        fun onBuatProductIdle() {
            // Do Nothing
        }

        fun onBuatProductLoading() {
            // Do Nothing
        }

        fun onBuatProductFailed(e: Throwable) {
            // Do Nothing
        }

        fun onBuatProductSuccess(added: Product.Response?) {
            // Do Nothing
        }

        fun onEditProductIdle() {
            // Do Nothing
        }

        fun onEditProductLoading() {
            // Do Nothing
        }

        fun onEditProductFailed(e: Throwable) {
            // Do Nothing
        }

        fun onEditProductSuccess() {
            // Do Nothing
        }

        fun onFetchProductIdle() {
            // Do Nothing
        }

        fun onFetchProductLoading() {
            // Do Nothing
        }

        fun onFetchProductFailed(e: Throwable) {
            // Do Nothing
        }

        fun onFetchProductSuccess(data: List<Product.Response>) {
            // Do Nothing
        }

        fun onDeleteProductIdle() {
            // Do Nothing
        }

        fun onDeleteProductLoading() {
            // Do Nothing
        }

        fun onDeleteProductFailed(e: Throwable) {
            // Do Nothing
        }

        fun onDeleteProductSuccess() {
            // Do Nothing
        }
    }
}
