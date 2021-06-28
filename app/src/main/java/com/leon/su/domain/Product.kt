package com.leon.su.domain

import com.google.firebase.database.PropertyName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object Product {
    const val REF = "products"

    @Serializable
    data class productDetail(
        @PropertyName("id")
        @SerialName("id")
        var idProduct: String? = null,

        @PropertyName("namaProduct")
        @SerialName("namaProduct")
        var namaProduct: String? = null,

        @PropertyName("hGrosir")
        @SerialName("hGrosir")
        var hargaGrosir: Double = 0.0,

        @PropertyName("hEcer")
        @SerialName("hEcer")
        var hargaEcer: Double = 0.0,

/*        @PropertyName("berat")
        @SerialName("berat")
        var beratProduct: Double = 0.0,
*/
        @PropertyName("stok")
        @SerialName("stok")
        var stokProduct: Int = 0
    )
}