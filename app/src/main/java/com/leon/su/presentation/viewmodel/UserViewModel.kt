package com.leon.su.presentation.viewmodel

import com.google.firebase.auth.FirebaseUser
import com.leon.su.data.UserRepository
import com.leon.su.domain.Flags
import com.leon.su.domain.State
import com.leon.su.domain.UserResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository
) : BaseViewModel() {

    private val _logout = MutableStateFlow<State<Boolean>>(State.Idle())
    val logout: StateFlow<State<Boolean>> get() = _logout

    fun resetLogout() {
        _logout.value = State.Idle()
    }

    fun doLogout() = ioScope.launch {
        repository.doLogout().catch { _logout.emit(State.Failed(it)) }
            .collect { _logout.emit(it) }
    }

    private val _login = MutableStateFlow<State<FirebaseUser?>>(State.Idle())
    val login: StateFlow<State<FirebaseUser?>> get() = _login

    fun resetLoginState() {
        _login.value = State.Idle()
    }

    fun login(email: String, password: String) = ioScope.launch {
        repository.login(email, password).catch { _login.emit(State.Failed(it)) }
            .collect { _login.emit(it) }
    }

    private val _reload = MutableStateFlow<State<Boolean>>(State.Idle())
    val reload: StateFlow<State<Boolean>> get() = _reload

    fun resetReloadState() {
        _reload.value = State.Idle()
    }

    fun reloadCurrentUser() = ioScope.launch {
        repository.reloadCurrentUser().catch { _reload.emit(State.Failed(it)) }
            .collect { _reload.emit(it) }
    }

    private val _user = MutableStateFlow<State<Pair<UserResponse, Flags?>>>(State.Idle())
    val mUser: StateFlow<State<Pair<UserResponse, Flags?>>> get() = _user

    fun resetGetUserData() {
        _user.value = State.Idle()
    }

    fun getUserData(userUID: String) = ioScope.launch {
        repository.getUserData(userUID).catch { _user.emit(State.Failed(it)) }
            .collect { _user.emit(it) }
    }

    private val _fetchUsers = MutableStateFlow<State<List<UserResponse>>>(State.Idle())
    val mFetchUsers: StateFlow<State<List<UserResponse>>> get() = _fetchUsers

    fun resetFetchUsersState() {
        _fetchUsers.value = State.Idle()
    }

    fun fetchUsers() = ioScope.launch {
        repository.fetchUser().catch { _fetchUsers.emit(State.Failed(it)) }
            .collect { _fetchUsers.emit(it) }
    }

    private val _delete = MutableStateFlow<State<Boolean>>(State.Idle())
    val mDelete: StateFlow<State<Boolean>> get() = _delete

    fun resetDeleteState() {
        _delete.value = State.Idle()
    }

    fun delete(password: String) = ioScope.launch {
        repository.delete(password)
            .catch {
                _delete.emit(State.Failed(it))
            }
            .collect {
                _delete.emit(it)
            }
    }

    private val _flags = MutableStateFlow<State<Boolean>>(State.Idle())
    val mFlags: StateFlow<State<Boolean>> get() = _flags

    fun resetFlagsState() {
        _flags.value = State.Idle()
    }

    fun setFlags(flags: Flags) = ioScope.launch {
        repository.setFlags(flags)
            .catch {
                _flags.emit(State.Failed(it))
            }
            .collect {
                _flags.emit(it)
            }
    }
}
