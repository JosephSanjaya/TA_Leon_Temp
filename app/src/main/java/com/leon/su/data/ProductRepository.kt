package com.leon.su.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.leon.su.domain.Product
import com.leon.su.domain.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class ProductRepository {

    suspend fun insert(added: Product.Data) = flow {
        emit(State.Loading())
        val add = Firebase.firestore
            .collection(Product.REF)
            .add(added)
            .await()
        val result = add.get().await()
        emit(
            State.Success(
                Product.Response(
                    id = result.id,
                    data = result.toObject(Product.Data::class.java)
                )
            )
        )
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)

    suspend fun edit(edited: Product.Response) = flow {
        if (edited.data == null)
            throw Throwable("Product tidak ditemukan!")
        emit(State.Loading())
        val request = Firebase.firestore
            .collection(Product.REF)
            .document(edited.id.toString())
            .set(edited.data!!)
        request.await()
        emit(State.Success(true))
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)

    suspend fun getById(id: String) = flow {
        emit(State.Loading())
        val request = Firebase.firestore
            .collection(Product.REF)
            .document(id)
            .get()
        val result = request.await()
        emit(
            State.Success(
                Product.Response(
                    id = result.id,
                    data = result.toObject(Product.Data::class.java)
                )
            )
        )
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)

    suspend fun sold(sold: List<Product.Cart>) = flow {
        emit(State.Loading())
        val existing = Firebase.firestore
            .collection(Product.REF)
            .whereEqualTo("status", Product.Status.ACTIVE.value)
            .get()
            .await()
            .map {
                Product.Response(
                    id = it.id,
                    data = it.toObject(Product.Data::class.java)
                )
            }
        sold.filter {
            it.product != null
        }.forEach { cart ->
            val exist = existing.first {
                it.id == cart.product!!.id
            }
            val request = Firebase.firestore
                .collection(Product.REF)
                .document(cart.product?.id.toString())
                .set(
                    cart.product?.data!!.apply {
                        stok = (exist.data?.stok ?: 0) - cart.quantity
                    }
                )
            request.await()
        }
        emit(State.Success(true))
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)

    suspend fun add(add: List<Product.Cart>) = flow {
        emit(State.Loading())
        val existing = Firebase.firestore
            .collection(Product.REF)
            .whereEqualTo("status", Product.Status.ACTIVE.value)
            .get()
            .await()
            .map {
                Product.Response(
                    id = it.id,
                    data = it.toObject(Product.Data::class.java)
                )
            }
        add.filter {
            it.product != null
        }.forEach { cart ->
            val exist = existing.first {
                it.id == cart.product!!.id
            }
            val request = Firebase.firestore
                .collection(Product.REF)
                .document(cart.product?.id.toString())
                .set(
                    cart.product?.data!!.apply {
                        stok = (exist.data?.stok ?: 0) + cart.quantity
                    }
                )
            request.await()
        }
        emit(State.Success(true))
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)

    suspend fun delete(id: String) = flow {
        emit(State.Loading())
        val request = Firebase.firestore
            .collection(Product.REF)
            .document(id)
            .get()
        request.await()
        emit(State.Success(true))
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)

    suspend fun fetch(status: Product.Status, type: Product.Type) = flow {
        emit(State.Loading())
        val result = Firebase.firestore
            .collection(Product.REF)
            .whereEqualTo("type", type.value)
            .whereEqualTo("status", status.value)
            .get().await()
            .map {
                Product.Response(
                    id = it.id,
                    data = it.toObject(Product.Data::class.java)
                )
            }
        emit(State.Success(result))
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)

    suspend fun fetchAll(type: Product.Type) = flow {
        emit(State.Loading())
        val result = Firebase.firestore
            .collection(Product.REF)
            .whereEqualTo("type", type.value)
            .get().await()
            .map {
                Product.Response(
                    id = it.id,
                    data = it.toObject(Product.Data::class.java)
                )
            }
        emit(State.Success(result))
    }.catch {
        throw it
    }.flowOn(Dispatchers.IO)
}
