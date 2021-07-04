package com.leon.su.data

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.leon.su.domain.State
import com.leon.su.domain.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class RegisterRepository {

    suspend fun register(data: UserData, password: String) = flow {
        emit(State.Loading())
        val result = Firebase.auth
            .createUserWithEmailAndPassword(data.userEmail.toString(), password)
            .await()
        Firebase.firestore
            .collection(UserData.REF)
            .document(result.user?.uid.toString())
            .set(data)
            .await()
        emit(State.Success(result.user))
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)

    suspend fun registerPegawai(
        creatorPassword: String,
        data: UserData,
        password: String,
    ) = flow {
        emit(State.Loading())
        val creator = Firebase.auth.currentUser
        val credential = EmailAuthProvider
            .getCredential(creator?.email.toString(), creatorPassword)
        creator?.reauthenticate(credential)?.await()
        val result = Firebase.auth
            .createUserWithEmailAndPassword(data.userEmail.toString(), password)
            .await()
        Firebase.firestore
            .collection(UserData.REF)
            .document(result.user?.uid.toString())
            .set(data)
            .await()
        Firebase.auth.signOut()
        Firebase.auth.signInWithCredential(credential).await()
        emit(State.Success(result.user))
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)
}
