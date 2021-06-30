package com.leon.su.data

import android.content.SharedPreferences
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.leon.su.domain.State
import com.leon.su.domain.UserData
import com.leon.su.domain.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val mSharedPreferences: SharedPreferences
) {

    suspend fun getUserData(userUID: String) = flow {
        emit(State.Loading())
        val request = Firebase.firestore
            .collection(UserData.REF)
            .document(userUID)
            .get()
            .await()
        val result = UserResponse(
            id = request.id,
            data = request.toObject(UserData::class.java)
        )
        mSharedPreferences.users = result
        emit(State.Success(result))
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

    @ExperimentalCoroutinesApi
    suspend fun reloadCurrentUser() = callbackFlow {
        trySend(State.Loading())
        when (val currentUser = Firebase.auth.currentUser) {
            null -> {
                trySend(State.Failed(Throwable(UserData.MSG_USER_NOT_FOUND)))
                close(Throwable(UserData.MSG_USER_NOT_FOUND))
            }
            else -> {
                currentUser.reload().addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(State.Success(true))
                        close()
                    } else {
                        trySend(State.Failed(it.exception ?: Throwable()))
                        close(it.exception)
                    }
                }
            }
        }
        awaitClose()
    }

    suspend fun fetchUser() = flow {
        emit(State.Loading())
        val request = Firebase.firestore
            .collection(UserData.REF)
            .get()
            .await()
        val result = request.map {
            UserResponse(
                id = it.id,
                data = it.toObject(UserData::class.java)
            )
        }
        emit(State.Success(result))
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)

    suspend fun delete(password: String) = flow {
        emit(State.Loading())
        val user = Firebase.auth.currentUser
            ?: throw Throwable(Throwable(UserData.MSG_USER_NOT_FOUND))
        val credential =
            EmailAuthProvider.getCredential(user.email.toString(), password)
        val authRequest = user.reauthenticate(credential)
        authRequest.await()
        val userDeleteRequest = user.delete()
        userDeleteRequest.await()
        val dataDeleteRequest = Firebase.firestore
            .collection(UserData.REF)
            .document(user.uid)
            .delete()
        dataDeleteRequest.await()
        emit(State.Success(true))
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)

    suspend fun doLogout() = flow {
        emit(State.Loading())
        Firebase.auth.signOut()
        mSharedPreferences.edit().clear().apply()
        emit(State.Success(true))
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)
}
