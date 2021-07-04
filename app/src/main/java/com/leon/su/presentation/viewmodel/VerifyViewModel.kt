package com.leon.su.presentation.viewmodel

import com.google.firebase.auth.FirebaseUser
import com.leon.su.data.VerifyRepository
import com.leon.su.domain.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class VerifyViewModel(
    private val repository: VerifyRepository
) : BaseViewModel() {

    private val _emailVerify = MutableStateFlow<State<Boolean>>(State.Idle())
    val emailVerify: StateFlow<State<Boolean>> get() = _emailVerify

    fun resetEmailVerifyState() {
        _emailVerify.value = State.Idle()
    }

    fun sendEmailVerification(user: FirebaseUser?) = ioScope.launch {
        repository.sendEmailVerification(user)
            .catch { _emailVerify.emit(State.Failed(it)) }
            .collect { _emailVerify.emit(it) }
    }

    private val _verifyEmail = MutableStateFlow<State<Boolean>>(State.Idle())
    val verifyEmail: StateFlow<State<Boolean>> get() = _verifyEmail

    fun resetVerifyEmailState() {
        _emailVerify.value = State.Idle()
    }

    fun verifyEmail(user: FirebaseUser?, oobCode: String) = ioScope.launch {
        repository.verifyEmail(user, oobCode)
            .catch { _verifyEmail.emit(State.Failed(it)) }
            .collect { _verifyEmail.emit(it) }
    }
}
