package com.leon.su.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    @SerialName("id")
    var id: String? = null,

    @SerialName("userEmail")
    var userEmail: String? = null,

    @SerialName("nama")
    var nama: String? = null,

    @SerialName("roles")
    var roles: String? = null
) {
    companion object {
        const val REF = "users"
    }
}
