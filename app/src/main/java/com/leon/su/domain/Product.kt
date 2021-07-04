package com.leon.su.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.floor

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

        @SerialName("product")
        var product: Response? = null,
    ) {
        /**
         * @return Pair
         * - first = nilai ecer
         * - second = nilai grosir
         */
        fun getEcerGrosir(): Pair<Int, Int> {
            return if (product?.data?.grosirUnit == 1) {
                Pair(quantity, 0)
            } else {
                val value = quantity.toDouble() / if (product?.data?.grosirUnit == null ||
                    product?.data?.grosirUnit == 0
                ) 1.0 else (product?.data?.grosirUnit ?: 1).toDouble()
                val mult = floor(value)
                val grosir = mult.toInt()
                val ecer = quantity - (grosir * (product?.data?.grosirUnit ?: 1))
                return Pair(ecer, grosir)
            }
        }

        fun getTotal(): Double {
            val ecerGrosir = getEcerGrosir()
            return if (ecerGrosir.second == 0) {
                quantity * (product?.data?.hargaEcer ?: 0.0)
            } else {
                (ecerGrosir.first * (product?.data?.hargaEcer ?: 0.0)) +
                    (ecerGrosir.second * (product?.data?.hargaGrosir ?: 0.0))
            }
        }
    }

    @Serializable
    data class Response(
        @SerialName("id")
        var id: String? = null,

        @SerialName("product")
        var data: Data? = null,
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
