package com.leon.su.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.leon.su.data.ProductRepository
import com.leon.su.domain.Product
import com.leon.su.domain.ProductData
import com.leon.su.domain.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProductViewModel(private val repo: ProductRepository) : ViewModel() {
    private val _product = MutableStateFlow<State<String?>>(State.Idle())
    val mProduct: StateFlow<State<String?>> get() = _product

    private val _register = MutableStateFlow<State<FirebaseUser?>>(State.Idle())
    val mRegister: StateFlow<State<FirebaseUser?>> get() = _register

    private val _fetch = MutableStateFlow<State<String?>>(State.Idle())
    val mFetch: StateFlow<State<String?>> get() = _product

    fun resetProductState() {
        _product.value = State.Idle()
    }

    fun resetFetchState() {
        _fetch.value = State.Idle()
    }

    fun fetchData() = viewModelScope.launch {
        repo.getProduct()
            .catch {
                _product.emit(State.Failed(it))
            }
            .collect {
                _product.emit(it)
            }
    }

    fun productRegister(productData: ProductData) = viewModelScope.launch {
        repo.productReg(productData)
            .catch {
                _register.emit(State.Failed(it))
            }
            .collect {
                _register.emit(it)
            }
    }

    /*    fun product(product: Product) = viewModelScope.launch {
        repo.product(product)
            .catch {
                _product.emit(State.Failed(it))
            }
            .collect {
                _product.emit(it)
            }
    }
*/
}
