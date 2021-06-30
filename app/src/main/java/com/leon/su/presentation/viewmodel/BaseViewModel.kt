/*
 * Copyright (c) 2021 Designed and developed by Joseph Sanjaya, S.T., M.Kom., All Rights Reserved.
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/))
 */

package com.leon.su.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract class BaseViewModel : ViewModel() {

    protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        LogUtils.e(throwable)
    }
    protected val mainScope = CoroutineScope(Dispatchers.Main + exceptionHandler)
    protected val ioScope = CoroutineScope(Dispatchers.IO + exceptionHandler)
    protected val defaultScope = CoroutineScope(viewModelScope.coroutineContext + exceptionHandler)

    override fun onCleared() {
        super.onCleared()
        defaultScope.coroutineContext.cancel()
        mainScope.coroutineContext.cancel()
        ioScope.coroutineContext.cancel()
    }
}
