package com.leon.su.data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leon.su.domain.State
import com.leon.su.domain.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

/*
 * Copyright (c) 2021 Designed and developed by Joseph Sanjaya, S.T., M.Kom., All Rights Reserved.
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/))
 */

class RegisterRepository {

    suspend fun register(
        email: String,
        password: String,
        data: Users
    ) = flow {
        emit(State.Loading())
        val result = Firebase.auth.createUserWithEmailAndPassword(email, password).await()
        Firebase.database.reference.child(Users.REF).child(result.user?.uid.toString()).setValue(
            data.apply {
                id = result.user?.uid
            }
        ).await()
        emit(State.Success(result.user))
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)

    suspend fun buatSomething(nama: String, deskripsi: String) = flow {
        emit(State.Loading())
        val push = Firebase.database.reference.child("mata_pelajaran").push()
//        push.setValue(MataPelajaran.Detail(id = push.key, nama = nama, deskripsi = deskripsi))
//            .await()
        emit(State.Success(push.key))
    }

}
