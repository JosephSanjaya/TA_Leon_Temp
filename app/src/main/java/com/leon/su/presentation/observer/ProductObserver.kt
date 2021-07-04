package com.leon.su.presentation.observer

import androidx.lifecycle.*
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
            viewModel.mGetById.collect {
                when (it) {
                    is State.Idle -> view.onGetByIdIdle()
                    is State.Loading -> view.onGetByIdLoading()
                    is State.Success -> {
                        view.onGetByIdSuccess(it.data)
                        viewModel.resetGetById()
                    }
                    is State.Failed -> {
                        view.onGetByIdFailed(it.throwable)
                        viewModel.resetGetById()
                    }
                }
            }
        }
        owner.lifecycleScope.launch {
            viewModel.mSold.collect {
                when (it) {
                    is State.Idle -> view.onSoldProductIdle()
                    is State.Loading -> view.onSoldProductLoading()
                    is State.Success -> {
                        view.onSoldProductSuccess()
                        viewModel.resetSoldState()
                    }
                    is State.Failed -> {
                        view.onSoldProductFailed(it.throwable)
                        viewModel.resetSoldState()
                    }
                }
            }
        }
        owner.lifecycleScope.launch {
            viewModel.mAdd.collect {
                when (it) {
                    is State.Idle -> view.onAddProductIdle()
                    is State.Loading -> view.onAddProductLoading()
                    is State.Success -> {
                        view.onAddProductSuccess()
                        viewModel.resetAddState()
                    }
                    is State.Failed -> {
                        view.onAddProductFailed(it.throwable)
                        viewModel.resetAddState()
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

        fun onGetByIdIdle() {
            // Do Nothing
        }

        fun onGetByIdLoading() {
            // Do Nothing
        }

        fun onGetByIdFailed(e: Throwable) {
            // Do Nothing
        }

        fun onGetByIdSuccess(data: Product.Response) {
            // Do Nothing
        }

        fun onSoldProductIdle() {
            // Do Nothing
        }

        fun onSoldProductLoading() {
            // Do Nothing
        }

        fun onSoldProductFailed(e: Throwable) {
            // Do Nothing
        }

        fun onSoldProductSuccess() {
            // Do Nothing
        }

        fun onAddProductIdle() {
            // Do Nothing
        }

        fun onAddProductLoading() {
            // Do Nothing
        }

        fun onAddProductFailed(e: Throwable) {
            // Do Nothing
        }

        fun onAddProductSuccess() {
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
