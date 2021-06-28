package com.leon.su.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductData(
    @SerialName("namaProduct")
    var namaProduct: String? = null,

    @SerialName("hGrosir")
    var hargaGrosir: Double = 0.0,

    @SerialName("hEcer")
    var hargaEcer: Double = 0.0,

    @SerialName("satuan")
    var satuanProduct: Double = 0.0,

    @SerialName("stok")
    var stokProduct: Int = 0
) {
    companion object {
        const val REF = "product"
    }
}
