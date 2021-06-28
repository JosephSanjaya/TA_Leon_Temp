package com.leon.su.data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.leon.su.domain.State
import com.leon.su.domain.UserData
import com.leon.su.domain.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class UserRepository {

    suspend fun userReg(user: UserData) = flow {
        emit(State.Loading())
        val request = Firebase.firestore
            .collection(UserData.REF)
            .add(user)
            .await()
        val result = request.get().await()
        emit(
            State.Success(
                UserResponse(
                    id = result.id,
                    data = result.toObject(UserData::class.java)
                )
            )
        )
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)

    suspend fun login(email: String, password: String) = flow {
        emit(State.Loading())
        val result = Firebase.auth.signInWithEmailAndPassword(email, password).await()
        emit(State.Success(result.user))
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)

    /*
    suspend fun userRegOld(email: String, password: String, data: Users) = flow {
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

    suspend fun buatSomething(nama: String, peran: String) = flow {
        emit(State.Loading())
        val push = Firebase.database.reference.child("user").push()
//        push.setValue(MataPelajaran.Detail(id = push.key, nama = nama, peran = peran))
//            .await()
        emit(State.Success(push.key))
    }
     */
}
