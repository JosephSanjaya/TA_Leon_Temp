package com.leon.su.data

import com.blankj.utilcode.util.AppUtils
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leon.su.domain.State
import com.leon.su.domain.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class PasswordRepository {
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
                .setUrl("${UserData.FORGOT_URL}$email")
                .build()
        ).await()
        emit(State.Success(true))
    }.flowOn(Dispatchers.IO)

    suspend fun verifyPasswordResetCode(code: String) = flow {
        emit(State.Loading())
        Firebase.auth.verifyPasswordResetCode(code).await()
        emit(State.Success(code))
    }.flowOn(Dispatchers.IO)

    suspend fun confirmPasswordReset(code: String, password: String) = flow {
        emit(State.Loading())
        Firebase.auth.confirmPasswordReset(code, password).await()
        emit(State.Success(true))
    }.flowOn(Dispatchers.IO)

    suspend fun changePassword(oldPassword: String, newPassword: String) = flow {
        emit(State.Loading())
        val currentUser = Firebase.auth.currentUser
        if (currentUser == null) throw Throwable("Login terlebih dahulu!")
        else {
            val credential =
                EmailAuthProvider.getCredential(currentUser.email.toString(), oldPassword)
            currentUser.reauthenticate(credential).await()
            currentUser.updatePassword(newPassword).await()
            emit(State.Success(true))
        }
    }.flowOn(Dispatchers.IO)
}
