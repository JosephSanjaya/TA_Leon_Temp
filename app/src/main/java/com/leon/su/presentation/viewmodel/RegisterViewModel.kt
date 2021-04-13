package com.leon.su.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.leon.su.data.RegisterRepository
import com.leon.su.domain.State
import com.leon.su.domain.Users
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/*
 * Copyright (c) 2021 Designed and developed by Joseph Sanjaya, S.T., M.Kom., All Rights Reserved.
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/))
 */

class RegisterViewModel(
    private val repo: RegisterRepository
) : ViewModel() {

    private val _register = MutableStateFlow<State<FirebaseUser?>>(State.Idle())
    val mRegister: StateFlow<State<FirebaseUser?>> get() = _register

    fun resetRegisterState() {
        _register.value = State.Idle()
    }

    fun register(email: String, password: String, data: Users) = viewModelScope.launch {
        repo.register(email, password, data)
            .catch {
                _register.emit(State.Failed(it))
            }
            .collect {
                _register.emit(it)
            }
    }
}
