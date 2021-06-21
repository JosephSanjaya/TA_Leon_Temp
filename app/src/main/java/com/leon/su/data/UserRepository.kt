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

class UserRepository {

    suspend fun login(email: String, password: String) = flow {
        emit(State.Loading())
        val result = Firebase.auth.signInWithEmailAndPassword(email, password).await()
        emit(State.Success(result.user))
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)

    suspend fun userReg(email: String, password: String, data: Users) = flow {
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


/*
    suspend fun sendPasswordResetEmail(email: String) = flow {
        emit(State.Loading())
        Firebase.auth.sendPasswordResetEmail(
            email,
            ActionCodeSettings.newBuilder()
                .setHandleCodeInApp(true)
                .setAndroidPackageName(
                    AppUtils.getAppPackageName(),
                    true,
                    AppUtils.getAppVersionName()
                )
                .setUrl("${Users.FORGOT_URL}$email")
                .build()
        ).await()
        emit(State.Success(true))
    }.flowOn(Dispatchers.IO)
*/
    suspend fun buatSomething(nama: String, peran: String) = flow {
        emit(State.Loading())
        val push = Firebase.database.reference.child("user").push()
//        push.setValue(MataPelajaran.Detail(id = push.key, nama = nama, peran = peran))
//            .await()
        emit(State.Success(push.key))
    }

}