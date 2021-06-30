package com.leon.su.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("id")
    var id: String? = null,

    @SerialName("user")
    var data: UserData? = null,
)
