package com.leon.su.data

import com.blankj.utilcode.util.AppUtils
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leon.su.domain.State
import com.leon.su.domain.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class VerifyRepository {

    suspend fun sendEmailVerification(user: FirebaseUser?) = flow<State<Boolean>> {
        emit(State.Loading())
        when (user) {
            null -> {
                emit(State.Failed(Throwable(UserData.MSG_USER_NOT_FOUND)))
            }
            else -> {
                user.sendEmailVerification(
                    ActionCodeSettings.newBuilder()
                        .setHandleCodeInApp(true)
                        .setAndroidPackageName(
                            AppUtils.getAppPackageName(),
                            true,
                            AppUtils.getAppVersionName()
                        )
                        .setUrl("${UserData.VERIFY_URL}${user.uid}")
                        .build()
                ).await()
                emit(State.Success(true))
            }
        }
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)

    suspend fun verifyEmail(user: FirebaseUser?, oobCode: String) = flow {
        emit(State.Loading())
        Firebase.auth
            .applyActionCode(oobCode)
            .await()
        user?.reload()
        emit(State.Success(true))
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)
}
