package com.leon.su.presentation.viewmodel

import com.leon.su.data.ProductRepository
import com.leon.su.domain.Product
import com.leon.su.domain.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository
) : BaseViewModel() {

    private val _insert = MutableStateFlow<State<Product.Response>>(State.Idle())
    val mInsert: StateFlow<State<Product.Response>> get() = _insert

    fun resetInsertState() {
        _insert.value = State.Idle()
    }

    fun insert(added: Product.Data) = ioScope.launch {
        repository.insert(added)
            .catch {
                _insert.emit(State.Failed(it))
            }
            .collect {
                _insert.emit(it)
            }
    }

    private val _edit = MutableStateFlow<State<Boolean>>(State.Idle())
    val mEdit: StateFlow<State<Boolean>> get() = _edit

    fun resetEditState() {
        _edit.value = State.Idle()
    }

    fun edit(
        edited: Product.Response
    ) = ioScope.launch {
        repository.edit(edited)
            .catch {
                _edit.emit(State.Failed(it))
            }
            .collect {
                _edit.emit(it)
            }
    }

    private val _getById = MutableStateFlow<State<Product.Response>>(State.Idle())
    val mGetById: StateFlow<State<Product.Response>> get() = _getById

    fun resetGetById() {
        _getById.value = State.Idle()
    }

    fun getById(
        id: String
    ) = ioScope.launch {
        repository.getById(id)
            .catch {
                _getById.emit(State.Failed(it))
            }
            .collect {
                _getById.emit(it)
            }
    }

    private val _sold = MutableStateFlow<State<Boolean>>(State.Idle())
    val mSold: StateFlow<State<Boolean>> get() = _sold

    fun resetSoldState() {
        _sold.value = State.Idle()
    }

    fun sold(
        sold: List<Product.Cart>
    ) = ioScope.launch {
        repository.sold(sold)
            .catch {
                _sold.emit(State.Failed(it))
            }
            .collect {
                _sold.emit(it)
            }
    }

    private val _add = MutableStateFlow<State<Boolean>>(State.Idle())
    val mAdd: StateFlow<State<Boolean>> get() = _add

    fun resetAddState() {
        _add.value = State.Idle()
    }

    fun add(
        add: List<Product.Cart>
    ) = ioScope.launch {
        repository.add(add)
            .catch {
                _add.emit(State.Failed(it))
            }
            .collect {
                _add.emit(it)
            }
    }

    private val _fetch = MutableStateFlow<State<List<Product.Response>>>(State.Idle())
    val mFetch: StateFlow<State<List<Product.Response>>> get() = _fetch

    fun resetFetchState() {
        _fetch.value = State.Idle()
    }

    fun fetchAll(type: Product.Type) = defaultScope.launch {
        repository.fetchAll(type)
            .catch {
                _fetch.emit(State.Failed(it))
            }
            .collect {
                _fetch.emit(it)
            }
    }

    fun fetch(status: Product.Status, type: Product.Type) = defaultScope.launch {
        repository.fetch(status, type)
            .catch {
                _fetch.emit(State.Failed(it))
            }
            .collect {
                _fetch.emit(it)
            }
    }

    private val _delete = MutableStateFlow<State<Boolean>>(State.Idle())
    val mDelete: StateFlow<State<Boolean>> get() = _delete

    fun resetDeleteState() {
        _delete.value = State.Idle()
    }

    fun delete(id: String) = defaultScope.launch {
        repository.delete(id)
            .catch {
                _delete.emit(State.Failed(it))
            }
            .collect {
                _delete.emit(it)
            }
    }
}
