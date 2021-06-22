package com.leon.su.data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.leon.su.domain.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.tasks.await

class ProductRepository {
    suspend fun insert(product: ProductData) = flow {
        emit(State.Loading())
        val request = Firebase.firestore
            .collection(ProductData.REF)
            .add(product)
            .await()
        val result = request.get().await()
        emit(
            State.Success(
                ProductResponse(
                    id = result.id,
                    data = result.toObject(ProductData::class.java)
                )
            )
        )
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)

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

    suspend fun getProductDatabase() = callbackFlow {
        if (this.isActive) offer(State.Loading())
        val request = Firebase.database.reference.child(Product.REF)
        request.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                offer(State.Success(snapshot.getValue(Product.productDetail::class.java)))
            }

            override fun onCancelled(error: DatabaseError) {
                offer(State.Failed(error.toException()))
            }
        })
        awaitClose()
    }

    suspend fun getProductFirestore() = flow {
        emit(State.Loading())
        val request = Firebase.firestore
            .collection(Product.REF)
            .get()
        val result = request.await().map {
            it.toObject(Product.productDetail::class.java)
        }
        emit(State.Success(result))
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)

    suspend fun productReg(namaProduct: String, hGrosir: Double, hEcer: Double, berat: Double) =
        flow {
            emit(State.Loading())
            val push = Firebase.database.reference.child(Product.REF).push()
            push.setValue(
                Product.productDetail(
                    idProduct = push.key,
                    namaProduct = namaProduct,
                    hargaGrosir = hGrosir,
                    hargaEcer = hEcer,
                    beratProduct = berat
                )
            )
                .await()
            emit(State.Success(push.key))
        }
}
