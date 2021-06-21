package com.leon.su.data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leon.su.domain.Product
import com.leon.su.domain.State
import com.leon.su.domain.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class ProductRepository {
    suspend fun product(email: String, password: String, data: Users) = flow {
        emit(State.Loading())
        val result = Firebase.auth.createUserWithEmailAndPassword(email, password).await()
        Firebase.database.reference.child(Product.REF).child(result.user?.uid.toString()).setValue(
            data.apply {
                id = result.user?.uid
            }
        ).await()
        emit(State.Success(result.user))
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)

    suspend fun productReg(namaProduct: String, hGrosir: Double, hEcer: Double, berat: Double) = flow {
        emit(State.Loading())
        val push = Firebase.database.reference.child(Product.REF).push()
        push.setValue(Product.productDetail(idProduct = push.key, namaProduct = namaProduct, hargaGrosir = hGrosir, hargaEcer = hEcer, beratProduct = berat))
            .await()
        emit(State.Success(push.key))
    }
}