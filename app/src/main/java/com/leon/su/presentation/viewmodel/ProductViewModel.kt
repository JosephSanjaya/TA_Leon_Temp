package com.leon.su.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.leon.su.data.ProductRepository
import com.leon.su.domain.Product
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

    fun resetProductState() {
        _product.value = State.Idle()
    }

    fun product(product: Product) = viewModelScope.launch {
        repo.product(product)
            .catch {
                _product.emit(State.Failed(it))
            }
            .collect {
                _product.emit(it)
            }
    }

    private val _fetch = MutableStateFlow<State<String?>>(State.Idle())
    val mFetch: StateFlow<State<String?>> get() = _product

    fun resetFetchState() {
        _fetch.value = State.Idle()
    }

    fun fetchData() = viewModelScope.launch {
        repo.getProductFirestore()
            .catch {
                _product.emit(State.Failed(it))
            }
            .collect {
                _product.emit(it)
            }
    }

    fun productRegister(namaProduct: String, hGrosir: Double, hEcer: Double, berat: Double) = viewModelScope.launch {
        repo.productReg(namaProduct, hGrosir, hEcer, berat)
            .catch {
                _register.emit(State.Failed(it))
            }
            .collect {
                _register.emit(it)
            }
    }
}
