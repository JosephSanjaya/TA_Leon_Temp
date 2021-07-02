package com.leon.su.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object Product {
    const val REF = "products"

    enum class Status(val value: String) {
        DISCONTINUED("discontinued"),
        ACTIVE("active"),
        ALL("all"),
    }

    enum class Type(val value: String) {
        MAKANAN("makanan"),
        MINUMAN("minuman"),
        LAINNYA("lainnya"),
        ALL("all"),
    }

    @Serializable
    data class Cart(
        @SerialName("quantity")
        var quantity: Int = 0,

        @SerialName("total")
        var total: Double = 0.0,

        @SerialName("product")
        var product: Data? = null,
    )

    @Serializable
    data class Response(
        @SerialName("id")
        var id: String? = null,

        @SerialName("product")
        var product: Data? = null,
    )

    @Serializable
    data class Data(
        @SerialName("namaProduct")
        var namaProduct: String? = null,

        @SerialName("hargaGrosir")
        var hargaGrosir: Double = 0.0,

        @SerialName("grosirUnit")
        var grosirUnit: Int = 0,

        @SerialName("hargaEcer")
        var hargaEcer: Double = 0.0,

        @SerialName("hargaModal")
        var hargaModal: Double = 0.0,

        @SerialName("status")
        var status: String = Status.ACTIVE.value,

        @SerialName("stok")
        var stok: Int = 0,

        @SerialName("type")
        var type: String? = null,
    )
}
