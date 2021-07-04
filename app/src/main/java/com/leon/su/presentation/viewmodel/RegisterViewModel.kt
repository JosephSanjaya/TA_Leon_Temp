package com.leon.su.presentation.viewmodel

import com.google.firebase.auth.FirebaseUser
import com.leon.su.data.RegisterRepository
import com.leon.su.domain.State
import com.leon.su.domain.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: RegisterRepository
) : BaseViewModel() {

    private val _register = MutableStateFlow<State<FirebaseUser?>>(State.Idle())
    val mRegister: StateFlow<State<FirebaseUser?>> get() = _register

    fun resetRegisterState() {
        _register.value = State.Idle()
    }

    fun register(data: UserData, password: String) = ioScope.launch {
        repository.register(data, password)
            .catch { _register.emit(State.Failed(it)) }
            .collect { _register.emit(it) }
    }

    fun registerPegawai(creatorPassword: String, data: UserData, password: String) =
        ioScope.launch {
            repository.registerPegawai(creatorPassword, data, password)
                .catch { _register.emit(State.Failed(it)) }
                .collect { _register.emit(it) }
        }
}
