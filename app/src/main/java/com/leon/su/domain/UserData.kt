package com.leon.su.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    @SerialName("userEmail")
    var userEmail: String? = null,

    @SerialName("nama")
    var nama: String? = null,

    @SerialName("roles")
    var roles: String? = null
) {

    companion object {
        const val REF = "users"
        const val FORGOT_URL = "https://sumberulumapp.page.link/forgotPassword?email="
        const val VERIFY_URL = "https://sumberulumapp.page.link/verify?uid="
        const val MSG_USER_NOT_FOUND = "User tidak ditemukan, silahkan login terlebih dahulu!"
    }
}
