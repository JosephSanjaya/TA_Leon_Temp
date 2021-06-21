package com.leon.su.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.leon.su.data.UserRepository
import com.leon.su.domain.State
import com.leon.su.domain.Users
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserViewModel (private val repo: UserRepository) : ViewModel() {
    private val _login = MutableStateFlow<State<FirebaseUser?>>(State.Idle())
    val mLogin: StateFlow<State<FirebaseUser?>> get() = _login

    private val _register = MutableStateFlow<State<FirebaseUser?>>(State.Idle())
    val mRegister: StateFlow<State<FirebaseUser?>> get() = _register

        fun resetLoginState() {
            _login.value = State.Idle()
        }

    fun login(email: String, password: String, data: Users) = viewModelScope.launch {
        repo.login(email, password).catch {
                _login.emit(State.Failed(it))
            }
            .collect {
                _login.emit(it)
            }
    }
    fun userRegister(email: String, password: String, data: Users) = viewModelScope.launch {
        repo.userReg(email, password, data)
            .catch {
                _register.emit(State.Failed(it))
            }
            .collect {
                _register.emit(it)
            }
    }

}