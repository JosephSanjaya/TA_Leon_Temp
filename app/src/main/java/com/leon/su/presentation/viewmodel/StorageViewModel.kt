package com.leon.su.presentation.viewmodel

import com.leon.su.data.StorageRepository
import com.leon.su.domain.State
import java.io.File
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class StorageViewModel(
    private val repository: StorageRepository
) : BaseViewModel() {

    private val _upload = MutableStateFlow<State<Boolean>>(State.Idle())
    val mUpload: StateFlow<State<Boolean>> get() = _upload

    fun resetUploadState() {
        _upload.value = State.Idle()
    }

    fun upload(file: File) = ioScope.launch {
        repository.upload(file)
            .catch {
                _upload.emit(State.Failed(it))
            }
            .collect {
                _upload.emit(it)
            }
    }
}
