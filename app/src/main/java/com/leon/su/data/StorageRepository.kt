/*
 * Copyright (c) 2021 Designed and developed by Joseph Sanjaya, S.T., M.Kom., All Rights Reserved.
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/))
 */

package com.leon.su.data

import android.content.SharedPreferences
import com.blankj.utilcode.util.UriUtils
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.leon.su.domain.State
import com.soywiz.klock.DateTime
import java.io.File
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class StorageRepository(
    val mSharedPreferences: SharedPreferences
) {
    suspend fun upload(file: File) = flow {
        emit(State.Loading())
        val upload = Firebase.storage.reference
            .child(DateTime.nowLocal().toString("dd/MM/yyyy"))
            .child(
                "${mSharedPreferences.users?.data?.nama} - ${
                DateTime.nowLocal().toString("HH:mm")
                }"
            )
            .putFile(UriUtils.file2Uri(file))
        upload.await()
        emit(State.Success(true))
    }
}
