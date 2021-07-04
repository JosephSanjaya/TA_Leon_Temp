package com.leon.su.presentation.viewmodel

import com.leon.su.data.PasswordRepository
import com.leon.su.domain.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PasswordViewModel(
    private val repository: PasswordRepository
) : BaseViewModel() {

    private val _sendForgot = MutableStateFlow<State<Boolean>>(State.Idle())
    val sendForgot: StateFlow<State<Boolean>> get() = _sendForgot

    fun resetSendForgotState() {
        _sendForgot.value = State.Idle()
    }

    fun sendForgotPassword(email: String) = ioScope.launch {
        repository.sendPasswordResetEmail(email)
            .catch { _sendForgot.emit(State.Failed(it)) }
            .collect { _sendForgot.emit(it) }
    }

    private val _verifyCodePassword = MutableStateFlow<State<String>>(State.Idle())
    val verifyCodePassword: StateFlow<State<String>> get() = _verifyCodePassword

    fun resetVerifyCodePasswordState() {
        _verifyCodePassword.value = State.Idle()
    }

    fun verifyCodePassword(code: String) = ioScope.launch {
        repository.verifyPasswordResetCode(code)
            .catch { _verifyCodePassword.emit(State.Failed(it)) }
            .collect { _verifyCodePassword.emit(it) }
    }

    private val _confirmReset = MutableStateFlow<State<Boolean>>(State.Idle())
    val confirmReset: StateFlow<State<Boolean>> get() = _confirmReset

    fun resetConfirmResetState() {
        _confirmReset.value = State.Idle()
    }

    fun confirmPasswordReset(code: String, email: String) = ioScope.launch {
        repository.confirmPasswordReset(code, email)
            .catch { _confirmReset.emit(State.Failed(it)) }
            .collect { _confirmReset.emit(it) }
    }

    private val _changePassword = MutableStateFlow<State<Boolean>>(State.Idle())
    val changePassword: StateFlow<State<Boolean>> get() = _changePassword

    fun resetChangePasswordState() {
        _changePassword.value = State.Idle()
    }

    fun changePassword(oldPassword: String, newPassword: String) = ioScope.launch {
        repository.changePassword(oldPassword, newPassword)
            .catch { _changePassword.emit(State.Failed(it)) }
            .collect { _changePassword.emit(it) }
    }
}
