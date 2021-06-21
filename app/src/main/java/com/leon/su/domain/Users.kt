package com.leon.su.domain

import com.google.firebase.database.PropertyName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Users(
    @PropertyName("id")
    @SerialName("id")
    var id: String? = null,

    @PropertyName("email")
    @SerialName("email")
    var userEmail: String? = null,

    @PropertyName("uname")
    @SerialName("uname")
    var uname: String? = null,

    @PropertyName("nama")
    @SerialName("nama")
    var nama: String? = null,

    @PropertyName("roles")
    @SerialName("roles")
    var roles: String? = null,

) {
    companion object {
        const val FORGOT_URL = "https://"
        const val REF = "users"
    }
}
