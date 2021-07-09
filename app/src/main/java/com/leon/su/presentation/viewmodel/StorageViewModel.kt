package com.leon.su.presentation.viewmodel

import com.google.firebase.storage.StorageReference
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

    private val _fetch = MutableStateFlow<State<MutableList<StorageReference>>>(State.Idle())
    val mFetch: StateFlow<State<MutableList<StorageReference>>> get() = _fetch

    fun resetFetchState() {
        _fetch.value = State.Idle()
    }

    fun fetch() = ioScope.launch {
        repository.fetch()
            .catch {
                _fetch.emit(State.Failed(it))
            }
            .collect {
                _fetch.emit(it)
            }
    }

    private val _fetchByRef = MutableStateFlow<State<MutableList<StorageReference>>>(State.Idle())
    val mFetchByRef: StateFlow<State<MutableList<StorageReference>>> get() = _fetchByRef

    fun resetFetchByRefState() {
        _fetchByRef.value = State.Idle()
    }

    fun fetchByRef(ref: StorageReference) = ioScope.launch {
        repository.fetchByReferences(ref)
            .catch {
                _fetchByRef.emit(State.Failed(it))
            }
            .collect {
                _fetchByRef.emit(it)
            }
    }

    private val _download = MutableStateFlow<State<File>>(State.Idle())
    val mDownload: StateFlow<State<File>> get() = _download

    fun resetDownloadState() {
        _download.value = State.Idle()
    }

    fun download(ref: StorageReference) = ioScope.launch {
        repository.download(ref)
            .catch {
                _download.emit(State.Failed(it))
            }
            .collect {
                _download.emit(it)
            }
    }
}
