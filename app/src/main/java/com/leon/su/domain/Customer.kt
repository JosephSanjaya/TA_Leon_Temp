package com.leon.su.domain

import com.google.firebase.database.PropertyName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    @PropertyName("id")
    @SerialName("id")
    var idCustomer: String?= null,

    @PropertyName("nama")
    @SerialName("nama")
    var namaCustomer: String?= null,

    @PropertyName("alamat")
    @SerialName("alamat")
    var alamatCustomer: String?=null,

    @PropertyName("kontak")
    @SerialName("kontak")
    var kontakCustomer: String?=null
)